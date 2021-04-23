package com.example.safarinjema;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            Intent i = new Intent(MainActivity.this, MainPageActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            setContentView(R.layout.activity_main);
        }


    }
    public void open_signIn(View v){
        Intent i = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(i);
        finish();
    }
    public void open_signUp(View v){
        Intent i = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(i);
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