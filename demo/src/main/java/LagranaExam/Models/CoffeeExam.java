package LagranaExam.Models;

import jakarta.validation.constraints.*;

public class CoffeeExam {
    private int id;

    @Size(min = 2, max = 50, message = "Name must be greater than 2 letters and lesser than 50 letters")
    private String name;

    @NotBlank(message = "Need the coffee's type")
    private String type;

    @NotBlank(message = "Need the coffee's size")
    private String size;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private double price;

    @NotBlank(message = "Need coffee's roast level")
    private String roastLevel;

    @Size(max = 100)
    private String origin;

    private boolean isDecaf;

    @NotNull(message = "Need the coffee's stock")
    @Min(value = 0, message = "Stock must be a positive number")
    private int stock;

    private String flavorNotes;

    @NotBlank(message = "Need the coffee's brew method")
    private String brewMethod;

    private String coffeePicture;

    public CoffeeExam(int id, String name, String type, String size, double price, String roastLevel, String origin,
                      boolean isDecaf, int stock, String flavorNotes, String brewMethod, String coffeePicture) {
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
        this.coffeePicture = coffeePicture;
    }

    public CoffeeExam() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getRoastLevel() { return roastLevel; }
    public void setRoastLevel(String roastLevel) { this.roastLevel = roastLevel; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public boolean isDecaf() { return isDecaf; }
    public void setDecaf(boolean isDecaf) { this.isDecaf = isDecaf; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getFlavorNotes() { return flavorNotes; }
    public void setFlavorNotes(String flavorNotes) { this.flavorNotes = flavorNotes; }

    public String getBrewMethod() { return brewMethod; }
    public void setBrewMethod(String brewMethod) { this.brewMethod = brewMethod; }

    public String getCoffeePicture() { return coffeePicture; }
    public void setCoffeePicture(String coffeePicture) { this.coffeePicture = coffeePicture; }
}
