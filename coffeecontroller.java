package com.coffeeshop.controller;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/coffee-orders")
public class CoffeeController {
    private static final String FILE_DIR = "target/orders";

    @PostMapping("/add")
    public String addCoffeeOrder(@RequestParam String customerName, @RequestParam String coffeeType,
                                 @RequestParam double bitterness, @RequestParam double aroma, @RequestParam double satisfaction) {
        if (!isValidRating(bitterness) || !isValidRating(aroma) || !isValidRating(satisfaction)) {
            return "Error! Ratings must be between 1 and 10.";
        }

        double overallRating = calculateOverallRating(bitterness, aroma, satisfaction);
        String orderData = String.format("Customer: %s\nCoffee Type: %s\nBitterness: %.2f\nAroma: %.2f\nSatisfaction: %.2f\nOverall Rating: %.2f\n",
                customerName, coffeeType, bitterness, aroma, satisfaction, overallRating);

        saveOrderToFile(customerName, orderData);
        return "Order saved successfully!";
    }

    @GetMapping("/all")
    public List<String> getAllOrders() {
        File folder = new File(FILE_DIR);
        File[] orderFiles = folder.listFiles();
        List<String> orders = new ArrayList<>();

        if (orderFiles != null) {
            for (File file : orderFiles) {
                orders.add(readOrderFromFile(file));
            }
        }
        return orders;
    }

    private void saveOrderToFile(String fileName, String data) {
        File folder = new File(FILE_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try (FileWriter fw = new FileWriter(new File(folder, fileName))) {
            fw.write(data);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private String readOrderFromFile(File file) {
        StringBuilder orderData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                orderData.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return orderData.toString();
    }

    private boolean isValidRating(double rating) {
        return rating >= 1 && rating <= 10;
    }

    private double calculateOverallRating(double bitterness, double aroma, double satisfaction) {
        return bitterness * 0.3 + aroma * 0.3 + satisfaction * 0.4;
    }
}
