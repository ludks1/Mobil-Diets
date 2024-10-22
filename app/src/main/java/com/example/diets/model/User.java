package com.example.diets.model;

import androidx.annotation.NonNull;

public class User {
    // properties
    private String name;
    private String email;
    private String password;
    private int age;
    private double weight;
    private double height;
    private final int type_of_user = 1; // 1 for user, 2 for admin

    // constructor
    public User(String name, String email, String password, int age, double weight, double height) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int massIndex(double weight, double height) {
        feetToMeters(height);
        return (int) (weight / (height * height));
    }

    private int feetToMeters(double feet) {
        return (int) (feet * 0.3048);
    }
}
