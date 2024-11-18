package com.example.diets.api;

import com.example.diets.record.RecipeResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodService {
    private static final String apiKey = "ff17729e74f14a72b489a39749365886";
    private static final String baseUrl = "https://api.spoonacular.com";

    // Método público para obtener recetas basadas en el nombre del alimento
    public List<RecipeResponse.Result> getFoodRecipes(String foodName) throws IOException {
        String requestUrl = baseUrl + "/recipes/complexSearch" +
                "?query=" + foodName +
                "&apiKey=" + apiKey;

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
        connection.disconnect();

        Gson gson = new Gson();
        try {
            RecipeResponse recipeResponse = gson.fromJson(response.toString(), RecipeResponse.class);
            return recipeResponse != null ? recipeResponse.results() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Obtener información nutricional de una receta por su ID
    public JsonObject getRecipeNutrition(int recipeId) throws IOException {
        String requestUrl = baseUrl + "/recipes/" + recipeId + "/nutritionWidget.json" +
                "?apiKey=" + apiKey;

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
        connection.disconnect();

        Gson gson = new Gson();
        try {
            return gson.fromJson(response.toString(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
