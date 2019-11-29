package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

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

import java.io.File;
import java.io.IOException;

public class AccountActivity extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextView btnlogout;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();;
    private FirebaseUser fbUser = fbAuth.getInstance().getCurrentUser();
    private String fbUserId = fbUser.getUid();
    private String fbUserEmail = fbUser.getEmail();
    private DatabaseReference dbUsers;
    private StorageReference storageReference;

    private TextView txtHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_account, container, false);
        final TextView company = v.findViewById(R.id.txtCompanyName);
        final ImageView profile = v.findViewById(R.id.profile_image);
        dbUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        Query q1 = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userID")
                .equalTo(fbUserId);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                company.setText(dataSnapshot.child(fbUserId).child("name").getValue(String.class));
                try {
                    final File file = File.createTempFile("image","jpg");
                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cargoo-d9aa1.appspot.com/").child(String.valueOf(dataSnapshot.child(fbUserId).child("profileimage").getValue(String.class)));
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            profile.setImageBitmap(bitmap);
                        }
                    });
                }catch (IOException e){
                    Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                
            }
        });
        final TextView accountsetting = v.findViewById(R.id.btnAccountSetting);
        accountsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AccountSetting.class);
                startActivity(i);
            }
        });

        btnlogout = v.findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Keluar?");
                dialog.setMessage("Anda yakin?");
                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();

                    }
                });
                dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });

        txtHistory = v.findViewById(R.id.txtHistory);
        txtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });

        return v;
    }
}
