package rndmjck.com.si_tifuniks;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Space;

import butterknife.BindView;
import butterknife.ButterKnife;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.model.Info;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button_profile)
    Button buttonProfile;
    @BindView(R.id.button_info)
    Button buttonInfo;
    @BindView(R.id.button_jadwal_kuliah)
    Button buttonJadwalKuliah;
    @BindView(R.id.button_gallery_mahasiswa)
    Button buttonGalleryMahasiswa;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        buttonJadwalKuliah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JadwalKuliahActivity.class);
                startActivity(intent);
            }
        });

        buttonGalleryMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_navigation, menu);
        MenuItem menuItemlog = menu.findItem(R.id.action_log);
        if (sessionManager.isLoggedIn()) {
            menuItemlog.setTitle("Logout");
        } else {
            menuItemlog.setTitle("Login");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (sessionManager.isLoggedIn()) {
            if (id == R.id.action_log) {
                sessionManager.logoutUser();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        } else {
            if (id == R.id.action_log) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
