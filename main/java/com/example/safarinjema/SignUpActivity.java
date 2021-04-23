package com.example.safarinjema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    ".{8,}");
    private EditText e5_phone, e3_firstName, e4_lastName, e6_email, e7_password, e10_city;

    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private FirebaseDatabase root;
    private DatabaseReference databaseReference;
    private CountryCodePicker cpp;
    private Button Button;
    private Switch mBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkConnection();
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
        Title.setText("Sign Up");
        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title
        mBox = (Switch) findViewById(R.id.checkBox1);
        initializeUI();
        auth = FirebaseAuth.getInstance();

        TextView checkbox = (TextView) findViewById(R.id.text);

        checkbox.setText(Html.fromHtml("I agree to the <a href='http://www.redbus.in/mob/mTerms.aspx' > Terms and Conditions</a>"));
        mBox.setMovementMethod(LinkMovementMethod.getInstance());
        Button = (Button) findViewById(R.id.button2);




        dialog = new ProgressDialog(this);


        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();


            }
        });

        mBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBox.isChecked()) {
                    Button.setEnabled(true);
                } else {
                    Button.setEnabled(false);
                }

            }
        });


    }

    private void initializeUI() {
        e5_phone = (EditText) findViewById(R.id.editTextPhone);
        cpp = (CountryCodePicker) findViewById(R.id.ccp);
        cpp.registerCarrierNumberEditText(e5_phone);
        e3_firstName = (EditText)findViewById(R.id.editTextTextPersonName3);
        e4_lastName = (EditText) findViewById(R.id.editTextTextPersonName4);
        e6_email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        e7_password = (EditText) findViewById(R.id.editTextTextPassword);
        e10_city = (EditText) findViewById(R.id.editTextTextPersonName6);


    }

    private void registerNewUser() {
        String phone = cpp.getFullNumberWithPlus();
        String firstName = e3_firstName.getText().toString().trim();
        String lastName = e4_lastName.getText().toString().trim();
        String email = e6_email.getText().toString().trim();
        String password = e7_password.getText().toString().trim();
        String city = e10_city.getText().toString().trim();


        if (TextUtils.isEmpty(phone)) {
            e5_phone.setError("Phone number is Required.");
            return;
        } else if (phone.length() < 6 || phone.length()>17) {
            e5_phone.setError("Please enter a valid phone number");
            return;
        } else if (TextUtils.isEmpty(firstName)) {
            e3_firstName.setError("This field is required");
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            e4_lastName.setError("This field is required");
            return;
        } else if (TextUtils.isEmpty(email)) {
            e6_email.setError("Email is Required.");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e6_email.setError("Please enter a valid email");
            return;
        } else if (TextUtils.isEmpty(password)) {
            e7_password.setError("Password is Required.");
            return;
        } else if (password.length() < 8) {
            e7_password.setError("Password too short");
            return;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            e7_password.setError("Password too weak");
            return;
        } else if (TextUtils.isEmpty(city)) {
            e10_city.setError("This field is required");
            return;

        }
        else if ( phone.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || city.equals(""))
        {
            Button.setEnabled(false);
        }
        else {
            Button.setEnabled(true);
            dialog.setMessage("Registering please wait ...");
            dialog.show();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.hide();
                                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                                root = FirebaseDatabase.getInstance();
                                databaseReference = root.getReference("Users");
                                String phone = cpp.getFullNumberWithPlus();
                                String firstName = e3_firstName.getText().toString().trim();
                                String lastName = e4_lastName.getText().toString().trim();
                                String email = e6_email.getText().toString().trim();
                                String password = e7_password.getText().toString().trim();
                                String city = e10_city.getText().toString().trim();


                                Users User = new Users(phone, firstName, lastName, email, password, city);
                                FirebaseUser firebaseUser = auth.getCurrentUser();


                                databaseReference.child(firebaseUser.getUid()).setValue(User)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Intent i = new Intent(SignUpActivity.this,MainPageActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });



                            } else {
                                dialog.hide();
                                Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }
    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null!=activeNetwork){
            if (activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){

            }
            if (activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){

            }

        }
        else {
            Toast.makeText(getApplicationContext(),"Please enable your internet connection...",Toast.LENGTH_SHORT).show();
        }
    }
}