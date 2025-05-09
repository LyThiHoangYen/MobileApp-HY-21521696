package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagerDashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView employeeRecyclerView;
    private List<Task> taskList;
    private List<EmployeeData> employeeList;
    private TaskUserAdapter taskAdapter;
    private EmployeeAdapter employeeAdapter;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ManagerDashboardFragment() {
        // Required empty public constructor
    }

    public static ManagerDashboardFragment newInstance() {
        return new ManagerDashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_dashboard, container, false);
        
        // Set up task RecyclerView
        recyclerView = view.findViewById(R.id.manager_task_recycler_view);
        setupTasksList();
        
        // Set up employee RecyclerView
        employeeRecyclerView = view.findViewById(R.id.employee_recycler_view);
        employeeList = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(employeeList, getActivity());
        
        RecyclerView.LayoutManager employeeLayoutManager = new LinearLayoutManager(getActivity());
        employeeRecyclerView.setAdapter(employeeAdapter);
        employeeRecyclerView.setNestedScrollingEnabled(false);
        employeeRecyclerView.setLayoutManager(employeeLayoutManager);
        
        // Load employee data from Firestore
        loadEmployees();
        
        return view;
    }
    
    private void setupTasksList() {
        taskList = new ArrayList<>();
        
        // Thêm tính năng điểm danh/chấm công cho role manager
        taskList.add(new Task("Điểm danh/ Chấm công", R.drawable.task_qr, "Điểm danh bằng QR code"));
        
        // Thêm các task dành cho quản lý giống như hình ảnh
        taskList.add(new Task("Quản lý nhân viên", R.drawable.task_quanlynhanvien, "Xem danh sách và thông tin nhân viên"));
        taskList.add(new Task("Duyệt đơn nghỉ phép", R.drawable.task_duyetdon, "Kiểm tra và phê duyệt đơn xin nghỉ"));
        taskList.add(new Task("Báo cáo chấm công", R.drawable.task_tomtat, "Xem báo cáo điểm danh của nhân viên"));
        taskList.add(new Task("Tạo thông báo", R.drawable.task_thongbao, "Tạo thông báo cho nhân viên"));
        taskList.add(new Task("Thống kê hiệu suất", R.drawable.task_thongke, "Xem hiệu suất làm việc của nhân viên"));
        
        taskAdapter = new TaskUserAdapter(taskList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
    }
    
    private void loadEmployees() {
        db.collection("employees")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && getActivity() != null) {
                    employeeList.clear();
                    
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("fullName");
                        String position = document.getString("position");
                        String department = document.getString("department");
                        String imageUrl = document.getString("imageUrl");
                        
                        if (name != null) {
                            EmployeeData employee = new EmployeeData(id, name, position, department, imageUrl);
                            employeeList.add(employee);
                        }
                    }
                    
                    employeeAdapter.notifyDataSetChanged();
                }
            });
    }
} 