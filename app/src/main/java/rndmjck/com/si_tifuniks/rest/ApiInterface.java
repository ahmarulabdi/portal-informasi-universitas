package rndmjck.com.si_tifuniks.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rndmjck.com.si_tifuniks.response.DataGalleryResponse;
import rndmjck.com.si_tifuniks.response.DataInfoResponse;
import rndmjck.com.si_tifuniks.response.DataJadwalKuliahResponse;
import rndmjck.com.si_tifuniks.response.DetailGalleryResponse;
import rndmjck.com.si_tifuniks.response.DetailInfoResponse;
import rndmjck.com.si_tifuniks.response.DetailJadwalKuliahResponse;
import rndmjck.com.si_tifuniks.response.LoginResponse;

public interface ApiInterface {

    @GET("info/profile")
    Call<DetailInfoResponse> profile();

    @GET("info/datas")
    Call<DataInfoResponse> dataInfo();

    @FormUrlEncoded
    @POST("info/create")
    Call<DetailInfoResponse> tambahInfo(
            @Field("title") String title,
            @Field("deskripsi") String deskripsi
    );

    @FormUrlEncoded
    @POST("info/edit")
    Call<DetailInfoResponse> editInfo(
            @Field("id_info") String idInfo,
            @Field("title") String title,
            @Field("deskripsi") String deskripsi
    );

    @FormUrlEncoded
    @POST("info/delete")
    Call<DetailInfoResponse> deleteInfo(
            @Field("id_info") String idInfo
    );

    @GET("jadwalkuliah/datas")
    Call<DataJadwalKuliahResponse> dataJadwalKuliah();

    @GET("jadwalkuliah/data/{id_jadwal_kuliah}")
    Call<DetailJadwalKuliahResponse> detailJadwalKuliah(
            @Path("id_jadwal_kuliah") String idJadwalKuliah
    );

    @GET("gallery/datas")
    Call<DataGalleryResponse> dataGallery();

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> login(
            @Field("nip_nim") String nipNim,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("gallery/create")
    Call<DetailGalleryResponse> tambahGallery(
            @Field("image_title") String imageTitle,
            @Field("image_base64") String imageBase64,
            @Field("title") String title,
            @Field("deskripsi") String deskripsi
    );

    @FormUrlEncoded
    @POST("gallery/delete")
    Call<DetailGalleryResponse> deleteGallery(
            @Field("id_gallery") String idGallery
    );
    @GET("gallery/data/{id_gallery}")
    Call<DetailGalleryResponse> detailGallery(
            @Path("id_gallery") String idGallery
    );

    @FormUrlEncoded
    @POST("gallery/update")
    Call<DetailGalleryResponse> updateGallery(
            @Field("id_gallery") String idGallery,
            @Field("image_title") String imageTitle,
            @Field("image_base64") String imageBase64,
            @Field("title") String title,
            @Field("deskripsi") String deskripsi
    );



}
