package rndmjck.com.si_tifuniks.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rndmjck.com.si_tifuniks.R;
import rndmjck.com.si_tifuniks.Utility.SessionManager;
import rndmjck.com.si_tifuniks.model.Info;
import rndmjck.com.si_tifuniks.response.DetailInfoResponse;
import rndmjck.com.si_tifuniks.rest.ApiClient;
import rndmjck.com.si_tifuniks.rest.ApiInterface;

/**
 * Created by rndmjck on 15/08/18.
 */

public class RViewInfoAdapter extends RecyclerView.Adapter<RViewInfoAdapter.ViewHolderInfo> {

    private static final String TAG = "RViewInfoAdapter";
    private List<Info> infos;
    private Context context;
    private ApiInterface apiInterface;
    private SessionManager sessionManager;


    public RViewInfoAdapter(List<Info> infos, Context context) {
        this.infos = infos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_view_adapter, parent, false);
        ViewHolderInfo viewHolderInfo = new ViewHolderInfo(view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sessionManager = new SessionManager(context.getApplicationContext());
        return viewHolderInfo;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderInfo holder, final int position) {
        holder.textViewTitle.setText(infos.get(position).getTitle());
        holder.textViewDeskripsi.setText(infos.get(position).getDeskripsi());
        holder.textViewCreatedAt.setText(infos.get(position).getUpdatedAt());

        if (!sessionManager.isLoggedIn()){
            holder.textViewEdit.setVisibility(View.GONE);
            holder.textViewDelete.setVisibility(View.GONE);
        }

        holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("konfirmasi");
                builder.setMessage("apakah anda yakin ingin menghapus info " + infos.get(position).getTitle() + " ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idInfo = infos.get(position).getIdInfo();
                        deleteInfo(idInfo);
                        infos.remove(position);
                        notifyDataSetChanged();

                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });

        holder.textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);

                final EditText editTextTitle = new EditText(context.getApplicationContext());
                editTextTitle.setText(infos.get(position).getTitle());
                final EditText editTextDeskripsi = new EditText(context.getApplicationContext());
                editTextDeskripsi.setText(infos.get(position).getDeskripsi());


                linearLayout.addView(editTextTitle,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(editTextDeskripsi,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                builder.setView(linearLayout);
                builder.setTitle("Edit Info");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idInfo = infos.get(position).getIdInfo();
                        String Title = editTextTitle.getText().toString();
                        String Deskripsi = editTextDeskripsi.getText().toString();
                        editInfo(idInfo,Title,Deskripsi,holder);
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }
        });

    }

    private void editInfo(String idInfo, final String title, final String deskripsi, final ViewHolderInfo holder) {
        Log.d(TAG, "editInfo: idInfo "+ idInfo);
        Log.d(TAG, "editInfo: title"+ title);
        Log.d(TAG, "editInfo: deskripsi "+ deskripsi);
        apiInterface.editInfo(idInfo,title,deskripsi).enqueue(new Callback<DetailInfoResponse>() {
            @Override
            public void onResponse(Call<DetailInfoResponse> call, Response<DetailInfoResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        Toast.makeText(context.getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        holder.textViewTitle.setText(title);
                        holder.textViewDeskripsi.setText(deskripsi);
                    }else {
                        Toast.makeText(context.getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailInfoResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(),"koneksi gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteInfo(String idInfo) {
        apiInterface.deleteInfo(idInfo).enqueue(new Callback<DetailInfoResponse>() {
            @Override
            public void onResponse(Call<DetailInfoResponse> call, Response<DetailInfoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailInfoResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }


    class ViewHolderInfo extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDeskripsi;
        TextView textViewCreatedAt;
        TextView textViewEdit;
        TextView textViewDelete;

        public ViewHolderInfo(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.item_text_view_title_info);
            textViewDeskripsi = itemView.findViewById(R.id.item_text_view_deskripsi_info);
            textViewCreatedAt = itemView.findViewById(R.id.item_text_view_created_at_info);
            textViewEdit = itemView.findViewById(R.id.item_text_view_edit_info);
            textViewDelete = itemView.findViewById(R.id.item_text_view_delete_info);
        }
    }
}
