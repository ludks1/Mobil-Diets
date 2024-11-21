package com.example.diets.model;

public class WeightRecord {
    private double weight;
    private String date;

    public WeightRecord() {
        // Constructor vacío necesario para Firebase
    }

    public WeightRecord(double weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
