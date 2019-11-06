package rndmjck.com.si_tifuniks.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rndmjck.com.si_tifuniks.model.Gallery;

/**
 * Created by rndmjck on 16/08/18.
 */

public class DetailGalleryResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Gallery data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Gallery getData() {
        return data;
    }

    public void setData(Gallery data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
