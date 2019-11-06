package rndmjck.com.si_tifuniks.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import rndmjck.com.si_tifuniks.model.JadwalKuliah;

/**
 * Created by rndmjck on 15/08/18.
 */

public class DetailJadwalKuliahResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private JadwalKuliah data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public JadwalKuliah getData() {
        return data;
    }

    public void setData(JadwalKuliah data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
