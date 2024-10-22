package com.example.diets.record;

import java.util.Objects;

public final class FoodRecipes {
    private final String query;
    private final String cuisine;
    private final String excludeCuisine;
    private final String diet;
    private final String intolerances;
    private final String equipment;
    private final String includeIngredients;
    private final String excludeIngredients;
    private final String type;
    private final boolean instructionsRequired;
    private final boolean fillIngredients;
    private final boolean addRecipeInformation;
    private final boolean addRecipeInstructions;
    private final boolean addRecipeNutrition;
    private final String author;
    private final String tags;
    private final int recipeBoxId;
    private final String titleMatch;
    private final int maxReadyTime;
    private final int minServings;
    private final int maxServings;
    private final boolean ignorePantry;
    private final String sort;
    private final String sortDirection;
    private final int minCarbs;
    private final int maxCarbs;
    private final int minProtein;
    private final int maxProtein;
    private final int minCalories;
    private final int minFat;
    private final int maxFat;
    private final int minAlcohol;
    private final int maxAlcohol;
    private final int minCaffeine;
    private final int maxCaffeine;
    private final int minCopper;
    private final int maxCopper;
    private final int minCalcium;
    private final int maxCalcium;
    private final int minCholine;
    private final int maxCholine;
    private final int minCholesterol;
    private final int maxCholesterol;
    private final int minFluoride;
    private final int maxFluoride;
    private final int minSaturatedFat;
    private final int maxSaturatedFat;
    private final int minVitaminA;
    private final int maxVitaminA;
    private final int minVitaminC;
    private final int maxVitaminC;
    private final int minVitaminD;
    private final int maxVitaminD;
    private final int minVitaminE;
    private final int maxVitaminE;
    private final int minVitaminK;
    private final int maxVitaminK;
    private final int minVitaminB1;
    private final int maxVitaminB1;
    private final int minVitaminB2;
    private final int maxVitaminB2;
    private final int minVitaminB5;
    private final int maxVitaminB5;
    private final int minVitaminB3;
    private final int maxVitaminB3;
    private final int minVitaminB6;
    private final int maxVitaminB6;
    private final int minVitaminB12;
    private final int maxVitaminB12;
    private final int minFiber;
    private final int maxFiber;
    private final int minFolate;
    private final int maxFolate;
    private final int minFolicAcid;
    private final int maxFolicAcid;
    private final int minIodine;
    private final int maxIodine;
    private final int minIron;
    private final int maxIron;
    private final int minMagnesium;
    private final int maxMagnesium;
    private final int minManganese;
    private final int maxManganese;
    private final int minPhosphorus;
    private final int maxPhosphorus;
    private final int minPotassium;
    private final int maxPotassium;
    private final int minSelenium;
    private final int maxSelenium;
    private final int minSodium;
    private final int maxSodium;
    private final int minSugar;
    private final int maxSugar;
    private final int minZinc;
    private final int maxZinc;
    private final int offset;
    private final int number;

    public FoodRecipes(String query,
                       String cuisine,
                       String excludeCuisine,
                       String diet,
                       String intolerances,
                       String equipment,
                       String includeIngredients,
                       String excludeIngredients,
                       String type,
                       boolean instructionsRequired,
                       boolean fillIngredients,
                       boolean addRecipeInformation,
                       boolean addRecipeInstructions,
                       boolean addRecipeNutrition,
                       String author,
                       String tags,
                       int recipeBoxId,
                       String titleMatch,
                       int maxReadyTime,
                       int minServings,
                       int maxServings,
                       boolean ignorePantry,
                       String sort,
                       String sortDirection,
                       int minCarbs,
                       int maxCarbs,
                       int minProtein,
                       int maxProtein,
                       int minCalories,
                       int minFat,
                       int maxFat,
                       int minAlcohol,
                       int maxAlcohol,
                       int minCaffeine,
                       int maxCaffeine,
                       int minCopper,
                       int maxCopper,
                       int minCalcium,
                       int maxCalcium,
                       int minCholine,
                       int maxCholine,
                       int minCholesterol,
                       int maxCholesterol,
                       int minFluoride,
                       int maxFluoride,
                       int minSaturatedFat,
                       int maxSaturatedFat,
                       int minVitaminA,
                       int maxVitaminA,
                       int minVitaminC,
                       int maxVitaminC,
                       int minVitaminD,
                       int maxVitaminD,
                       int minVitaminE,
                       int maxVitaminE,
                       int minVitaminK,
                       int maxVitaminK,
                       int minVitaminB1,
                       int maxVitaminB1,
                       int minVitaminB2,
                       int maxVitaminB2,
                       int minVitaminB5,
                       int maxVitaminB5,
                       int minVitaminB3,
                       int maxVitaminB3,
                       int minVitaminB6,
                       int maxVitaminB6,
                       int minVitaminB12,
                       int maxVitaminB12,
                       int minFiber,
                       int maxFiber,
                       int minFolate,
                       int maxFolate,
                       int minFolicAcid,
                       int maxFolicAcid,
                       int minIodine,
                       int maxIodine,
                       int minIron,
                       int maxIron,
                       int minMagnesium,
                       int maxMagnesium,
                       int minManganese,
                       int maxManganese,
                       int minPhosphorus,
                       int maxPhosphorus,
                       int minPotassium,
                       int maxPotassium,
                       int minSelenium,
                       int maxSelenium,
                       int minSodium,
                       int maxSodium,
                       int minSugar,
                       int maxSugar,
                       int minZinc,
                       int maxZinc,
                       int offset,
                       int number) {
        this.query = query;
        this.cuisine = cuisine;
        this.excludeCuisine = excludeCuisine;
        this.diet = diet;
        this.intolerances = intolerances;
        this.equipment = equipment;
        this.includeIngredients = includeIngredients;
        this.excludeIngredients = excludeIngredients;
        this.type = type;
        this.instructionsRequired = instructionsRequired;
        this.fillIngredients = fillIngredients;
        this.addRecipeInformation = addRecipeInformation;
        this.addRecipeInstructions = addRecipeInstructions;
        this.addRecipeNutrition = addRecipeNutrition;
        this.author = author;
        this.tags = tags;
        this.recipeBoxId = recipeBoxId;
        this.titleMatch = titleMatch;
        this.maxReadyTime = maxReadyTime;
        this.minServings = minServings;
        this.maxServings = maxServings;
        this.ignorePantry = ignorePantry;
        this.sort = sort;
        this.sortDirection = sortDirection;
        this.minCarbs = minCarbs;
        this.maxCarbs = maxCarbs;
        this.minProtein = minProtein;
        this.maxProtein = maxProtein;
        this.minCalories = minCalories;
        this.minFat = minFat;
        this.maxFat = maxFat;
        this.minAlcohol = minAlcohol;
        this.maxAlcohol = maxAlcohol;
        this.minCaffeine = minCaffeine;
        this.maxCaffeine = maxCaffeine;
        this.minCopper = minCopper;
        this.maxCopper = maxCopper;
        this.minCalcium = minCalcium;
        this.maxCalcium = maxCalcium;
        this.minCholine = minCholine;
        this.maxCholine = maxCholine;
        this.minCholesterol = minCholesterol;
        this.maxCholesterol = maxCholesterol;
        this.minFluoride = minFluoride;
        this.maxFluoride = maxFluoride;
        this.minSaturatedFat = minSaturatedFat;
        this.maxSaturatedFat = maxSaturatedFat;
        this.minVitaminA = minVitaminA;
        this.maxVitaminA = maxVitaminA;
        this.minVitaminC = minVitaminC;
        this.maxVitaminC = maxVitaminC;
        this.minVitaminD = minVitaminD;
        this.maxVitaminD = maxVitaminD;
        this.minVitaminE = minVitaminE;
        this.maxVitaminE = maxVitaminE;
        this.minVitaminK = minVitaminK;
        this.maxVitaminK = maxVitaminK;
        this.minVitaminB1 = minVitaminB1;
        this.maxVitaminB1 = maxVitaminB1;
        this.minVitaminB2 = minVitaminB2;
        this.maxVitaminB2 = maxVitaminB2;
        this.minVitaminB5 = minVitaminB5;
        this.maxVitaminB5 = maxVitaminB5;
        this.minVitaminB3 = minVitaminB3;
        this.maxVitaminB3 = maxVitaminB3;
        this.minVitaminB6 = minVitaminB6;
        this.maxVitaminB6 = maxVitaminB6;
        this.minVitaminB12 = minVitaminB12;
        this.maxVitaminB12 = maxVitaminB12;
        this.minFiber = minFiber;
        this.maxFiber = maxFiber;
        this.minFolate = minFolate;
        this.maxFolate = maxFolate;
        this.minFolicAcid = minFolicAcid;
        this.maxFolicAcid = maxFolicAcid;
        this.minIodine = minIodine;
        this.maxIodine = maxIodine;
        this.minIron = minIron;
        this.maxIron = maxIron;
        this.minMagnesium = minMagnesium;
        this.maxMagnesium = maxMagnesium;
        this.minManganese = minManganese;
        this.maxManganese = maxManganese;
        this.minPhosphorus = minPhosphorus;
        this.maxPhosphorus = maxPhosphorus;
        this.minPotassium = minPotassium;
        this.maxPotassium = maxPotassium;
        this.minSelenium = minSelenium;
        this.maxSelenium = maxSelenium;
        this.minSodium = minSodium;
        this.maxSodium = maxSodium;
        this.minSugar = minSugar;
        this.maxSugar = maxSugar;
        this.minZinc = minZinc;
        this.maxZinc = maxZinc;
        this.offset = offset;
        this.number = number;
    }

    public String query() {
        return query;
    }

    public String cuisine() {
        return cuisine;
    }

    public String excludeCuisine() {
        return excludeCuisine;
    }

    public String diet() {
        return diet;
    }

    public String intolerances() {
        return intolerances;
    }

    public String equipment() {
        return equipment;
    }

    public String includeIngredients() {
        return includeIngredients;
    }

    public String excludeIngredients() {
        return excludeIngredients;
    }

    public String type() {
        return type;
    }

    public boolean instructionsRequired() {
        return instructionsRequired;
    }

    public boolean fillIngredients() {
        return fillIngredients;
    }

    public boolean addRecipeInformation() {
        return addRecipeInformation;
    }

    public boolean addRecipeInstructions() {
        return addRecipeInstructions;
    }

    public boolean addRecipeNutrition() {
        return addRecipeNutrition;
    }

    public String author() {
        return author;
    }

    public String tags() {
        return tags;
    }

    public int recipeBoxId() {
        return recipeBoxId;
    }

    public String titleMatch() {
        return titleMatch;
    }

    public int maxReadyTime() {
        return maxReadyTime;
    }

    public int minServings() {
        return minServings;
    }

    public int maxServings() {
        return maxServings;
    }

    public boolean ignorePantry() {
        return ignorePantry;
    }

    public String sort() {
        return sort;
    }

    public String sortDirection() {
        return sortDirection;
    }

    public int minCarbs() {
        return minCarbs;
    }

    public int maxCarbs() {
        return maxCarbs;
    }

    public int minProtein() {
        return minProtein;
    }

    public int maxProtein() {
        return maxProtein;
    }

    public int minCalories() {
        return minCalories;
    }

    public int minFat() {
        return minFat;
    }

    public int maxFat() {
        return maxFat;
    }

    public int minAlcohol() {
        return minAlcohol;
    }

    public int maxAlcohol() {
        return maxAlcohol;
    }

    public int minCaffeine() {
        return minCaffeine;
    }

    public int maxCaffeine() {
        return maxCaffeine;
    }

    public int minCopper() {
        return minCopper;
    }

    public int maxCopper() {
        return maxCopper;
    }

    public int minCalcium() {
        return minCalcium;
    }

    public int maxCalcium() {
        return maxCalcium;
    }

    public int minCholine() {
        return minCholine;
    }

    public int maxCholine() {
        return maxCholine;
    }

    public int minCholesterol() {
        return minCholesterol;
    }

    public int maxCholesterol() {
        return maxCholesterol;
    }

    public int minFluoride() {
        return minFluoride;
    }

    public int maxFluoride() {
        return maxFluoride;
    }

    public int minSaturatedFat() {
        return minSaturatedFat;
    }

    public int maxSaturatedFat() {
        return maxSaturatedFat;
    }

    public int minVitaminA() {
        return minVitaminA;
    }

    public int maxVitaminA() {
        return maxVitaminA;
    }

    public int minVitaminC() {
        return minVitaminC;
    }

    public int maxVitaminC() {
        return maxVitaminC;
    }

    public int minVitaminD() {
        return minVitaminD;
    }

    public int maxVitaminD() {
        return maxVitaminD;
    }

    public int minVitaminE() {
        return minVitaminE;
    }

    public int maxVitaminE() {
        return maxVitaminE;
    }

    public int minVitaminK() {
        return minVitaminK;
    }

    public int maxVitaminK() {
        return maxVitaminK;
    }

    public int minVitaminB1() {
        return minVitaminB1;
    }

    public int maxVitaminB1() {
        return maxVitaminB1;
    }

    public int minVitaminB2() {
        return minVitaminB2;
    }

    public int maxVitaminB2() {
        return maxVitaminB2;
    }

    public int minVitaminB5() {
        return minVitaminB5;
    }

    public int maxVitaminB5() {
        return maxVitaminB5;
    }

    public int minVitaminB3() {
        return minVitaminB3;
    }

    public int maxVitaminB3() {
        return maxVitaminB3;
    }

    public int minVitaminB6() {
        return minVitaminB6;
    }

    public int maxVitaminB6() {
        return maxVitaminB6;
    }

    public int minVitaminB12() {
        return minVitaminB12;
    }

    public int maxVitaminB12() {
        return maxVitaminB12;
    }

    public int minFiber() {
        return minFiber;
    }

    public int maxFiber() {
        return maxFiber;
    }

    public int minFolate() {
        return minFolate;
    }

    public int maxFolate() {
        return maxFolate;
    }

    public int minFolicAcid() {
        return minFolicAcid;
    }

    public int maxFolicAcid() {
        return maxFolicAcid;
    }

    public int minIodine() {
        return minIodine;
    }

    public int maxIodine() {
        return maxIodine;
    }

    public int minIron() {
        return minIron;
    }

    public int maxIron() {
        return maxIron;
    }

    public int minMagnesium() {
        return minMagnesium;
    }

    public int maxMagnesium() {
        return maxMagnesium;
    }

    public int minManganese() {
        return minManganese;
    }

    public int maxManganese() {
        return maxManganese;
    }

    public int minPhosphorus() {
        return minPhosphorus;
    }

    public int maxPhosphorus() {
        return maxPhosphorus;
    }

    public int minPotassium() {
        return minPotassium;
    }

    public int maxPotassium() {
        return maxPotassium;
    }

    public int minSelenium() {
        return minSelenium;
    }

    public int maxSelenium() {
        return maxSelenium;
    }

    public int minSodium() {
        return minSodium;
    }

    public int maxSodium() {
        return maxSodium;
    }

    public int minSugar() {
        return minSugar;
    }

    public int maxSugar() {
        return maxSugar;
    }

    public int minZinc() {
        return minZinc;
    }

    public int maxZinc() {
        return maxZinc;
    }

    public int offset() {
        return offset;
    }

    public int number() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        FoodRecipes that = (FoodRecipes) obj;
        return Objects.equals(this.query, that.query) &&
                Objects.equals(this.cuisine, that.cuisine) &&
                Objects.equals(this.excludeCuisine, that.excludeCuisine) &&
                Objects.equals(this.diet, that.diet) &&
                Objects.equals(this.intolerances, that.intolerances) &&
                Objects.equals(this.equipment, that.equipment) &&
                Objects.equals(this.includeIngredients, that.includeIngredients) &&
                Objects.equals(this.excludeIngredients, that.excludeIngredients) &&
                Objects.equals(this.type, that.type) &&
                this.instructionsRequired == that.instructionsRequired &&
                this.fillIngredients == that.fillIngredients &&
                this.addRecipeInformation == that.addRecipeInformation &&
                this.addRecipeInstructions == that.addRecipeInstructions &&
                this.addRecipeNutrition == that.addRecipeNutrition &&
                Objects.equals(this.author, that.author) &&
                Objects.equals(this.tags, that.tags) &&
                this.recipeBoxId == that.recipeBoxId &&
                Objects.equals(this.titleMatch, that.titleMatch) &&
                this.maxReadyTime == that.maxReadyTime &&
                this.minServings == that.minServings &&
                this.maxServings == that.maxServings &&
                this.ignorePantry == that.ignorePantry &&
                Objects.equals(this.sort, that.sort) &&
                Objects.equals(this.sortDirection, that.sortDirection) &&
                this.minCarbs == that.minCarbs &&
                this.maxCarbs == that.maxCarbs &&
                this.minProtein == that.minProtein &&
                this.maxProtein == that.maxProtein &&
                this.minCalories == that.minCalories &&
                this.minFat == that.minFat &&
                this.maxFat == that.maxFat &&
                this.minAlcohol == that.minAlcohol &&
                this.maxAlcohol == that.maxAlcohol &&
                this.minCaffeine == that.minCaffeine &&
                this.maxCaffeine == that.maxCaffeine &&
                this.minCopper == that.minCopper &&
                this.maxCopper == that.maxCopper &&
                this.minCalcium == that.minCalcium &&
                this.maxCalcium == that.maxCalcium &&
                this.minCholine == that.minCholine &&
                this.maxCholine == that.maxCholine &&
                this.minCholesterol == that.minCholesterol &&
                this.maxCholesterol == that.maxCholesterol &&
                this.minFluoride == that.minFluoride &&
                this.maxFluoride == that.maxFluoride &&
                this.minSaturatedFat == that.minSaturatedFat &&
                this.maxSaturatedFat == that.maxSaturatedFat &&
                this.minVitaminA == that.minVitaminA &&
                this.maxVitaminA == that.maxVitaminA &&
                this.minVitaminC == that.minVitaminC &&
                this.maxVitaminC == that.maxVitaminC &&
                this.minVitaminD == that.minVitaminD &&
                this.maxVitaminD == that.maxVitaminD &&
                this.minVitaminE == that.minVitaminE &&
                this.maxVitaminE == that.maxVitaminE &&
                this.minVitaminK == that.minVitaminK &&
                this.maxVitaminK == that.maxVitaminK &&
                this.minVitaminB1 == that.minVitaminB1 &&
                this.maxVitaminB1 == that.maxVitaminB1 &&
                this.minVitaminB2 == that.minVitaminB2 &&
                this.maxVitaminB2 == that.maxVitaminB2 &&
                this.minVitaminB5 == that.minVitaminB5 &&
                this.maxVitaminB5 == that.maxVitaminB5 &&
                this.minVitaminB3 == that.minVitaminB3 &&
                this.maxVitaminB3 == that.maxVitaminB3 &&
                this.minVitaminB6 == that.minVitaminB6 &&
                this.maxVitaminB6 == that.maxVitaminB6 &&
                this.minVitaminB12 == that.minVitaminB12 &&
                this.maxVitaminB12 == that.maxVitaminB12 &&
                this.minFiber == that.minFiber &&
                this.maxFiber == that.maxFiber &&
                this.minFolate == that.minFolate &&
                this.maxFolate == that.maxFolate &&
                this.minFolicAcid == that.minFolicAcid &&
                this.maxFolicAcid == that.maxFolicAcid &&
                this.minIodine == that.minIodine &&
                this.maxIodine == that.maxIodine &&
                this.minIron == that.minIron &&
                this.maxIron == that.maxIron &&
                this.minMagnesium == that.minMagnesium &&
                this.maxMagnesium == that.maxMagnesium &&
                this.minManganese == that.minManganese &&
                this.maxManganese == that.maxManganese &&
                this.minPhosphorus == that.minPhosphorus &&
                this.maxPhosphorus == that.maxPhosphorus &&
                this.minPotassium == that.minPotassium &&
                this.maxPotassium == that.maxPotassium &&
                this.minSelenium == that.minSelenium &&
                this.maxSelenium == that.maxSelenium &&
                this.minSodium == that.minSodium &&
                this.maxSodium == that.maxSodium &&
                this.minSugar == that.minSugar &&
                this.maxSugar == that.maxSugar &&
                this.minZinc == that.minZinc &&
                this.maxZinc == that.maxZinc &&
                this.offset == that.offset &&
                this.number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, cuisine, excludeCuisine, diet, intolerances, equipment, includeIngredients, excludeIngredients, type, instructionsRequired, fillIngredients, addRecipeInformation, addRecipeInstructions, addRecipeNutrition, author, tags, recipeBoxId, titleMatch, maxReadyTime, minServings, maxServings, ignorePantry, sort, sortDirection, minCarbs, maxCarbs, minProtein, maxProtein, minCalories, minFat, maxFat, minAlcohol, maxAlcohol, minCaffeine, maxCaffeine, minCopper, maxCopper, minCalcium, maxCalcium, minCholine, maxCholine, minCholesterol, maxCholesterol, minFluoride, maxFluoride, minSaturatedFat, maxSaturatedFat, minVitaminA, maxVitaminA, minVitaminC, maxVitaminC, minVitaminD, maxVitaminD, minVitaminE, maxVitaminE, minVitaminK, maxVitaminK, minVitaminB1, maxVitaminB1, minVitaminB2, maxVitaminB2, minVitaminB5, maxVitaminB5, minVitaminB3, maxVitaminB3, minVitaminB6, maxVitaminB6, minVitaminB12, maxVitaminB12, minFiber, maxFiber, minFolate, maxFolate, minFolicAcid, maxFolicAcid, minIodine, maxIodine, minIron, maxIron, minMagnesium, maxMagnesium, minManganese, maxManganese, minPhosphorus, maxPhosphorus, minPotassium, maxPotassium, minSelenium, maxSelenium, minSodium, maxSodium, minSugar, maxSugar, minZinc, maxZinc, offset, number);
    }

    @Override
    public String toString() {
        return "FoodRecipes[" +
                "query=" + query + ", " +
                "cuisine=" + cuisine + ", " +
                "excludeCuisine=" + excludeCuisine + ", " +
                "diet=" + diet + ", " +
                "intolerances=" + intolerances + ", " +
                "equipment=" + equipment + ", " +
                "includeIngredients=" + includeIngredients + ", " +
                "excludeIngredients=" + excludeIngredients + ", " +
                "type=" + type + ", " +
                "instructionsRequired=" + instructionsRequired + ", " +
                "fillIngredients=" + fillIngredients + ", " +
                "addRecipeInformation=" + addRecipeInformation + ", " +
                "addRecipeInstructions=" + addRecipeInstructions + ", " +
                "addRecipeNutrition=" + addRecipeNutrition + ", " +
                "author=" + author + ", " +
                "tags=" + tags + ", " +
                "recipeBoxId=" + recipeBoxId + ", " +
                "titleMatch=" + titleMatch + ", " +
                "maxReadyTime=" + maxReadyTime + ", " +
                "minServings=" + minServings + ", " +
                "maxServings=" + maxServings + ", " +
                "ignorePantry=" + ignorePantry + ", " +
                "sort=" + sort + ", " +
                "sortDirection=" + sortDirection + ", " +
                "minCarbs=" + minCarbs + ", " +
                "maxCarbs=" + maxCarbs + ", " +
                "minProtein=" + minProtein + ", " +
                "maxProtein=" + maxProtein + ", " +
                "minCalories=" + minCalories + ", " +
                "minFat=" + minFat + ", " +
                "maxFat=" + maxFat + ", " +
                "minAlcohol=" + minAlcohol + ", " +
                "maxAlcohol=" + maxAlcohol + ", " +
                "minCaffeine=" + minCaffeine + ", " +
                "maxCaffeine=" + maxCaffeine + ", " +
                "minCopper=" + minCopper + ", " +
                "maxCopper=" + maxCopper + ", " +
                "minCalcium=" + minCalcium + ", " +
                "maxCalcium=" + maxCalcium + ", " +
                "minCholine=" + minCholine + ", " +
                "maxCholine=" + maxCholine + ", " +
                "minCholesterol=" + minCholesterol + ", " +
                "maxCholesterol=" + maxCholesterol + ", " +
                "minFluoride=" + minFluoride + ", " +
                "maxFluoride=" + maxFluoride + ", " +
                "minSaturatedFat=" + minSaturatedFat + ", " +
                "maxSaturatedFat=" + maxSaturatedFat + ", " +
                "minVitaminA=" + minVitaminA + ", " +
                "maxVitaminA=" + maxVitaminA + ", " +
                "minVitaminC=" + minVitaminC + ", " +
                "maxVitaminC=" + maxVitaminC + ", " +
                "minVitaminD=" + minVitaminD + ", " +
                "maxVitaminD=" + maxVitaminD + ", " +
                "minVitaminE=" + minVitaminE + ", " +
                "maxVitaminE=" + maxVitaminE + ", " +
                "minVitaminK=" + minVitaminK + ", " +
                "maxVitaminK=" + maxVitaminK + ", " +
                "minVitaminB1=" + minVitaminB1 + ", " +
                "maxVitaminB1=" + maxVitaminB1 + ", " +
                "minVitaminB2=" + minVitaminB2 + ", " +
                "maxVitaminB2=" + maxVitaminB2 + ", " +
                "minVitaminB5=" + minVitaminB5 + ", " +
                "maxVitaminB5=" + maxVitaminB5 + ", " +
                "minVitaminB3=" + minVitaminB3 + ", " +
                "maxVitaminB3=" + maxVitaminB3 + ", " +
                "minVitaminB6=" + minVitaminB6 + ", " +
                "maxVitaminB6=" + maxVitaminB6 + ", " +
                "minVitaminB12=" + minVitaminB12 + ", " +
                "maxVitaminB12=" + maxVitaminB12 + ", " +
                "minFiber=" + minFiber + ", " +
                "maxFiber=" + maxFiber + ", " +
                "minFolate=" + minFolate + ", " +
                "maxFolate=" + maxFolate + ", " +
                "minFolicAcid=" + minFolicAcid + ", " +
                "maxFolicAcid=" + maxFolicAcid + ", " +
                "minIodine=" + minIodine + ", " +
                "maxIodine=" + maxIodine + ", " +
                "minIron=" + minIron + ", " +
                "maxIron=" + maxIron + ", " +
                "minMagnesium=" + minMagnesium + ", " +
                "maxMagnesium=" + maxMagnesium + ", " +
                "minManganese=" + minManganese + ", " +
                "maxManganese=" + maxManganese + ", " +
                "minPhosphorus=" + minPhosphorus + ", " +
                "maxPhosphorus=" + maxPhosphorus + ", " +
                "minPotassium=" + minPotassium + ", " +
                "maxPotassium=" + maxPotassium + ", " +
                "minSelenium=" + minSelenium + ", " +
                "maxSelenium=" + maxSelenium + ", " +
                "minSodium=" + minSodium + ", " +
                "maxSodium=" + maxSodium + ", " +
                "minSugar=" + minSugar + ", " +
                "maxSugar=" + maxSugar + ", " +
                "minZinc=" + minZinc + ", " +
                "maxZinc=" + maxZinc + ", " +
                "offset=" + offset + ", " +
                "number=" + number + ']';
    }

}
