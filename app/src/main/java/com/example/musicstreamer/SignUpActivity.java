package com.example.musicstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText username, email, password, cnfPassword;
    private Button signupButton;
    private TextView loginScreen;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.usernameET);
        email = findViewById(R.id.emailETSignup);
        password = findViewById(R.id.passwordETSignUp);
        cnfPassword = findViewById(R.id.cnfPasswordET);
        signupButton = findViewById(R.id.signUpButton);
        loginScreen = findViewById(R.id.loginRedirect);
        progressBar = findViewById(R.id.progressBarSignUp);
    }

    public void signUpFunction(View view){
        String emailText = email.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String cnfPasswordText = cnfPassword.getText().toString();

        if(emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty() || cnfPasswordText.isEmpty()){
            Snackbar.make(findViewById(R.id.cLayoutMain), "Can't leave fields empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(!passwordText.equals(cnfPasswordText)){
            Snackbar.make(findViewById(R.id.cLayoutMain), "Passwords do not match", Snackbar.LENGTH_SHORT).show();
            return;
        }
        signUpUser(usernameText, emailText, passwordText);
    }

    private void signUpUser(String usernameText, String emailText, String passwordText) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(Config.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

        UserInterface ui = retrofit.create(UserInterface.class);
        Model1 loginModel = new Model1(usernameText,emailText, passwordText);
        Call<Model1> call = ui.createUser(loginModel);
        call.enqueue(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                if(response.isSuccessful()){
                    Model1 expModel = response.body();
                    Toast.makeText(getApplicationContext(), expModel.getToken(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void loginRedirect(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
