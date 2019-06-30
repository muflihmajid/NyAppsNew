package com.example.nytimesapps.API;

import com.example.nytimesapps.Model.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface nyTimesAPI {
    @GET("svc/search/v2/articlesearch.json")
    Call<ResponseWrapper> loadArticles(@Query("api-key") String key,
                                       @Query("q") String query,
                                       @Query("page") String pNum,
                                       @Query("fq") String newsDesk);
}
