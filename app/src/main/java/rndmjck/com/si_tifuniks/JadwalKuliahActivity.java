package rndmjck.com.si_tifuniks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.adapter.RViewJadwalKuliahAdapter;
import rndmjck.com.si_tifuniks.model.JadwalKuliah;
import rndmjck.com.si_tifuniks.response.DataJadwalKuliahResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class JadwalKuliahActivity extends AppCompatActivity {

    private static final String TAG = "JadwalKuliahActivity";
    @BindView(R.id.recycler_view_jadwal_kuliah_container)
    RecyclerView recyclerViewJadwalKuliahContainer;
    @BindView(R.id.swipe_refresh_layout_content_jadwal_kuliah)
    SwipeRefreshLayout swipeRefreshLayoutJadwalKuliah;
    ApiInterface apiInterface;

    RViewJadwalKuliahAdapter rViewJadwalKuliahAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kuliah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        swipeRefreshLayoutJadwalKuliah.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataJadwalKuliah();
                swipeRefreshLayoutJadwalKuliah.setRefreshing(false);
            }
        });

        getDataJadwalKuliah();
    }

    private void getDataJadwalKuliah() {
        apiInterface.dataJadwalKuliah().enqueue(new Callback<DataJadwalKuliahResponse>() {
            @Override
            public void onResponse(Call<DataJadwalKuliahResponse> call, Response<DataJadwalKuliahResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<JadwalKuliah> jadwalKuliahs = response.body().getData();
                        rViewJadwalKuliahAdapter = new RViewJadwalKuliahAdapter(jadwalKuliahs, getApplicationContext());
                        recyclerViewJadwalKuliahContainer.setAdapter(rViewJadwalKuliahAdapter);
                        recyclerViewJadwalKuliahContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataJadwalKuliahResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

//    private void getDetailJadwalKuliah() {
//        apiInterface.detailJadwalKuliah("2").enqueue(new Callback<DetailJadwalKuliahResponse>() {
//            @Override
//            public void onResponse(Call<DetailJadwalKuliahResponse> call, Response<DetailJadwalKuliahResponse> response) {
//                if (response.isSuccessful()){
//                    if (response.body().getStatus()){
//                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DetailJadwalKuliahResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),"koneksi gagal",Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure: tes"+t);
//            }
//        });
//    }

}
