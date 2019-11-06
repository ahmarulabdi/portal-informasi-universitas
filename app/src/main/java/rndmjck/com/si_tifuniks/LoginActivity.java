package rndmjck.com.si_tifuniks;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.TransitionOptions;

import java.util.concurrent.TimeoutException;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.response.LoginResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class LoginActivity extends AppCompatActivity {
    
    @BindView(R.id.edit_text_nip_administrator)
    TextInputEditText textInputEditTextNipAdministrator;
    @BindView(R.id.edit_text_password)
    TextInputEditText textInputEditTextPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(getApplicationContext());
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String nipNim = textInputEditTextNipAdministrator.getText().toString();
        String password = textInputEditTextPassword.getText().toString();
        apiInterface.login(nipNim,password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        sessionManager.createLoginSession(response.body().getData());
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Koneksi gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
