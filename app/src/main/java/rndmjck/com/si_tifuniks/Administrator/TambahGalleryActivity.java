package rndmjck.com.si_tifuniks.Administrator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.TransitionOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.GalleryActivity;
import rndmjck.com.si_tifuniks.R;
import rndmjck.com.si_tifuniks.response.DataGalleryResponse;
import rndmjck.com.si_tifuniks.response.DetailGalleryResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class TambahGalleryActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "TambahGalleryActivity";
    private final Integer REQUEST_GALLERY = 101;
    @BindView(R.id.image_button_get_image)
    ImageButton imageButtonGetImage;
    @BindView(R.id.image_view_tambah_gambar_gallery)
    ImageView imageViewTambahGambarGallery;
    @BindView(R.id.edit_text_tambah_title_gallery)
    EditText editTextTambahTitleGallery;
    @BindView(R.id.edit_text_tambah_deskripsi_gallery)
    EditText editTextTambahDeskripsiGallery;
    @BindView(R.id.button_submit_tambah_gallery)
    Button buttonSubmitTambahGallery;
    Bitmap bitmap;
    private Uri uri;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        imageButtonGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukagallery();
            }
        });


        buttonSubmitTambahGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahGallery();
            }
        });


    }

    private void tambahGallery() {
        Log.d(TAG, "tambahGallery: ");
        String imageBase64 = imageToString();
        String imageTitle = editTextTambahTitleGallery.getText().toString();
        String title = editTextTambahTitleGallery.getText().toString();
        String deskripsi = editTextTambahDeskripsiGallery.getText().toString();

        apiInterface.tambahGallery(imageTitle,imageBase64, title, deskripsi).enqueue(new Callback<DetailGalleryResponse>() {
            @Override
            public void onResponse(Call<DetailGalleryResponse> call, Response<DetailGalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.d(TAG, "onResponse: tambah gallery sukses");
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailGalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bukagallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "open gallery"), REQUEST_GALLERY);
        Log.d(TAG, "bukagallery: tes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                uri = data.getData();
                if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(), "gambar muncul", Toast.LENGTH_SHORT).show();
                    try {
                        imageViewTambahGambarGallery.setVisibility(View.VISIBLE);
                        buttonSubmitTambahGallery.setVisibility(View.VISIBLE);
                        buttonSubmitTambahGallery.setEnabled(true);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageViewTambahGambarGallery.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            REQUEST_GALLERY, galleryPermissions);
                }
            }
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "akses gambar gallery diterima");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "akses gambar gallery ditolak");
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);

        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}
