package com.example.safarinjema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordReset extends AppCompatActivity {
    TextView textView;
    private Button mButton;
    private EditText e1_email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
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
        Title.setText("Reset password");

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title

        auth = FirebaseAuth.getInstance();

        mButton = (Button) findViewById(R.id.button);
        e1_email = (EditText) findViewById(R.id.editTextTextPersonName);
        e1_email.addTextChangedListener(LoginTextWatcher);
        textView = findViewById(R.id.editText);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword();
            }
        });
    }
    private void resetPassword () {
        String emailInput = e1_email.getText().toString().trim();
        if (TextUtils.isEmpty(emailInput)) {
            e1_email.setError("Email is Required.");
            return;
        } if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            e1_email.setError("Enter a valid email");
            return;
        }
        else {
            auth.sendPasswordResetEmail(emailInput)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Please check your email inbox if you need to reset your password", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PasswordReset.this,SignInActivity.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
    private TextWatcher LoginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = e1_email.getText().toString().trim();
            if(email.equals("")){
                mButton.setEnabled(false);
            } else {
                mButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}