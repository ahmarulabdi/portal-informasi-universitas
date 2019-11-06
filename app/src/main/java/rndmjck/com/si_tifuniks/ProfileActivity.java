package rndmjck.com.si_tifuniks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.response.DetailInfoResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    @BindView(R.id.textview_paragraf_profile)
    TextView textViewParagrafProfile;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getProfile();

    }

    private void getProfile() {
        apiInterface.profile().enqueue(new Callback<DetailInfoResponse>() {
            @Override
            public void onResponse(Call<DetailInfoResponse> call, Response<DetailInfoResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        textViewParagrafProfile.setText(Html.fromHtml(response.body().getData().getDeskripsi()));
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailInfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"koneksi gagal",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }

}
