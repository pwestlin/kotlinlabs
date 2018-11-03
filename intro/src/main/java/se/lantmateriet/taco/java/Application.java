package se.lantmateriet.taco.java;

public class Application {

    public static void main(String[] args) {
        Car car = new Car("Opel", "Ascona", "1977");
        System.out.println("car = " + car);
        Car car2 = new Car("Opel", "Ascona", "1977");
        System.out.println("car = " + car);
        System.out.println("car2.equals(car) = " + car2.equals(car));
    }
}
