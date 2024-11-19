package com.example.diets.model;

public class User {
    // Properties
    private String id;
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
    private double caloriesToday; // Calorías de hoy
    private double proteinToday; // Proteínas de hoy
    private double fatToday; // Grasas de hoy
    private double carbToday; // Carbohidratos de hoy
    private boolean isAdmin;

    // Constructor sin argumentos requerido por Firebase
    public User() {
        this.firstTime = true;
        this.caloriesToday = 0.0;
        this.proteinToday = 0.0;
        this.fatToday = 0.0;
        this.carbToday = 0.0;
    }

    // Constructor con argumentos
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
        this.caloriesToday = 0.0;
        this.proteinToday = 0.0;
        this.fatToday = 0.0;
        this.carbToday = 0.0;
    }

    public String getId() {
        // Si el ID es nulo o vacío, devuelve una cadena predeterminada para evitar errores.
        return id != null && !id.isEmpty() ? id : "Sin ID";
    }


    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }
        this.id = id;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "Sin nombre";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "Sin apellido";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email != null ? email : "Sin correo";
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
        return gender != null ? gender : "Sin género";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel != null ? activityLevel : "Sin actividad";
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal != null ? goal : "Sin meta";
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public double getCaloriesToday() {
        return caloriesToday;
    }

    public void setCaloriesToday(double caloriesToday) {
        this.caloriesToday = caloriesToday;
    }

    public double getProteinToday() {
        return proteinToday;
    }

    public void addProteinToday(double protein) {
        this.proteinToday += protein;
    }

    public double getFatToday() {
        return fatToday;
    }

    public void addFatToday(double fat) {
        this.fatToday += fat;
    }

    public double getCarbToday() {
        return carbToday;
    }

    public void addCarbToday(double carb) {
        this.carbToday += carb;
    }

    // Nuevo método restaurado: Añadir calorías
    public void addCaloriesToday(double calories) {
        this.caloriesToday += calories;
    }

    // Método para reiniciar valores diarios
    public void resetDailyValues() {
        this.caloriesToday = 0.0;
        this.proteinToday = 0.0;
        this.fatToday = 0.0;
        this.carbToday = 0.0;
    }

    // Métodos de cálculo
    public int massIndex(double weight, double height) {
        height = feetToMeters(height);
        return (int) (weight / (height * height));
    }

    private double feetToMeters(double feet) {
        return feet * 0.3048;
    }

    public double calculateCalories(double weight, double height, int age, String gender, String activityLevel, String goal) {
        double bmr;
        if ("male".equalsIgnoreCase(gender)) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        double activityFactor = switch (activityLevel != null ? activityLevel.toLowerCase() : "sedentary") {
            case "light" -> 1.375;
            case "moderate" -> 1.55;
            case "high" -> 1.725;
            case "very high" -> 1.9;
            default -> 1.2;
        };

        double get = bmr * activityFactor;
        return switch (goal != null ? goal.toLowerCase() : "maintain weight") {
            case "lose weight" -> get * 0.75;
            case "gain muscle" -> get * 1.15;
            default -> get;
        };
    }

    public double[] calculateMacros(double weight, String goal, double calories) {
        double proteinGrams = 1.8 * weight;
        double fatGrams = calories * 0.25 / 9;
        double carbGrams = (calories - (proteinGrams * 4) - (fatGrams * 9)) / 4;
        return new double[]{proteinGrams, fatGrams, carbGrams};
    }
}
