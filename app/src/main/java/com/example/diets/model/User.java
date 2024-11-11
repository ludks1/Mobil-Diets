package com.example.diets.model;

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
}