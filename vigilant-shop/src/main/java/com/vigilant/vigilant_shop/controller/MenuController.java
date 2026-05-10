package com.vigilant.vigilant_shop.controller;

import com.vigilant.vigilant_shop.model.ClothItem;
import com.vigilant.vigilant_shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MenuController {

    @Autowired
    private FileService fileService;

    // 1. Load Home Page
    @GetMapping("/")
    public String showHome(Model model) {
        // 1. Read "menu.txt"
        List<ClothItem> items = fileService.loadMenu();
        // 2. Pass list to Model
        model.addAttribute("items", items);
        return "index"; // Renders index.html
    }

    // Load Item Details
    @GetMapping("/product/{id}")
    public String showItemDetails(@PathVariable int id, Model model) {
        List<ClothItem> items = fileService.loadMenu();
        // 1. Find ClothItem by ID (Stream logic)
        ClothItem item = items.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);

        if (item != null) {
            model.addAttribute("item", item);
            return "details"; // Renders details.html
        } else {
            return "redirect:/";
        }
    }
    // Add this method inside MenuController class
    @GetMapping("/lookbook")
    public String showLookbook() {
        return "lookbook";
    }
}
