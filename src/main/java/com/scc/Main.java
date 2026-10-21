
package com.scc;

import com.scc.dao.Databaseconnection;
import com.scc.dao.Scenariodao;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Scenariodao dao = new Scenariodao();

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Databaseconnection.initializeDatabase();

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    recordPassage();
                    break;

                case 2:
                    viewPassageLogs();
                    break;

                case 3:
                    running = false;
                    System.out.println("\nExiting system... Thank you!");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n==============================================");
        System.out.println("       EXPRESSWAY TOLLGATE REGISTRY");
        System.out.println("==============================================");
        System.out.println("[1] Record Vehicle Passage");
        System.out.println("[2] View Passage Logs");
        System.out.println("[3] Exit System");
        System.out.println("==============================================");
    }

    private static void recordPassage() {
        System.out.println("\n--- RECORD VEHICLE PASSAGE ---");

        String plateNo;

        while (true) {
            System.out.print("Enter License Plate Number: ");
            plateNo = scanner.nextLine().trim().toUpperCase();

            if (!plateNo.isEmpty()) {
                break;
            }

            System.out.println("Plate number cannot be empty.");
        }

        String vehicleClass;

        while (true) {
            System.out.print(
                    "Enter Vehicle Class (CLASS_1 / CLASS_2 / CLASS_3): ");

            vehicleClass = scanner.nextLine().trim().toUpperCase();

            if (vehicleClass.equals("CLASS_1")
                    || vehicleClass.equals("CLASS_2")
                    || vehicleClass.equals("CLASS_3")) {
                break;
            }

            System.out.println(
                    "Invalid class. Choose CLASS_1, CLASS_2, or CLASS_3.");
        }

        double tollFee;

        while (true) {
            tollFee = readDouble("Enter Toll Fee: ");

            if (Double.isFinite(tollFee) && tollFee > 0) {
                break;
            }

            System.out.println("Toll fee must be greater than zero.");
        }

        String payment;

        while (true) {
            System.out.print("Enter Payment Method (RFID / CASH): ");
            payment = scanner.nextLine().trim().toUpperCase();

            if (payment.equals("RFID") || payment.equals("CASH")) {
                break;
            }

            System.out.println("Invalid payment. Choose RFID or CASH.");
        }

        boolean saved = dao.insertPassage(
                plateNo, vehicleClass, tollFee, payment);

        if (saved) {
            System.out.println(
                    "\nSUCCESS: Passage recorded successfully!");
        } else {
            System.out.println(
                    "\nERROR: Could not record the passage.");
        }
    }

    private static void viewPassageLogs() {
        System.out.println("\n================ EXPRESSWAY PASSAGE LOGS ================");

        List<Scenariodao.VehiclePassage> passages =
                dao.getAllPassages();

        if (passages.isEmpty()) {
            System.out.println("No passage logs found.");
            System.out.println(
                    "==========================================================");
            return;
        }

        System.out.printf("%-5s | %-15s | %-10s | %12s | %-10s%n",
                "ID", "PLATE NO", "CLASS", "TOLL FEE", "PAYMENT");

        System.out.println(
                "---------------------------------------------------------------");

        double totalToll = 0.0;

        for (Scenariodao.VehiclePassage p : passages) {
            System.out.printf("%-5d | %-15s | %-10s | %12.2f | %-10s%n",
                    p.getId(),
                    p.getPlateNo(),
                    p.getVehicleClass(),
                    p.getTollFee(),
                    p.getPayment());

            totalToll += p.getTollFee();
        }

        System.out.println(
                "---------------------------------------------------------------");
        System.out.println("Total Records Displayed: " + passages.size());
        System.out.printf("Total Toll Fees: %.2f%n", totalToll);
        System.out.println(
                "===============================================================");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println("Please enter a valid amount.");

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid amount. Example: 350.00");
            }
        }
    }
}
