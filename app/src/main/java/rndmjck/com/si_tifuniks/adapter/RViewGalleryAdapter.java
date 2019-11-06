package rndmjck.com.si_tifuniks.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.Administrator.EditGalleryActivity;
import rndmjck.com.si_tifuniks.R;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.config.ServerConfig;
import rndmjck.com.si_tifuniks.model.Gallery;
import rndmjck.com.si_tifuniks.model.Info;
import rndmjck.com.si_tifuniks.response.DetailGalleryResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

/**
 * Created by rndmjck on 15/08/18.
 */

public class RViewGalleryAdapter extends RecyclerView.Adapter<RViewGalleryAdapter.ViewHolderGallery> {

    private List<Gallery> galleries;
    private Context context;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    public RViewGalleryAdapter(List<Gallery> galleries, Context context) {
        this.galleries = galleries;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderGallery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_view_adapter, parent, false);
        ViewHolderGallery viewHolderGallery = new ViewHolderGallery(view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(context.getApplicationContext());

        return viewHolderGallery;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGallery holder, final int position) {
        holder.textViewTitle.setText(galleries.get(position).getTitle());
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(ServerConfig.IMAGE_FOLDER_GALLERY + galleries.get(position).getGambar())
                .into(holder.imageViewGambar);
        holder.textViewDeskripsi.setText(galleries.get(position).getDeskripsi());
        holder.textViewCreatedAt.setText(galleries.get(position).getCreatedAt());

        if (!sessionManager.isLoggedIn()){
            holder.textViewEdit.setVisibility(View.GONE);
            holder.textViewDelete.setVisibility(View.GONE);
        }


        holder.textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context.getApplicationContext(), EditGalleryActivity.class);
                intent.putExtra("id_gallery",galleries.get(position).getIdGallery());
                context.startActivity(intent);
            }
        });

        holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("konfirmasi");
                builder.setMessage("apakah anda yakin menghapus gallery " + galleries.get(position).getTitle() + " ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idGallery = galleries.get(position).getIdGallery();

                        deleteGallery(idGallery, position);
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    private void deleteGallery(String idGallery, final int position) {
        Log.d(String.valueOf(context), "deleteGallery: "+idGallery);

        apiInterface.deleteGallery(idGallery).enqueue(new Callback<DetailGalleryResponse>() {
            @Override
            public void onResponse(Call<DetailGalleryResponse> call, Response<DetailGalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        galleries.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailGalleryResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(),"koneksi gagal",Toast.LENGTH_SHORT).show();
                Log.d(String.valueOf(context), "onFailure: "+t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleries.size();
    }


    class ViewHolderGallery extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewGambar;
        TextView textViewDeskripsi;
        TextView textViewCreatedAt;
        TextView textViewEdit;
        TextView textViewDelete;

        public ViewHolderGallery(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.item_text_view_title_gallery);
            imageViewGambar = itemView.findViewById(R.id.item_image_view_gambar_gallery);
            textViewDeskripsi = itemView.findViewById(R.id.item_text_view_deskripsi_gallery);
            textViewCreatedAt = itemView.findViewById(R.id.item_text_view_created_at_gallery);
            textViewEdit = itemView.findViewById(R.id.item_text_view_edit_gallery);
            textViewDelete = itemView.findViewById(R.id.item_text_view_delete_gallery);
        }
    }


}
