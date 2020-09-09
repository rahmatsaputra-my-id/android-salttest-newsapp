package com.example.salttest.resources;

import com.example.salttest.models.Berita;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Provider_interface {

    //fromapple
//    @GET("everything")
//    Call<Berita> getBerita(
//        @Query("q") String q1,
//        @Query("from") String from1,
//        @Query("to") String to1,
//        @Query("sortBy") String sortBy1,
//        @Query("apiKey") String apiKey
//    );

    //from indonesia
    @GET("top-headlines")
    Call<Berita> getBerita(
        @Query("country") String _country,
        @Query("category") String category,
        @Query("apiKey") String apiKey
    );

//    @GET("everything")
//        Call<Berita> getBerita(
//            @Query("q") String country,
//            @Query("apiKey") String apiKey
//        );

    @GET("everything")
    Call<Berita> getBeritaSearch(

            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );
}
