package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UpdateAnhTheActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnOpen;
    
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_anh_the_nv);

        imageView = findViewById(R.id.image_view);
        btnOpen = findViewById(R.id.button2);
        
        // Initialize the Activity Result Launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap captureImage = getBitmapFromExtras(extras);
                        if (captureImage != null) {
                            imageView.setImageBitmap(captureImage);
                        }
                    }
                }
            }
        );

        if (ContextCompat.checkSelfPermission(com.example.assignment.UpdateAnhTheActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(com.example.assignment.UpdateAnhTheActivity.this, new String[]{
                    Manifest.permission.CAMERA}, 100);
        }
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            }
        });
    }
<<<<<<< Updated upstream
<<<<<<< Updated upstream

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
=======
=======
>>>>>>> Stashed changes
    
    // Helper method to get bitmap with appropriate API based on Android version
    private Bitmap getBitmapFromExtras(Bundle extras) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return extras.getParcelable("data", Bitmap.class);
        } else {
            // Suppressing warning for backward compatibility
            @SuppressWarnings("deprecation")
            Bitmap bitmap = (Bitmap) extras.getParcelable("data");
            return bitmap;
>>>>>>> Stashed changes
        }

    }

}
