package com.example.diets.api;

import com.example.diets.record.RecipeByIngredientsResponse;
import com.example.diets.record.RecipeResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class FoodService {
    private static final String apiKey =
            "ff17729e74f14a72b489a39749365886&";
    private static final String url = "https://api.spoonacular.com/recipes/complexSearch?";


    public RecipeResponse getFoodRecipes(String foodName) throws IOException {
        String requestUrl = String.format(url,
                "query=",foodName,
                "&apiKey=", apiKey);
        // '&' importante para concatenar los
        // parametros de la URL

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
            return recipeResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RecipeByIngredientsResponse> getRecipesByIngredients(String ingredients) throws IOException {
        String requestUrl = String.format(
                "https://api.spoonacular" +
                        ".com/recipes" +
                        "/findByIngredients" +
                        "?ingredients=",
                ingredients,"&apiKey=", apiKey);

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
        try {
            List<RecipeByIngredientsResponse> recipes = gson.fromJson(
                    response.toString(),
                    new TypeToken<List<RecipeByIngredientsResponse>>(){}.getType()
            );
            return recipes;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addFood(RecipeResponse recipe) throws IOException {
        Gson gson = new Gson();
        String requestUrl = String.format(url,
                apiKey, recipe);

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content" +
                "-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        String jsonInputString = gson.toJson(recipe);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);

            // Verificar la respuesta de la API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) { // HTTP 201 Created
                System.out.println("Alimento agregado exitosamente.");
            } else {
                System.out.println("Error al agregar el alimento: " + responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
