package com.EXAM.MIDTERM;

import java.util.List;

public class Coffee {
    private int id;
    private String name;
    private String type;
    private String size;
    private double price;
    private String roastLevel;
    private String origin;
    private boolean isDecaf;
    private int stock;
    private List<String> flavorNotes;
    private String brewMethod;

    public Coffee(int id, String name, String type, String size, double price, String roastLevel, String origin, boolean isDecaf, int stock, List<String> flavorNotes, String brewMethod) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.origin = origin;
        this.isDecaf = isDecaf;
        this.stock = stock;
        this.flavorNotes = flavorNotes;
        this.brewMethod = brewMethod;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public String getOrigin() {
        return origin;
    }

    public boolean isDecaf() {
        return isDecaf;
    }

    public int getStock() {
        return stock;
    }

    public List<String> getFlavorNotes() {
        return flavorNotes;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDecaf(boolean isDecaf) {
        this.isDecaf = isDecaf;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setFlavorNotes(List<String> flavorNotes) {
        this.flavorNotes = flavorNotes;
    }

    public void setBrewMethod(String brewMethod) {
        this.brewMethod = brewMethod;
    }
}
