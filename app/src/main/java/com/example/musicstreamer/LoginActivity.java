package com.example.musicstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicstreamer.POJO.Model1;
import com.example.musicstreamer.RouteHandlers.UserInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//primary color: #5595eb
//seconday color: #3f78ef
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpPage;
    private TextInputEditText email, password;
    private ProgressBar progressBar;

    private String TAG = "RESPONSE";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        signUpPage = findViewById(R.id.signUpTV);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        progressBar = findViewById(R.id.progressBarLoginScreen);
    }


    public void loginFunction(View view){
        String emailText = email.getText().toString(),
                passwordText = password.getText().toString();
        if(emailText.equals("") || passwordText.equals("")){
            Snackbar.make(findViewById(R.id.mainView), "Cannot leave fields empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        loginUser(emailText, passwordText);
    }

    private void loginUser(String emailText, String passwordText) {
        progressBar.setVisibility(View.VISIBLE);

        //contact REST API
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Config.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

        UserInterface ui = retrofit.create(UserInterface.class);
        Model1 loginModel = new Model1(emailText, passwordText);
        Call<Model1> call = ui.loginUser(loginModel);
        call.enqueue(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if(response.isSuccessful()){
                    Model1 expModel = response.body();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", expModel.getToken());
                    editor.apply();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e("ERROR", t.getMessage().toString());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);

        if(sharedPreferences.getString("token", null) != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void signUpPage(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}
