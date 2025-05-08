package com.example.assignment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class donxinphep extends AppCompatActivity {
    private static final String TAG = "DonXinPhep";
    
    private EditText etName, etApplicationDate, etFromDate, etToDate, etReason;
    private RadioButton rbAnnualLeave, rbMedicalLeave, rbOtherReason;
    private RadioGroup rgLeaveType;
    private Button btnSubmit;
    private TextView tvTotalDays;
    private ProgressBar progressBar;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();
    
    private RadioGroup startDayShiftRadioGroup;
    private RadioGroup endDayShiftRadioGroup;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donxinphep);
        
        // Firebase initialization
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        // Initialize UI components
        setupUI();
        
        // Set up date pickers
        setupDatePickers();
        
        // Set up leave date calculation
        setupDateCalculation();
        
        // Load user data if available
        loadUserData();
        
        // Set up submit button
        setupSubmitButton();
    }
    
    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        
        etName = findViewById(R.id.editTextTextPersonName);
        etApplicationDate = findViewById(R.id.editText);
        etFromDate = findViewById(R.id.editTextTextPersonName2);
        etToDate = findViewById(R.id.editTextTextPersonName3);
        etReason = findViewById(R.id.editTextTextMultiLine);
        
        rbAnnualLeave = findViewById(R.id.checkBox4);
        rbMedicalLeave = findViewById(R.id.checkBox);
        rbOtherReason = findViewById(R.id.checkBox2);
        rgLeaveType = findViewById(R.id.radioGroupLeaveType);
        
        tvTotalDays = findViewById(R.id.textView6);
        btnSubmit = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        
        // Initialize day shift radio groups
        startDayShiftRadioGroup = findViewById(R.id.startDayShiftRadioGroup);
        endDayShiftRadioGroup = findViewById(R.id.endDayShiftRadioGroup);
        
        // Set current date as application date
        etApplicationDate.setText(dateFormat.format(new Date()));
        
        // Set default selection for leave type
        rbAnnualLeave.setChecked(true);
        
        // Show/hide reason field based on selected leave type
        rgLeaveType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.checkBox2) {
                // "Other reason" is selected, show the reason field
                etReason.setVisibility(View.VISIBLE);
            } else {
                // Hide the reason field for other leave types
                if (!TextUtils.isEmpty(etReason.getText())) {
                    etReason.setText("");
                }
                etReason.setVisibility(View.GONE);
            }
        });
        
        // Initially hide the reason field if "Other reason" is not selected
        if (!rbOtherReason.isChecked()) {
            etReason.setVisibility(View.GONE);
        }
        
        // Setup attachments
        Button btnAttachFile = findViewById(R.id.btnAttachFile);
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        
        btnAttachFile.setOnClickListener(v -> {
            // File attachment functionality would be implemented here
            Toast.makeText(this, "Chức năng đính kèm tệp đang được phát triển", Toast.LENGTH_SHORT).show();
        });
        
        btnTakePhoto.setOnClickListener(v -> {
            // Photo capture functionality would be implemented here
            Toast.makeText(this, "Chức năng chụp ảnh đang được phát triển", Toast.LENGTH_SHORT).show();
        });
        
        // Set up preview button
        Button btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(v -> {
            if (validateForm()) {
                // Preview functionality would be implemented here
                Toast.makeText(this, "Xem trước đơn xin nghỉ phép", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupDatePickers() {
        // Application date picker
        etApplicationDate.setOnClickListener(v -> showDatePicker(etApplicationDate));
        
        // From date picker
        etFromDate.setOnClickListener(v -> showDatePicker(etFromDate, date -> {
            try {
                // Ensure the "to date" is not before the "from date"
                Date fromDate = dateFormat.parse(etFromDate.getText().toString());
                if (!TextUtils.isEmpty(etToDate.getText())) {
                    Date toDate = dateFormat.parse(etToDate.getText().toString());
                    if (toDate.before(fromDate)) {
                        etToDate.setText(etFromDate.getText());
                    }
                }
                calculateLeaveDays();
            } catch (ParseException e) {
                Log.e(TAG, "Date parsing error: " + e.getMessage());
            }
        }));
        
        // To date picker
        etToDate.setOnClickListener(v -> showDatePicker(etToDate, date -> calculateLeaveDays()));
    }
    
    private void setupDateCalculation() {
        // Update calculation when dates change
        etFromDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) calculateLeaveDays();
        });
        
        etToDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) calculateLeaveDays();
        });
        
        // Add listeners to radio groups to update calculation when shift selection changes
        startDayShiftRadioGroup.setOnCheckedChangeListener((group, checkedId) -> calculateLeaveDays());
        endDayShiftRadioGroup.setOnCheckedChangeListener((group, checkedId) -> calculateLeaveDays());
    }
    
    private void calculateLeaveDays() {
        if (!TextUtils.isEmpty(etFromDate.getText()) && !TextUtils.isEmpty(etToDate.getText())) {
            try {
                Date fromDate = dateFormat.parse(etFromDate.getText().toString());
                Date toDate = dateFormat.parse(etToDate.getText().toString());
                
                if (fromDate != null && toDate != null) {
                    long diffInMillis = Math.abs(toDate.getTime() - fromDate.getTime());
                    double diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1; // +1 to include both start and end days
                    
                    // Adjust for half-day selections if same day
                    if (diffInDays == 1) {
                        RadioButton selectedStartShift = findViewById(startDayShiftRadioGroup.getCheckedRadioButtonId());
                        if (selectedStartShift != null && 
                            (selectedStartShift.getId() == R.id.startDayMorningOnly || 
                             selectedStartShift.getId() == R.id.startDayAfternoonOnly)) {
                            diffInDays = 0.5;
                        }
                    } 
                    // Adjust for multi-day selections
                    else if (diffInDays > 1) {
                        // Check start day shift
                        RadioButton selectedStartShift = findViewById(startDayShiftRadioGroup.getCheckedRadioButtonId());
                        if (selectedStartShift != null && 
                            (selectedStartShift.getId() == R.id.startDayMorningOnly || 
                             selectedStartShift.getId() == R.id.startDayAfternoonOnly)) {
                            diffInDays -= 0.5;
                        }
                        
                        // Check end day shift
                        RadioButton selectedEndShift = findViewById(endDayShiftRadioGroup.getCheckedRadioButtonId());
                        if (selectedEndShift != null && 
                            (selectedEndShift.getId() == R.id.endDayMorningOnly || 
                             selectedEndShift.getId() == R.id.endDayAfternoonOnly)) {
                            diffInDays -= 0.5;
                        }
                    }
                    
                    // Format display text
                    String displayText;
                    if (diffInDays == 0.5) {
                        displayText = "Tổng số ngày nghỉ: 0.5 ngày";
                    } else if (diffInDays == Math.floor(diffInDays)) {
                        displayText = "Tổng số ngày nghỉ: " + (int)diffInDays + " ngày";
                    } else {
                        displayText = "Tổng số ngày nghỉ: " + diffInDays + " ngày";
                    }
                    
                    tvTotalDays.setText(displayText);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Error calculating leave days: " + e.getMessage());
                tvTotalDays.setText("Tổng số ngày nghỉ: 0 ngày");
            }
        }
    }
    
    private void loadUserData() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            
            db.collection("employees").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        if (name != null && !name.isEmpty()) {
                            etName.setText(name);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading user data: " + e.getMessage());
                });
        }
    }
    
    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                submitLeaveRequest();
            }
        });
    }
    
    private boolean validateForm() {
        boolean valid = true;
        
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Vui lòng nhập tên");
            valid = false;
        }
        
        if (TextUtils.isEmpty(etApplicationDate.getText())) {
            etApplicationDate.setError("Vui lòng chọn ngày làm đơn");
            valid = false;
        }
        
        if (TextUtils.isEmpty(etFromDate.getText())) {
            etFromDate.setError("Vui lòng chọn ngày bắt đầu nghỉ");
            valid = false;
        }
        
        if (TextUtils.isEmpty(etToDate.getText())) {
            etToDate.setError("Vui lòng chọn ngày kết thúc nghỉ");
            valid = false;
        }
        
        // Validate from date is not after to date
        if (!TextUtils.isEmpty(etFromDate.getText()) && !TextUtils.isEmpty(etToDate.getText())) {
            try {
                Date fromDate = dateFormat.parse(etFromDate.getText().toString());
                Date toDate = dateFormat.parse(etToDate.getText().toString());
                
                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    etFromDate.setError("Ngày bắt đầu không thể sau ngày kết thúc");
                    valid = false;
                }
            } catch (ParseException e) {
                Log.e(TAG, "Date validation error: " + e.getMessage());
                valid = false;
            }
        }
        
        if (!rbAnnualLeave.isChecked() && !rbMedicalLeave.isChecked() && !rbOtherReason.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn loại nghỉ phép", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        
        // If "Other reason" is selected, reason text is required
        if (rbOtherReason.isChecked() && TextUtils.isEmpty(etReason.getText())) {
            etReason.setError("Vui lòng nhập lý do nghỉ phép");
            valid = false;
        }
        
        return valid;
    }
    
    private void submitLeaveRequest() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Get values from form
        String name = etName.getText().toString();
        String fromDateStr = etFromDate.getText().toString();
        String toDateStr = etToDate.getText().toString();
        
        // Get leave type
        String leaveType;
        if (rbAnnualLeave.isChecked()) {
            leaveType = "annual_leave";
        } else if (rbMedicalLeave.isChecked()) {
            leaveType = "medical_leave";
        } else {
            leaveType = "other";
        }
        
        // Get reason if provided
        String reason = etReason.getText().toString().trim();
        
        try {
            // Parse dates
            Date fromDate = dateFormat.parse(fromDateStr);
            Date toDate = dateFormat.parse(toDateStr);
            
            // Calculate total days
            long diffInMillis = Math.abs(toDate.getTime() - fromDate.getTime());
            double totalDays = (double) (TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1);
            
            // Get shift selections for start and end days
            RadioButton selectedStartShift = findViewById(startDayShiftRadioGroup.getCheckedRadioButtonId());
            RadioButton selectedEndShift = findViewById(endDayShiftRadioGroup.getCheckedRadioButtonId());
            
            String startDayShift = "whole_day";
            if (selectedStartShift != null) {
                if (selectedStartShift.getId() == R.id.startDayMorningOnly) {
                    startDayShift = "morning";
                } else if (selectedStartShift.getId() == R.id.startDayAfternoonOnly) {
                    startDayShift = "afternoon";
                }
            }
            
            String endDayShift = "whole_day";
            if (selectedEndShift != null) {
                if (selectedEndShift.getId() == R.id.endDayMorningOnly) {
                    endDayShift = "morning";
                } else if (selectedEndShift.getId() == R.id.endDayAfternoonOnly) {
                    endDayShift = "afternoon";
                }
            }
            
            // Adjust total days based on shifts
            if (totalDays == 1) {
                if (startDayShift.equals("morning") || startDayShift.equals("afternoon")) {
                    totalDays = 0.5;
                }
            } else if (totalDays > 1) {
                if (startDayShift.equals("morning") || startDayShift.equals("afternoon")) {
                    totalDays -= 0.5;
                }
                if (endDayShift.equals("morning") || endDayShift.equals("afternoon")) {
                    totalDays -= 0.5;
                }
            }
            
            // Create the leave request
            String userId = auth.getCurrentUser().getUid();
            
            Map<String, Object> leaveRequest = new HashMap<>();
            leaveRequest.put("employeeId", userId);
            leaveRequest.put("employeeName", name);
            leaveRequest.put("applicationDate", new Date());
            leaveRequest.put("fromDate", fromDate);
            leaveRequest.put("toDate", toDate);
            leaveRequest.put("totalDays", totalDays);
            leaveRequest.put("leaveType", leaveType);
            leaveRequest.put("reason", reason);
            leaveRequest.put("status", "pending");
            leaveRequest.put("startDayShift", startDayShift);
            leaveRequest.put("endDayShift", endDayShift);
            leaveRequest.put("hasAttachments", false); // Set based on attachments
            
            // Save to Firestore
            db.collection("leave_requests")
                .add(leaveRequest)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(donxinphep.this, "Đơn xin nghỉ phép đã được gửi", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to leave status screen
                    Intent intent = new Intent(donxinphep.this, TinhTrangDonPhep.class);
                    startActivity(intent);
                    finish(); // Close this form
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Error submitting leave request: " + e.getMessage());
                    Toast.makeText(donxinphep.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                
        } catch (ParseException e) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Date parsing error: " + e.getMessage());
            Toast.makeText(this, "Lỗi xử lý ngày tháng", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void resetForm() {
        etFromDate.setText("");
        etToDate.setText("");
        etReason.setText("");
        rbAnnualLeave.setChecked(true);
        rbMedicalLeave.setChecked(false);
        rbOtherReason.setChecked(false);
        tvTotalDays.setText("Tổng số ngày nghỉ: 0 ngày");
    }
    
    private void showDatePicker(EditText targetEditText) {
        showDatePicker(targetEditText, null);
    }
    
    private void showDatePicker(EditText targetEditText, DateSelectedCallback callback) {
        Calendar currentDate = Calendar.getInstance();
        
        // If the field already has a date, use it as the initial date in the picker
        if (!TextUtils.isEmpty(targetEditText.getText())) {
            try {
                Date date = dateFormat.parse(targetEditText.getText().toString());
                if (date != null) {
                    currentDate.setTime(date);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing date: " + e.getMessage());
            }
        }
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                
                targetEditText.setText(dateFormat.format(selectedDate.getTime()));
                targetEditText.setError(null);
                
                if (callback != null) {
                    callback.onDateSelected(selectedDate.getTime());
                }
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }
    
    // Interface for date selection callback
    private interface DateSelectedCallback {
        void onDateSelected(Date date);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}