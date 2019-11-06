package rndmjck.com.si_tifuniks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.Administrator.TambahGalleryActivity;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.adapter.RViewGalleryAdapter;
import rndmjck.com.si_tifuniks.model.Gallery;
import rndmjck.com.si_tifuniks.response.DataGalleryResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class GalleryActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    @BindView(R.id.recycler_view_gallery)
    RecyclerView recyclerViewGallery;
    RViewGalleryAdapter rViewGalleryAdapter;
    @BindView(R.id.swipe_refresh_layout_content_gallery)
    SwipeRefreshLayout swipeRefreshLayoutGallery;
    @BindView(R.id.fab_tambah_gallery)
    FloatingActionButton floatingActionButtonTambahGallery;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()) {
            floatingActionButtonTambahGallery.setVisibility(View.GONE);
        }

        floatingActionButtonTambahGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahGalleryActivity.class);
                startActivity(intent);
            }
        });
        getDataGallery();


        swipeRefreshLayoutGallery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataGallery();
                swipeRefreshLayoutGallery.setRefreshing(false);
            }
        });


    }

    private void getDataGallery() {
        apiInterface.dataGallery().enqueue(new Callback<DataGalleryResponse>() {
            @Override
            public void onResponse(Call<DataGalleryResponse> call, Response<DataGalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Gallery> galleries = response.body().getData();
                        rViewGalleryAdapter = new RViewGalleryAdapter(galleries, getApplicationContext());
                        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewGallery.setAdapter(rViewGalleryAdapter);

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataGalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
