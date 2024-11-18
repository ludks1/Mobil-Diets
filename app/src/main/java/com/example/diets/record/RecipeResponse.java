package com.example.diets.record;

import java.util.List;

public record RecipeResponse(int offset,
                             int number,
                             List<Result> results,
                             int totalResults) {

    public record Result(int id,
                         String title,
                         String image,
                         String imageType,
                         double calories,    // Calorías de la receta
                         double protein,     // Cantidad de proteínas
                         double fat,         // Cantidad de grasa
                         double carbohydrates) { // Cantidad de carbohidratos

        // Métodos getters
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getImageType() {
            return imageType;
        }

        public double getCalories() {
            return calories;
        }

        public double getProtein() {
            return protein;
        }

        public double getFat() {
            return fat;
        }

        public double getCarbohydrates() {
            return carbohydrates;
        }

        // Método para obtener información completa de la receta
        public String getRecipeInfo() {
            return String.format("Receta: %s\nCalorías: %.2f kcal\nProteínas: %.2f g\nGrasas: %.2f g\nCarbohidratos: %.2f g",
                    title, calories, protein, fat, carbohydrates);
        }
    }
}
