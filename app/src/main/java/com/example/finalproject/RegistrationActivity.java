package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class RegistrationActivity extends BaseActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GET_IMAGE_REQUEST_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    ImageView capturedImageView;
    Bitmap capturedImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        capturedImageView = findViewById(R.id.capturedImage);
        capturedImageView.setOnClickListener(this::showOptionsDialog);
        findViewById(R.id.toLogin).setOnClickListener(this::toLogin);
        findViewById(R.id.cameraNextBtn).setOnClickListener(this::registerUser);
    }

    public void showOptionsDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");

        builder.setCancelable(true);
        builder.setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermissionAndOpenCamera();
            }
        });

        builder.setNegativeButton("Choose From Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseImageFromGallery();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImageBitmap = (Bitmap) extras.get("data");
            capturedImageView.setImageBitmap(capturedImageBitmap);
        } else if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    capturedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    capturedImageView.setImageBitmap(capturedImageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void registerUser(View view) {

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText carModelInput = findViewById(R.id.carModel);

        try {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String email = emailInput.getText().toString();
            String carModel = carModelInput.getText().toString();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || carModel.isEmpty() || capturedImageBitmap == null) {
                throw new IllegalArgumentException("Please fill in all fields and take a picture.");
            }

            if (isUserRegistered(username)) {
                throw new IllegalArgumentException("Username already exists. Please choose another username.");
            }

            if (!isEmailValid(email)) {
                throw new IllegalArgumentException("Please enter a valid email address.");
            }

            if (!isPasswordValid(password)) {
                throw new IllegalArgumentException("Password must be at least 8 characters long.");
            }

            String base64Image = encodeBitmapToBase64(capturedImageBitmap);
            User newUser = new User(username, password, email, carModel, base64Image);
//            Toast.makeText(this, "User created: " + newUser.getUsername(), Toast.LENGTH_SHORT).show();
            addUserToDictPref(newUser);

            MyApplication myApplication = (MyApplication) getApplicationContext();
            myApplication.setCurrentUser(newUser);

            Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isUserRegistered(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userDictJson = sharedPreferences.getString("user_dict_json", "{}");
        Gson gson = new Gson();
        Map<String, User> userDict = gson.fromJson(userDictJson, new TypeToken<Map<String, User>>() {
        }.getType());
        if (userDict == null) {
            return false;
        }
        return userDict.containsKey(username);
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void chooseImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {

            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhotoIntent, 102);
        }
    }


}