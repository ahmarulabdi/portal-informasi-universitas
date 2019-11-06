package rndmjck.com.si_tifuniks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 16/08/18.
 */

public class Gallery {

    @SerializedName("id_gallery")
    @Expose
    private String idGallery;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getIdGallery() {
        return idGallery;
    }

    public void setIdGallery(String idGallery) {
        this.idGallery = idGallery;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
