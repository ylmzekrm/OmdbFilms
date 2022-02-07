package com.test.omdbMovieFilter.interfaces;

import com.test.omdbMovieFilter.models.DetailModel;
import com.test.omdbMovieFilter.models.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbInterface {
    @GET("?type=movie")
    Call<ResultModel> Result(
            @Query("s") String Title);

    @GET("?plot=full")
    Call<DetailModel> Detail(
            @Query("i") String ImdbId);
}
