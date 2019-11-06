package rndmjck.com.si_tifuniks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.Administrator.TambahInfoActivity;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.adapter.RViewInfoAdapter;
import rndmjck.com.si_tifuniks.model.Info;
import rndmjck.com.si_tifuniks.response.DataInfoResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";
    @BindView(R.id.fab_tambah_info)
    FloatingActionButton fabTambahInfo;
    @BindView(R.id.swipe_resfresh_layout_content_info)
    SwipeRefreshLayout swipeRefreshLayoutContentInfo;
    @BindView(R.id.recycler_view_info)
    RecyclerView recyclerViewInfo;
    RViewInfoAdapter rViewInfoAdapter;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(getApplicationContext());

        swipeRefreshLayoutContentInfo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataInfo();
                swipeRefreshLayoutContentInfo.setRefreshing(false);
            }
        });


        getDataInfo();


        if (!sessionManager.isLoggedIn()) {
            fabTambahInfo.setVisibility(View.GONE);
        }

        fabTambahInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataInfo() {
        apiInterface.dataInfo().enqueue(new Callback<DataInfoResponse>() {
            @Override
            public void onResponse(Call<DataInfoResponse> call, Response<DataInfoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Info> infos = response.body().getData();
                        rViewInfoAdapter = new RViewInfoAdapter(infos, getApplicationContext());
                        recyclerViewInfo.setAdapter(rViewInfoAdapter);
                        recyclerViewInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataInfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

}
