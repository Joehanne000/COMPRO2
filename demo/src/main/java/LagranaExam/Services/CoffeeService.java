package LagranaExam.Services;

import LagranaExam.Models.CoffeeExam;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoffeeService {
    private List<CoffeeExam> coffeeExamList;
    private final String FILE_NAME = "database.csv";
    private int currentMaxId = 0;

    public CoffeeService() {
        coffeeExamList = new ArrayList<>();
        readFromDisk();
    }

    public List<CoffeeExam> getCoffeeExamList() {
        return coffeeExamList;
    }

    public void deleteCoffeeExam(int id) {
        coffeeExamList.removeIf(coffeeExam -> coffeeExam.getId() == id);
        writeToDisk();
    }

    public List<CoffeeExam> searchCoffee(String keyword) {
        if (keyword.trim().isEmpty()) {
            return new ArrayList<>(coffeeExamList);  // Return all if no keyword is provided
        }

        String searchKeyword = keyword.toLowerCase();  // Convert the search keyword to lowercase once

        return coffeeExamList.stream().filter(s -> {
            return (s.getName() != null && s.getName().toLowerCase().contains(searchKeyword)) ||
                    (s.getType() != null && s.getType().toLowerCase().contains(searchKeyword)) ||
                    (s.getSize() != null && s.getSize().toLowerCase().contains(searchKeyword)) ||
                    (s.getBrewMethod() != null && s.getBrewMethod().toLowerCase().contains(searchKeyword)) ||
                    (s.getFlavorNotes() != null && s.getFlavorNotes().toLowerCase().contains(searchKeyword)) ||
                    (s.getRoastLevel() != null && s.getRoastLevel().toLowerCase().contains(searchKeyword)) ||
                    (s.getOrigin() != null && s.getOrigin().toLowerCase().contains(searchKeyword));
        }).collect(Collectors.toList());
    }

    public CoffeeExam getCoffee(int id) {
        for (CoffeeExam s : coffeeExamList) {
            if (s.getId() == id)
                return s;
        }
        return null;
    }

    public void updateCoffee(int id, CoffeeExam update) {
        for (int i = 0; i < coffeeExamList.size(); i++) {
            if (coffeeExamList.get(i).getId() == id) {
                coffeeExamList.set(i, update);
                writeToDisk();
                break;
            }
        }
    }

    public void addCoffee(CoffeeExam coffeeExam) {
        coffeeExam.setId(++currentMaxId);
        coffeeExamList.add(coffeeExam);
        writeToDisk();
    }

    public int getId() {
        if (coffeeExamList.isEmpty()) {
            return 1;
        }
        return coffeeExamList.get(coffeeExamList.size() - 1).getId() + 1;
    }

    public void writeToDisk() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            System.out.println("Writing to file");
            for (CoffeeExam s : coffeeExamList) {
                bw.write(s.getId() + ","
                        + s.getName() + ","
                        + s.getType() + ","
                        + s.getSize() + ","
                        + s.getPrice() + ","
                        + s.getRoastLevel() + ","
                        + s.getOrigin() + ","
                        + s.isDecaf() + ","
                        + s.getStock() + ","
                        + s.getBrewMethod() + ","
                        + (s.getFlavorNotes() != null ? s.getFlavorNotes().replace(",", " ") : "") + ","
                        + s.getCoffeePicture()
                );
                System.out.println(s.getCoffeePicture());
                bw.newLine();
            }
            System.out.println("Done writing to file");
        } catch (IOException e) {
            System.out.println("Woah! Error: " + e.getMessage());
        }
    }

    public void readFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("file not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length >= 11) {
                    CoffeeExam c = new CoffeeExam();
                    c.setId(Integer.parseInt(data[0]));
                    c.setName(data[1]);
                    c.setType(data[2]);
                    c.setSize(data[3]);
                    c.setPrice(Double.parseDouble(data[4]));
                    c.setRoastLevel(data[5]);
                    c.setOrigin(data[6]);
                    c.setDecaf(Boolean.parseBoolean(data[7]));
                    c.setStock(Integer.parseInt(data[8]));
                    c.setBrewMethod(data[9]);
                    c.setFlavorNotes(data[10].replace(" ", ","));
                    c.setCoffeePicture(data.length > 11 ? data[11] : "");

                    coffeeExamList.add(c);

                    if (c.getId() > currentMaxId) {
                        currentMaxId = c.getId();
                    }
                }
            }
            System.out.println("Done reading from file");
        } catch (IOException e) {
            System.out.println("Wow! Error: " + e.getMessage());
        }
    }
}
