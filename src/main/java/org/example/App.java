package org.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ParkingLot parkingLot = new ParkingLot(5);

        while (true) {
            System.out.println("\n--- Parking Lot Menu ---");
            System.out.println("1. Park a Car");
            System.out.println("2. Remove a Car");
            System.out.println("3. Display Parking Status");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the car license plate: ");
                    String licensePlate = sc.next();
                    Car car = new Car(licensePlate);
                    parkingLot.parkCar(car);
                    break;
                case 2:
                    System.out.print("Enter the car license plate to remove: ");
                    String plateToRemove = sc.next();
                    parkingLot.removeCar(plateToRemove);
                    break;
                case 3:
                    parkingLot.displayParkingStatus();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

class Car {
    private String licenseplate;

    public Car(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public String getLicenseplate() {
        return licenseplate;
    }
}