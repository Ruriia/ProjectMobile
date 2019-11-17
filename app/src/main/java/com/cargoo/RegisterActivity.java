package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputCompanyName, inputEmail, inputPassword, inputConfirmPass, inputPhone, inputAddress, inputCity, inputDistrict, inputZip;
    private Spinner spinProvince;
    private Button btnRegister;
    private CheckBox checkBox;
    private FirebaseAuth fbAuth;
    private TextView txtSignIn;
    private ProgressBar progressBar;

    DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fbAuth = FirebaseAuth.getInstance();

        txtSignIn = findViewById(R.id.txtSignIn);

        inputCompanyName = findViewById(R.id.inputCompanyName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPass = findViewById(R.id.inputConfirmPass);
        inputPhone = findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputDistrict = findViewById(R.id.inputDistrict);
        inputZip = findViewById(R.id.inputZip);
        spinProvince = findViewById(R.id.spinProvincePegirim);
        checkBox = findViewById(R.id.checkBox);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setEnabled(false);

        progressBar = findViewById(R.id.progressBar);

        dbUsers = FirebaseDatabase.getInstance().getReference().child("Users"); // Create 'Users' document

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                btnRegister.setEnabled(true);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyName = inputCompanyName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPass = inputConfirmPass.getText().toString();
                String phone = inputPhone.getText().toString();
                String address = inputAddress.getText().toString();
                int zip = Integer.parseInt(inputZip.getText().toString());
                String province = spinProvince.getSelectedItem().toString();
                String city = inputCity.getText().toString();
                String district = inputDistrict.getText().toString();

                // ------------------------------------ FIREBASE AUTHENTICATION -------------------------------
                if(email.isEmpty()) {
                    inputEmail.setError("Please enter email");
                    inputEmail.requestFocus();
                }
                else if(password.isEmpty()) {
                    inputPassword.setError("Please enter password");
                    inputPassword.requestFocus();
                }
                else if(!(email.isEmpty() && password.isEmpty())){
                    if(companyName.isEmpty()) {
                        inputCompanyName.setError("Please enter the company name");
                        inputCompanyName.requestFocus();
                    }
                    else if(phone.isEmpty()) {
                        inputPhone.setError("Please enter your phone number");
                        inputPhone.requestFocus();
                    }
                    else if(address.isEmpty()) {
                        inputAddress.setError("Please enter your address line");
                        inputAddress.requestFocus();
                    }
                    else if(city.isEmpty()) {
                        inputCity.setError("Please enter your city");
                        inputCity.requestFocus();
                    }
                    else if(district.isEmpty()) {
                        inputDistrict.setError("Please enter your district");
                        inputDistrict.requestFocus();
                    }
                    else if(zip == 0) {
                        inputZip.setError("Please enter your zip code");
                        inputZip.requestFocus();
                    }
                    else if(!password.equals(confirmPass)){
                        inputConfirmPass.setError("Please input the same password");
                        inputConfirmPass.requestFocus();
                    }
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Sorry, the registration has failed. Please try again", Toast.LENGTH_LONG).show();
                                }else{
                                    fbAuth.getInstance().signOut();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                        });

                        // ---------------------- INPUT COMPANY DATA TO FIREBASE REALTIME DATABASE ---------------------------
                        String userID = dbUsers.push().getKey();

                        Users userdata1 = new Users(userID, companyName, phone, email, password);
                        Users userdata2 = new Users(address, province, city, district, zip); // For address

                        dbUsers.child(userID).setValue(userdata1);
                        dbUsers.child(userID).child("address").setValue(userdata2);
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error Occured.", Toast.LENGTH_LONG).show();
                }
                // ------------------------------------ END OF FIREBASE AUTHENTICATION -------------------------------


            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
}
