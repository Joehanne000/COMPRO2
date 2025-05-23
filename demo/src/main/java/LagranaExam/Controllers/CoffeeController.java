package LagranaExam.Controllers;

import LagranaExam.Models.AppUser;
import LagranaExam.Models.CoffeeExam;
import LagranaExam.Services.CoffeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    @GetMapping("/home")
    public String home(Model model) {
        return "layout/main";
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, HttpSession session, Model model) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }

        model.addAttribute("coffee", coffeeService.searchCoffee(search));
        model.addAttribute("activeMenu", "home");

        return "index";
    }

    @GetMapping("/catalog")
    public String catalog(Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }
        model.addAttribute("coffee", coffeeService.getCoffeeExamList());
        model.addAttribute("activeMenu", "catalog");
        return "catalog";
    }

    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }
        coffeeService.deleteCoffeeExam(id);
        return "redirect:/";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }
        model.addAttribute("types", new String[]{"Frappe", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"});
        model.addAttribute("sizes", new String[]{"Small", "Medium", "Large"});
        model.addAttribute("roastLevels", new String[]{"Light", "Medium", "Dark"});
        model.addAttribute("brewMethods", new String[]{"Drip", "French Press", "Espresso", "Filter"});

        CoffeeExam coffeeExam = new CoffeeExam();
        model.addAttribute("coffeeExam", coffeeExam);
        model.addAttribute("activeMenu", "new");

        return "new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("coffeeExam") @Valid CoffeeExam coffeeExam, BindingResult bindingResult, @RequestParam(value = "imageFile") MultipartFile coffeePicture, Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", new String[]{"Frappe", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"});
            model.addAttribute("sizes", new String[]{"Small", "Medium", "Large"});
            model.addAttribute("roastLevels", new String[]{"Light", "Medium", "Dark"});
            model.addAttribute("brewMethods", new String[]{"Drip", "French Press", "Espresso", "Filter"});
            return "new";
        }

        coffeeExam.setId(coffeeService.getId() + 1);

        if (!coffeePicture.isEmpty()) {
            String contentType = coffeePicture.getContentType();
            if (!contentType.startsWith("image")) {
                bindingResult.rejectValue("coffeePicture", "error.coffeePicture", "The uploaded file is not an image.");
                return "new";
            }

            String path = "data/coffee_pictures/";
            File uploadFolder = new File(path);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = UUID.randomUUID() + coffeePicture.getOriginalFilename().substring(coffeePicture.getOriginalFilename().lastIndexOf("."));
            try {
                coffeePicture.transferTo(new File(uploadFolder.getAbsolutePath() + File.separator + fileName));
                coffeeExam.setCoffeePicture(fileName);
            } catch (IOException e) {
                System.out.println("File upload error: " + e.getMessage());
            }
        }

        coffeeService.addCoffee(coffeeExam);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }
        CoffeeExam c = coffeeService.getCoffee(id);
        if(c != null){
            model.addAttribute("coffeeExam", c);
            model.addAttribute("types", new String[]{"Frappe", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"});
            model.addAttribute("sizes", new String[]{"Small", "Medium", "Large"});
            model.addAttribute("roastLevels", new String[]{"Light", "Medium", "Dark"});
            model.addAttribute("brewMethods", new String[]{"Drip", "French Press", "Espresso", "Filter"});

            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("coffeeExam") @Valid CoffeeExam coffeeExam, BindingResult bindingResult, Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("coffeeExam", coffeeExam);
            model.addAttribute("types", new String[]{"Frappe", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"});
            model.addAttribute("sizes", new String[]{"Small", "Medium", "Large"});
            model.addAttribute("roastLevels", new String[]{"Light", "Medium", "Dark"});
            model.addAttribute("brewMethods", new String[]{"Drip", "French Press", "Espresso", "Filter"});
            return "edit";
        }

        CoffeeExam c = coffeeService.getCoffee(coffeeExam.getId());
        if(c != null){
            coffeeExam.setCoffeePicture(c.getCoffeePicture());
            coffeeService.updateCoffee(coffeeExam.getId(), coffeeExam);
        }
        return "redirect:/";
    }

    @GetMapping("/coffee/{id}")
    public String view(@PathVariable int id, Model model, HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/logout";
        }

        CoffeeExam c = coffeeService.getCoffee(id);
        model.addAttribute("coffeeExam", c);
        return "coffee";
    }
}
