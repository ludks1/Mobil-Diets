package com.example.diets.api;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.diets.record.RecipeResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
    public JsonObject getFoodNutrition(String foodName) throws IOException {
        // Llama al método fetchNutritionData desde la misma instancia
        JsonObject nutritionData = fetchNutritionData(foodName);
        if (nutritionData != null && nutritionData.has("nutrients")) {
            return nutritionData;
        } else {
            Log.e("FoodService", "No se encontraron datos para el alimento: " + foodName);
            return null;
        }
    }

    private JsonObject fetchNutritionData(String foodName) throws IOException {
        // Aquí va tu lógica para hacer la solicitud HTTP o llamada a la API
        JsonObject fakeNutritionData = new JsonObject();
        // Ejemplo de datos ficticios para simular la respuesta de la API
        fakeNutritionData.addProperty("name", foodName);
        JsonArray nutrients = new JsonArray();

        JsonObject calories = new JsonObject();
        calories.addProperty("name", "Calories");
        calories.addProperty("amount", 250.0);
        nutrients.add(calories);

        JsonObject protein = new JsonObject();
        protein.addProperty("name", "Protein");
        protein.addProperty("amount", 20.0);
        nutrients.add(protein);

        fakeNutritionData.add("nutrients", nutrients);

        return fakeNutritionData; // Reemplaza esto con los datos reales de tu API.
    }
    // Placeholder para simular la búsqueda de información nutricional
    private JsonObject getRecipeNutritionByName(String foodName) throws IOException {
        JsonObject nutritionData = new JsonObject();

        // Simulación de datos obtenidos de una API
        switch (foodName.toLowerCase()) {
            case "manzana":
                nutritionData.addProperty("Calories", 52);
                nutritionData.addProperty("Protein", 0.3);
                nutritionData.addProperty("Fat", 0.2);
                nutritionData.addProperty("Carbohydrates", 14);
                break;
            case "pollo":
                nutritionData.addProperty("Calories", 239);
                nutritionData.addProperty("Protein", 27);
                nutritionData.addProperty("Fat", 14);
                nutritionData.addProperty("Carbohydrates", 0);
                break;
            case "arroz":
                nutritionData.addProperty("Calories", 130);
                nutritionData.addProperty("Protein", 2.4);
                nutritionData.addProperty("Fat", 0.3);
                nutritionData.addProperty("Carbohydrates", 28);
                break;
            default:
                nutritionData.addProperty("Calories", 0);
                nutritionData.addProperty("Protein", 0);
                nutritionData.addProperty("Fat", 0);
                nutritionData.addProperty("Carbohydrates", 0);
                break;
        }

        return nutritionData;
    }
}
