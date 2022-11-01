package com.test.wavetectest.retrofit;

import com.test.wavetectest.models.PhotosRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("curated")
    Call<PhotosRoot> getPhotos(
      @Header("Authorization") String auth_Key,
      @Query("page") int page_count,
      @Query("per_page") int per_page
    );

}
