package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AccountSetting extends AppCompatActivity {
    private static final int image_id = 1;
    Uri selectedImage;
    private String temp;

    private EditText edtNamaPerusahaan, edtTelpPerusahaan, edtEmailPerusahaan, edtCurrPassword, edtNewPassword, edtAlamatPerusahaan,
            edtProvinsiPerusahaan, edtKotaPerusahaan, edtKecamatanPerusahaan, edtKodePosPerusahaan;
    private ImageView profileimage;
    private StorageReference storageReference;

    private Button btnSave, btnCancel;

    private DatabaseReference dbUsers;
    
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser = fbAuth.getInstance().getCurrentUser();
    private String fbUserId = fbUser.getUid();
    private String fbUserEmail = fbUser.getEmail();

    private Button btnBack;

    // Initiate Form Auto-Fill
    private String refNamaPerusahaan, refTelpPerusahaan, refEmailPerusahaan, refAlamatPerusahaan, refProvinsiPerusahaan, refKotaPerusahaan, 
            refKecamatanPerusahaan, refKodePosPerusahaan, refProfilePicture;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_id && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            profileimage.setImageURI(selectedImage);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(uri));
    }

    private String uploadImage(){
        if(selectedImage != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference mStorageRef = firebaseStorage.getReference();;
            String path = "profileimage/" + UUID.randomUUID().toString() + "." + getExtension(selectedImage);
            StorageReference ref = mStorageRef.child(path);
            ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            return path;
        }else {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
//        previewimage = findViewById(R.id.profile_image);
//        Button editfoto = findViewById(R.id.btneditfoto);
//        editfoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galery, image_id);
//            }
//        });

        edtNamaPerusahaan = findViewById(R.id.edtNamaPerusahaan);
        edtTelpPerusahaan = findViewById(R.id.edtTelpPerusahaan);
        edtEmailPerusahaan = findViewById(R.id.edtEmailPerusahaan);

        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtCurrPassword = findViewById(R.id.edtCurrPassword);
        
        edtAlamatPerusahaan = findViewById(R.id.edtAlamatPerusahaan);
        edtProvinsiPerusahaan = findViewById(R.id.edtProvinsiPerusahaan);
        edtKotaPerusahaan = findViewById(R.id.edtKotaPerusahaan);
        edtKecamatanPerusahaan = findViewById(R.id.edtKecamatanPerusahaan);
        edtKodePosPerusahaan = findViewById(R.id.edtKodePosPerusahaan);
        profileimage = findViewById(R.id.profile_image);

        final ProgressBar pbAccSetting = findViewById(R.id.pbAccSetting);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fbAuth = FirebaseAuth.getInstance();

        // READ USER'S INFO
        dbUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        Query q1 = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userID")
                .equalTo(fbUserId);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refProfilePicture = String.valueOf(dataSnapshot.child(fbUserId).child("profileimage").getValue(String.class));
                refNamaPerusahaan = dataSnapshot.child(fbUserId).child("name").getValue(String.class);
                refTelpPerusahaan = dataSnapshot.child(fbUserId).child("phone").getValue(String.class);
                refEmailPerusahaan = dataSnapshot.child(fbUserId).child("email").getValue(String.class);
                refAlamatPerusahaan = dataSnapshot.child(fbUserId).child("address").child("address").getValue(String.class);
                refProvinsiPerusahaan = dataSnapshot.child(fbUserId).child("address").child("province").getValue(String.class);
                refKotaPerusahaan = dataSnapshot.child(fbUserId).child("address").child("city").getValue(String.class);
                refKecamatanPerusahaan = dataSnapshot.child(fbUserId).child("address").child("district").getValue(String.class);
                refKodePosPerusahaan = String.valueOf(dataSnapshot.child(fbUserId).child("address").child("zipcode").getValue(Integer.class));

                edtNamaPerusahaan.setText(refNamaPerusahaan);
                edtEmailPerusahaan.setText(refEmailPerusahaan);
                edtTelpPerusahaan.setText(refTelpPerusahaan);
                edtAlamatPerusahaan.setText(refAlamatPerusahaan);
                edtProvinsiPerusahaan.setText(refProvinsiPerusahaan);
                edtKotaPerusahaan.setText(refKotaPerusahaan);
                edtKecamatanPerusahaan.setText(refKecamatanPerusahaan);
                edtKodePosPerusahaan.setText(refKodePosPerusahaan);

                try {
                    final File file = File.createTempFile("image","jpg");
                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cargoo-d9aa1.appspot.com/").child(refProfilePicture);
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            profileimage.setImageBitmap(bitmap);
                        }
                    });
                }catch (IOException e){
                    Toast.makeText(AccountSetting.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Button changeprofile = findViewById(R.id.btnChangeProfile);
        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galery, image_id);
            }
        });


        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbAccSetting.setVisibility(View.VISIBLE);
                final String companyName = edtNamaPerusahaan.getText().toString();
                final String email = edtEmailPerusahaan.getText().toString();
                final String phone = edtTelpPerusahaan.getText().toString();
                final String address = edtAlamatPerusahaan.getText().toString();
                final int zip = Integer.parseInt(edtKodePosPerusahaan.getText().toString());
                final String province = edtProvinsiPerusahaan.getText().toString();
                final String city = edtKotaPerusahaan.getText().toString();
                final String district = edtKecamatanPerusahaan.getText().toString();
                refProfilePicture = uploadImage();

                final String newPass = edtNewPassword.getText().toString();
                final String currPass = edtCurrPassword.getText().toString();

                Users userdata1 = new Users(fbUserId, companyName, phone, email, refProfilePicture);
                Users userdata2 = new Users(address, province, city, district, zip); // For address

                dbUsers.child(fbUserId).setValue(userdata1);
                dbUsers.child(fbUserId).child("address").setValue(userdata2);

                dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!newPass.isEmpty()){
                            if(!currPass.isEmpty()){
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(fbUserEmail, currPass);

                                fbUser.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    fbUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("UpdatePassword", "Password updated");

                                                                Toast.makeText(AccountSetting.this, "Profile update succeed.", Toast.LENGTH_LONG).show();

                                                                fbAuth.getInstance().signOut();
                                                                Intent i = new Intent(AccountSetting.this, LoginActivity.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(i);
                                                                finish();

                                                            } else {
                                                                Log.d("UpdatePassword", "Error password not updated");
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    edtCurrPassword.setError("Your old password is invalid");
                                                    edtCurrPassword.requestFocus();
                                                    Log.d("UpdatePassword", "Error auth failed");
                                                    pbAccSetting.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }else{
                                edtCurrPassword.setError("Your old password is invalid");
                                edtCurrPassword.requestFocus();
                                pbAccSetting.setVisibility(View.GONE);
                            }
                        }else{
                            pbAccSetting.setVisibility(View.GONE);
                            Toast.makeText(AccountSetting.this, "Profile update succeed.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AccountSetting.this, "Update failed.", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == image_id && resultCode == RESULT_OK && data != null) {
//            selectedImage = data.getData();
//            previewimage.setImageURI(selectedImage);
//        }
//    }

}
