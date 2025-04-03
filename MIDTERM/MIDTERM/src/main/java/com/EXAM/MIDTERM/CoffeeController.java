package com.EXAM.MIDTERM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller class for handling coffee-related operations in the Spring Boot application.
 */
@Controller
public class CoffeeController {


    private List<Coffee> coffeeList = new ArrayList<>();


    @Autowired
    private CoffeeService coffeeService;


    /**
     * Constructor that initializes the coffee list with sample data.
     * Also saves the initial data to CSV.
     */
    @Autowired
    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;


        // Initialize coffee list with sample data
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 3.50, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 4.50, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 5.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 4.75, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 3.25, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip"));


        // Save initial data to CSV
        this.coffeeService.saveToCsv(coffeeList);
    }


    @GetMapping("/")
    public String getCoffees(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Coffee> filteredCoffees = coffeeList;


        if (search != null && !search.isEmpty()) {
            filteredCoffees = coffeeList.stream()
                    .filter(coffee -> coffee.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }


        model.addAttribute("coffees", filteredCoffees);
        model.addAttribute("search", search);
        return "index";
    }


    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeList.removeIf(coffee -> coffee.getId() == id);


        // Automatically save changes to CSV
        coffeeService.saveToCsv(coffeeList);


        return "redirect:/";
    }


    @GetMapping("/add")
    public String addCoffee() {
        return "new";
    }


    @PostMapping("/save")
    public String saveCoffee(@RequestParam String name,
                             @RequestParam String type,
                             @RequestParam String size,
                             @RequestParam double price,
                             @RequestParam String roastLevel,
                             @RequestParam String origin,
                             @RequestParam boolean isDecaf,
                             @RequestParam int stock,
                             @RequestParam List<String> flavorNotes,
                             @RequestParam String brewMethod) {


        int newId = coffeeList.isEmpty() ? 1 : coffeeList.get(coffeeList.size() - 1).getId() + 1;
        coffeeList.add(new Coffee(newId, name, type, size, price, roastLevel, origin, isDecaf, stock, flavorNotes, brewMethod));


        coffeeService.saveToCsv(coffeeList);


        return "redirect:/";
    }


    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                model.addAttribute("coffee", coffee);
                return "edit";
            }
        }
        return "redirect:/";
    }


    @PostMapping("/update")
    public String updateCoffee(@RequestParam int id,
                               @RequestParam String name,
                               @RequestParam String type,
                               @RequestParam String size,
                               @RequestParam double price,
                               @RequestParam String roastLevel,
                               @RequestParam String origin,
                               @RequestParam boolean isDecaf,
                               @RequestParam int stock,
                               @RequestParam List<String> flavorNotes,
                               @RequestParam String brewMethod) {


        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                coffee.setName(name);
                coffee.setType(type);
                coffee.setSize(size);
                coffee.setPrice(price);
                coffee.setRoastLevel(roastLevel);
                coffee.setOrigin(origin);
                coffee.setDecaf(isDecaf);
                coffee.setStock(stock);
                coffee.setFlavorNotes(flavorNotes);
                coffee.setBrewMethod(brewMethod);
                break;
            }
        }


        coffeeService.saveToCsv(coffeeList);


        return "redirect:/";
    }
}
