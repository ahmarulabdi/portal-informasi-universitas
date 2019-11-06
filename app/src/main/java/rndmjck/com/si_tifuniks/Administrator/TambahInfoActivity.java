package rndmjck.com.si_tifuniks.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.InfoActivity;
import rndmjck.com.si_tifuniks.R;
import rndmjck.com.si_tifuniks.response.DetailInfoResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class TambahInfoActivity extends AppCompatActivity {

    @BindView(R.id.button_submit_tambah_info)
    Button buttonSubmitTambahInfo;
    @BindView(R.id.edit_text_tambah_title_info)
    TextInputEditText textInputEditTextTitle;
    @BindView(R.id.edit_text_tambah_deskripsi_gallery)
    EditText editTextDeskripsi;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        buttonSubmitTambahInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahInfo();
            }
        });

    }

    private void tambahInfo() {
        String title = textInputEditTextTitle.getText().toString();
        String deskripsi = editTextDeskripsi.getText().toString();
        apiInterface.tambahInfo(title, deskripsi).enqueue(new Callback<DetailInfoResponse>() {
            @Override
            public void onResponse(Call<DetailInfoResponse> call, Response<DetailInfoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailInfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
