package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText inputEmail, inputPass;
    private TextView txtSignIn;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener fbAuthStateListener;
    private ProgressBar progressBar2;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        fbAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imageView);

        progressBar2 = findViewById(R.id.progressBar2);

        fbAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = fbAuth.getCurrentUser();
                if(fbUser != null){
                    Intent i = new Intent(LoginActivity.this, ActivityLoader.class);
                    startActivity(i);
                    finish();
                }
                else{
                    //Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        };

        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageView.setVisibility(View.GONE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String pass = inputPass.getText().toString();

                if(email.isEmpty()) {
                    inputEmail.setError("Please enter email");
                    inputEmail.requestFocus();
                }
                else if(pass.isEmpty()) {
                    inputPass.setError("Please enter password");
                    inputPass.requestFocus();
                }
                else if(!(email.isEmpty() && pass.isEmpty())){
                    progressBar2.setVisibility(View.VISIBLE);
                    fbAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Your username/password was incorrect.", Toast.LENGTH_LONG).show();
                                progressBar2.setVisibility(View.GONE);
                            }else{
                                progressBar2.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivity.this, ActivityLoader.class));
                                finish();
                            }
                        }
                    });
                }

                //fbAuth.signInWithEmailAndPassword(email, pass);
            }
        });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registeractivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registeractivity);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        fbAuth.addAuthStateListener(fbAuthStateListener);
    }

}
