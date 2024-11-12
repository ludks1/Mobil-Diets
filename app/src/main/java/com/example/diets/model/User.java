package com.example.diets.model;

import android.widget.Toast;

public class User {
    // properties
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;
    private double weight;
    private double height;
    private String gender;
    private String activityLevel;
    private String goal;
    private final int type_of_user = 1; // 1 for user, 2 for admin
    private boolean firstTime; // Nuevo campo para indicar si es la primera vez

    // Constructor sin argumentos requerido por Firebase
    public User() {
    }

    // constructor
    public User(String firstName, String lastName, String email, String password, int age, double weight, double height, String gender, String activityLevel, String goal) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.firstTime = true; // Por defecto, es la primera vez
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    // methods
    public int massIndex(double weight, double height) {
        height = feetToMeters(height);
        return (int) (weight / (height * height));
    }

    private double feetToMeters(double feet) {
        return feet * 0.3048;
    }

    public double calculateCalories(double weight, double height, int age, String gender, String activityLevel, String goal) {
        // Calcular TMB usando la fórmula de Mifflin-St Jeor
        double bmr;
        if (gender != null && gender.equalsIgnoreCase("male")) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        // Ajustar TMB según el nivel de actividad
        double activityFactor;
        if (activityLevel != null) {
            switch (activityLevel.toLowerCase()) {
                case "sedentary":
                    activityFactor = 1.2;
                    break;
                case "light":
                    activityFactor = 1.375;
                    break;
                case "moderate":
                    activityFactor = 1.55;
                    break;
                case "high":
                    activityFactor = 1.725;
                    break;
                case "very high":
                    activityFactor = 1.9;
                    break;
                default:
                    activityFactor = 1.2;
                    break;
            }
        } else {
            activityFactor = 1.2; // Valor por defecto
        }
        double get = bmr * activityFactor;

        // Ajustar GET según el objetivo
        double calories;
        if (goal != null) {
            switch (goal.toLowerCase()) {
                case "lose weight":
                    calories = get * 0.75; // 25% déficit
                    break;
                case "gain muscle":
                    calories = get * 1.15; // 15% superávit
                    break;
                default:
                    calories = get; // mantener peso
                    break;
            }
        } else {
            calories = get; // Valor por defecto
        }

        return calories;
    }

    public double[] calculateMacros(double weight, String goal, double calories) {
        double proteinGrams, fatGrams, carbGrams;
        if (goal != null) {
            switch (goal.toLowerCase()) {
                case "lose weight":
                    proteinGrams = 2.3 * weight; // Usando el extremo inferior del rango
                    fatGrams = calories * 0.25 / 9; // 25% de las calorías totales
                    break;
                case "gain muscle":
                    proteinGrams = 1.8 * weight; // Usando el extremo inferior del rango
                    fatGrams = calories * 0.25 / 9; // 25% de las calorías totales
                    break;
                default:
                    proteinGrams = 1.8 * weight; // Usando el extremo inferior del rango
                    fatGrams = calories * 0.25 / 9; // 25% de las calorías totales
                    break;
            }
        } else {
            proteinGrams = 1.8 * weight; // Valor por defecto
            fatGrams = calories * 0.25 / 9; // 25% de las calorías totales
        }
        carbGrams = (calories - (proteinGrams * 4) - (fatGrams * 9)) / 4;

        return new double[]{proteinGrams, fatGrams, carbGrams};
    }

}