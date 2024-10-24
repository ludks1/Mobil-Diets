package com.example.diets.record;

import java.util.List;

public record RecipeResponse(int offset,
                            int number,
                            List<Result> results,
                            int totalResults) {
    public record Result(int id,
                         String title,
                         String image,
                         String imageType) {
    }
}
