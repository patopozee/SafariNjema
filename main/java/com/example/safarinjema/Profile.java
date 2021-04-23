package com.example.safarinjema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {
    private EditText firstName, lastName, email, phone;
    private ImageView imageView;
    TextView changePassword;

    Button save;
    FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private FirebaseUser user;
    private static final String USERS = "User";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        View view = getLayoutInflater().inflate(R.layout.abs_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0000FF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Profile");
        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title


        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        imageView = findViewById(R.id.imageView);
        changePassword = findViewById(R.id.ChangePassword);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();



        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent i = new Intent(Profile.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference reference = db.getReference("Users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String first = (String) snapshot.child("firstName").getValue();
                    String last = (String) snapshot.child("lastName").getValue();
                    String mail = (String) snapshot.child("email").getValue();
                    String no = (String) snapshot.child("phone").getValue();
                    firstName.setText(first);
                    lastName.setText(last);
                    email.setText(mail);
                    phone.setText(no);


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(Profile.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

}


