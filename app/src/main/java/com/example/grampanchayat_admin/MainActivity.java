package com.example.grampanchayat_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = "MainActivity";

    //private static final String KEY_TITLE = "title";
    EditText editTextTitle;
    //TextView content;
    Button click;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;
    String result, merge, toast;


    
    //private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        //content = findViewById(R.id.content);
        click = findViewById(R.id.button2);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference d1 = fStore.collection("Notebook").document(userId);
        d1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                result = value.getString("Title");
                //content.setText(result);

                toast = editTextTitle.getText().toString().trim();

            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                final String Title = editTextTitle.getText().toString().trim() + "~";

                if(TextUtils.isEmpty(Title)) {
                    editTextTitle.setError("Title is Required.");
                }
                //progressBar.setVisibility(View.VISIBLE);

                DocumentReference documentReference = fStore.collection("Notebook").document(userId);
                Map<String, Object> user = new HashMap<>();
                merge = result + Title ;
                user.put("Title",merge);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Message saved" + userId);
                        Toast.makeText(MainActivity.this, "Your Complaint is Sent :" + toast , Toast.LENGTH_LONG).show();
                        editTextTitle.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure:" + e.toString());
                    }
                });


            }
        });


    }



    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


//    public void saveNote(View v) {
//        String title = editTextTitle.getText().toString();

       // Map<String, Object> note = new HashMap<>();
        //note.put(KEY_TITLE,title);


//        db.collection("Notebook").document("My First Note").set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG,e.toString());
//                    }
//                });

//        DocumentReference documentReference = fStore.collection("Notebook").document("Create");
//        Map<String,Object> user = new HashMap<>();
//
//        user.put("title",title);
//        documentReference.set("create").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("TAG", "onSuccess: user Profile is created for ");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG", "onFailure: " + e.toString());
//            }
//        });
    }
