package com.example.diets.api;

import com.example.diets.record.RecipeByIngredientsResponse;
import com.example.diets.record.RecipeResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FoodNutritionApiConnection {
    private static final String apiKey =
            "ff17729e74f14a72b489a39749365886&";
    private static final String url =
            "https://api.spoonacular.com/recipes/complexSearch?apiKey=";

    public RecipeResponse getFoodRecipes(String foodName) throws IOException {
        String requestUrl = String.format(url,
                apiKey, foodName);

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        RecipeResponse recipeResponse = gson.fromJson(response.toString(), RecipeResponse.class);

        return recipeResponse;
    }

    public List<RecipeByIngredientsResponse> getRecipesByIngredients(String ingredients) throws IOException {
        String requestUrl = String.format("https://api.spoonacular.com/recipes/findByIngredients?apiKey=%s&ingredients=%s", apiKey, ingredients);

        URL  url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        List<RecipeByIngredientsResponse> recipes = gson.fromJson(
                response.toString(),
                new TypeToken<List<RecipeByIngredientsResponse>>(){}.getType()
        );
        return recipes;
    }
    //probando que funcione los cambios

}
