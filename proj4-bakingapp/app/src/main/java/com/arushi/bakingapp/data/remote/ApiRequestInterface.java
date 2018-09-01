package com.arushi.bakingapp.data.remote;

import com.arushi.bakingapp.data.remote.model.Dessert;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiRequestInterface {

    @Headers({"Content-Type: application/json",
            "User-Agent: Baking-App"})
    @GET("59121517_baking/baking.json")
    Call<List<Dessert>> getDessertData();
}
