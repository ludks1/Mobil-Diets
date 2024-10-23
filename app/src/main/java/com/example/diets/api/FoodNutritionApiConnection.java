package com.example.diets.api;

import com.example.diets.record.FoodRecipes;
import com.google.gson.Gson;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FoodNutritionApiConnection {
    private final String apiKey = "ff17729e74f14a72b489a39749365886&";

    public FoodRecipes getFoodRecipes(String foodName) {
        String url = "https://api.spoonacular" +
                ".com/recipes/complexSearch" +
                "?apiKey=" + apiKey + "&query" +
                "=" + foodName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
           if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
              }
           return new Gson().fromJson(response.body().string(), FoodRecipes.class);
        } catch (IOException e) {
            throw new RuntimeException("Error " +
                    "al tratar de buscar la " +
                    "comida:", e);
        }
    }
}
