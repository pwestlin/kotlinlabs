package se.lantmateriet.taco.java;

import java.util.Objects;

public class Car {

    private final String brand;
    private final String model;
    private final String year;

    public Car(String brand, String model, String year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    @Override public String toString() {
        return "Car{" +
            "brand='" + brand + '\'' +
            ", model='" + model + '\'' +
            ", year='" + year + '\'' +
            '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Car car = (Car) o;
        return brand.equals(car.brand) &&
            model.equals(car.model) &&
            year.equals(car.year);
    }

    @Override public int hashCode() {
        return Objects.hash(brand, model, year);
    }
}
