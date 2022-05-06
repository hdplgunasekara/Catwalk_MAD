package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import java.util.HashMap;

public class ModelRegister extends AppCompatActivity {

    EditText name,password,email,mobile,birthday;
    Button reg_btn,upload_btn;
    DatabaseReference modelDbref;
    String str_status="Pending";
    ImageView image;
    //vars
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Models");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    String str_name,str_password,str_email,str_mobile,str_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_register);

        name = findViewById(R.id.mname);
        password = findViewById(R.id.mpassword);
        email = findViewById(R.id.memail);
        mobile = findViewById(R.id.mmobile);
        birthday = findViewById(R.id.mdate);
        reg_btn = findViewById(R.id.mregister);
        upload_btn=findViewById(R.id.selectmodelpicbtn1);
        image = findViewById(R.id.imagemodelup);

         modelDbref = FirebaseDatabase.getInstance().getReference().child("Models");

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendDataToDb(imageUri);
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            image.setImageURI(imageUri);

        }
    }



    private void sendDataToDb(Uri uri){
//        final ProgressDialog mDialog = new ProgressDialog(ModelRegister.this);
//        mDialog.setMessage("Please wait....");
//        mDialog.show();
//
//
//
        str_name = name.getText().toString();
        str_password = password.getText().toString();
        str_email = email.getText().toString();
        str_mobile = mobile.getText().toString();
        str_birthday = birthday.getText().toString();
//
//
//
//        modelDbref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //check if already available
//                if(snapshot.child(str_email).exists()){
//                    mDialog.dismiss();
//                    Toast.makeText(ModelRegister.this, "Already Available...", Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//                    mDialog.dismiss();
//                    Models model  = new Models(str_name,str_password,str_email,str_mobile,str_birthday,str_status);
//                    System.out.println(str_status);
//                    modelDbref.child(str_email).setValue(model);
//                    Toast.makeText(ModelRegister.this, "Sign up Successfully!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
////////////////////////////////////////////////////////////////////////////////////////////////////

//      Models model  = new Models(str_name,str_password,str_email,str_mobile,str_birthday);
//      modelDbref.push().setValue(model);
//
//        Toast.makeText(ModelRegister.this, "Model Registered", Toast.LENGTH_SHORT).show();

        ////////////////////////////////////////////////////////////////////////////////////////////////////

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Models model = new Models(str_name,str_password,str_email,str_mobile,str_birthday,str_status,uri.toString());
//                        String modelId = root.push().getKey();
                        root.child(str_email).setValue(model);

                        Toast.makeText(ModelRegister.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ModelRegister.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void checkMail(View v){

    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}