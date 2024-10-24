package com.example.diets.record;

import java.util.List;

public record RecipeByIngredientsResponse(
        int id,
        String image,
        String imageType,
        int likes,
        int missedIngredientCount,
        List<Ingredient> missedIngredients,
        String title,
        List<Ingredient> unusedIngredients,
        int usedIngredientCount,
        List<Ingredient> usedIngredients
) {
    public record Ingredient(
            String aisle,
            double amount,
            int id,
            String image,
            List<String> meta,
            String name,
            String original,
            String originalName,
            String unit,
            String unitLong,
            String unitShort
    ) {}
}
