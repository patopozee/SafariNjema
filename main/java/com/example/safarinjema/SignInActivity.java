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
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignInActivity extends AppCompatActivity {
    private EditText e1_email, e2_password;
    FirebaseAuth auth;
    ProgressDialog dialog;
    private Button Button;
    TextView createAccount, passwordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        View view = getLayoutInflater().inflate(R.layout.abs_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0000FF"));
        actionBar.setBackgroundDrawable(colorDrawable);


        checkConnection();

        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Log In");

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title
        e1_email = findViewById(R.id.editTextTextPersonName);
        e2_password = findViewById(R.id.EditTextTextPassword);
        passwordReset= findViewById(R.id.Reset);
        createAccount = findViewById(R.id.Create);
        Button = (Button) findViewById(R.id.button);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        e1_email.addTextChangedListener(LoginTextWatcher);
        e2_password.addTextChangedListener(LoginTextWatcher);
    }


    private TextWatcher LoginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = e1_email.getText().toString().trim();
            String password = e2_password.getText().toString().trim();
            if(email.equals("")|| password.equals("")){
                Button.setEnabled(false);
            } else {
                Button.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void signUp(View v){
        Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(i);
    }
    public void Reset(View v){
        Intent i = new Intent(SignInActivity.this,PasswordReset.class);
        startActivity(i);
    }

    public void signInUser(View v) {
        dialog.setMessage("Logging in please wait ...");
        dialog.show();
        auth.signInWithEmailAndPassword(e1_email.getText().toString(), e2_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.hide();
                            Intent i = new Intent(SignInActivity.this, MainPageActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            dialog.hide();
                            Toast.makeText(getApplicationContext(), "User not Found", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


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