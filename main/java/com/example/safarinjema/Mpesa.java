package com.example.safarinjema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Mpesa extends AppCompatActivity {

    @BindView(R.id.number) EditText mNumber;
    @BindView(R.id.amount) EditText mAmount;
    @BindView(R.id.button) Button mButton;
    @BindView(R.id.progressBar) ProgressBar mLoad;
    private Daraja daraja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        View view = getLayoutInflater().inflate(R.layout.abs_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Mpesa");

        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false); //hide the default title


        ButterKnife.bind(this);
        daraja = Daraja.with("bU7iUBR65ADwH8KZUzEWczGQeGw8lHsT", "9i4HvzzaDiKoLxlc", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(Mpesa.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(Mpesa.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(Mpesa.this.getClass().getSimpleName(), error);
            }
        });
        mButton.setOnClickListener(v -> {
            String phonenumber = mNumber.getText().toString().trim(); //collect the number from the user's input
            String Amount = mAmount.getText().toString().trim(); // amount privided by the user

            //check validity of a number
            //check validity of a number
            if(phonenumber.isEmpty() || phonenumber.length() != 12){
                mNumber.setError("Invalid number");
                return;
            }
            //check validity of a number
            else if(Amount.isEmpty() || Amount.length() <= 0){
                mAmount.setError("Amount should be more than 0");
                return;
            }
            else if(Integer.valueOf(Amount) <= 0){
                mAmount.setError("Amount should be more than 0");
                return;
            }
            else {
                mLoad.setVisibility(View.VISIBLE);
                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline,
                        Amount,
                        phonenumber,
                        "174379",
                        phonenumber,
                        "https://ennpky09mkpv.x.pipedream.net/",
                        "demo",
                        "demo"
                );
                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.i(Mpesa.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                            }

                            @Override
                            public void onError(String error) {
                                Log.i(Mpesa.this.getClass().getSimpleName(), error);
                            }
                        }
                );
            }
        });

    }
}