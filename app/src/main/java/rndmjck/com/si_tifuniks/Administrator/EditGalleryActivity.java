package rndmjck.com.si_tifuniks.Administrator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.GalleryActivity;
import rndmjck.com.si_tifuniks.R;
import rndmjck.com.si_tifuniks.config.ServerConfig;
import rndmjck.com.si_tifuniks.model.Gallery;
import rndmjck.com.si_tifuniks.response.DetailGalleryResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

public class EditGalleryActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private static final String TAG = "TambahGalleryActivity";
    private final Integer REQUEST_GALLERY = 102;
    @BindView(R.id.image_button_edit_get_image)
    ImageButton imageButtonEditGetGallery;
    @BindView(R.id.image_view_edit_gambar_gallery)
    ImageView imageViewEditGambarGallery;
    @BindView(R.id.edit_text_edit_title_gallery)
    EditText editTextEditTitleGallery;
    @BindView(R.id.edit_text_edit_deskripsi_gallery)
    EditText editTextEditDeskripsiGallery;
    @BindView(R.id.button_submit_edit_gallery)
    Button buttonSubmitEditGallery;

    ApiInterface apiInterface;
    private Uri uri;
    Bitmap bitmap;
    private String idGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (getIntent().hasExtra("id_gallery")) {
            idGallery = getIntent().getStringExtra("id_gallery");
            getDetailGallery();
        }


        imageButtonEditGetGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukagallery();
            }
        });

        buttonSubmitEditGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGallery();

            }
        });
    }

    private void editGallery() {
        String imageBase64 = imageToString();
        String imageTitle = editTextEditTitleGallery.getText().toString();
        String title = editTextEditTitleGallery.getText().toString();
        String deskripsi = editTextEditDeskripsiGallery.getText().toString();



        apiInterface.updateGallery(idGallery, imageTitle, imageBase64, title, deskripsi).enqueue(new Callback<DetailGalleryResponse>() {
            @Override
            public void onResponse(Call<DetailGalleryResponse> call, Response<DetailGalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailGalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"koneksi gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailGallery() {
        apiInterface.detailGallery(idGallery).enqueue(new Callback<DetailGalleryResponse>() {
            @Override
            public void onResponse(Call<DetailGalleryResponse> call, Response<DetailGalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Gallery gallery = response.body().getData();
                        imageViewEditGambarGallery.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(ServerConfig.IMAGE_FOLDER_GALLERY + gallery.getGambar())
                                .into(imageViewEditGambarGallery);

                        editTextEditTitleGallery.setText(gallery.getTitle());
                        editTextEditDeskripsiGallery.setText(gallery.getDeskripsi());
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailGalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
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
                        imageViewEditGambarGallery.setVisibility(View.VISIBLE);
                        buttonSubmitEditGallery.setVisibility(View.VISIBLE);
                        buttonSubmitEditGallery.setEnabled(true);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageViewEditGambarGallery.setImageBitmap(bitmap);
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


    private void bukagallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "open gallery"), REQUEST_GALLERY);
        Log.d(TAG, "bukagallery: tes");
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
