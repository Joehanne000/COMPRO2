package com.Exam.MIDTERMS;

import java.io.*;
import java.util.Scanner;

/**
 * Coffee Order Manager
 * Lets the user enter coffee type,
 * rate bitterness, aroma, and satisfaction,
 * calculates the overall rating, and saves the details into a file.
 */
public class Coffee {
    public static final double MIN_RATING = 1;
    public static final double MAX_RATING = 10;
    public static final String FILE_DIR = "target/orders";
    public static final int MAX_COFFEE_TYPES = 64;
    public static final int NUM_OF_ATTRIBUTES = 3;

    public static void main(String[] args) {
        String customerName;
        String coffeeType;
        String[] coffeeTypes = new String[MAX_COFFEE_TYPES];
        double[][] ratings = new double[MAX_COFFEE_TYPES][NUM_OF_ATTRIBUTES];

        String[] attributes = {"Bitterness", "Aroma", "Satisfaction"};

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer name: ");
        customerName = scanner.nextLine();

        StringBuilder sb = new StringBuilder();
        sb.append("Customer Name: ").append(customerName);
        sb.append("\n").append(String.format(
                        "%-15s%-10s%-10s%-10s%s\n",
                        "COFFEE TYPE", "BITTERNESS", "AROMA", "SATISFACTION", "OVERALL RATING"
                )
        );

        for (int i = 0; i < MAX_COFFEE_TYPES; i++) {
            System.out.print("Enter coffee type: ");
            coffeeTypes[i] = scanner.nextLine();
            sb.append(String.format("%-15s", coffeeTypes[i]));

            for (int j = 0; j < NUM_OF_ATTRIBUTES; j++) {
                System.out.print("\t" + attributes[j] + " (1-10): ");
                try {
                    ratings[i][j] = Double.parseDouble(scanner.nextLine());
                    if (ratings[i][j] < MIN_RATING || ratings[i][j] > MAX_RATING) {
                        throw new Exception("Error! Ratings must be between 1 and 10.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\tError! Invalid number.");
                    --j;
                    continue;
                } catch (Exception e) {
                    System.out.println("\t" + e.getMessage());
                    --j;
                    continue;
                }

                sb.append(String.format("%-10.2f", ratings[i][j]));
            }

            System.out.print("Add another coffee type (y/n): ");
            char choice = scanner.nextLine().charAt(0);

            sb.append(String.format("%.2f\n", getOverallRating(ratings[i])));

            if (Character.toLowerCase(choice) != 'y')
                break;
        }

        // PRINT RESULT
        System.out.println(sb);
        writeToFile(customerName, sb.toString());
    }

    /**
     * This function computes the overall coffee rating.
     * Formula: 30% of Bitterness + 30% of Aroma + 40% of Satisfaction
     *
     * @param attributeRatings double[] - containing ratings for bitterness, aroma, and satisfaction
     * @return double - the overall coffee rating
     */
    public static double getOverallRating(double[] attributeRatings) {
        return attributeRatings[0] * .3 + attributeRatings[1] * .3 + attributeRatings[2] * .4;
    }

    public static void writeToFile(String fileName, String data) {
        File folder = new File(FILE_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, fileName);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(data);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static void readAllFiles() {
        File folder = new File(FILE_DIR);
        File[] coffeeOrderFiles = folder.listFiles();
        if (coffeeOrderFiles != null) {
            for (File file : coffeeOrderFiles) {
                if (file.isFile()) {
                    readFile(file);
                }
            }
        }
    }

    public static void readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            System.out.println("--------------------------");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
