package com.rhossu.selfieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 101;
    private File imageFilePath;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        Button cameraBtn = (Button) findViewById(R.id.btnSelfie);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Start front camera intent");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
                File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "selfieapp");
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }
                System.out.println("path=" + outputFile.getAbsolutePath());
                imageFilePath = new File(outputFile, "selfie_" + System.currentTimeMillis() + ".jpg");
                Uri capturedImageUri = null;
                try {
                    if (!imageFilePath.exists()) {
                        imageFilePath.createNewFile();
                    }
                    capturedImageUri = Uri.fromFile(imageFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri imageUri = Uri.fromFile(imageFilePath);
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Uri imageUri = Uri.fromFile(imageFilePath);
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(MainActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            ivPhoto.setImageURI(resultUri);
        }
    }
}
