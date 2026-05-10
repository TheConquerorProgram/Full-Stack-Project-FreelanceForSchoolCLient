package com.vigilant.vigilant_shop.controller;

import com.vigilant.vigilant_shop.model.ClothItem;
import com.vigilant.vigilant_shop.service.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private FileService fileService;

    // Helper to check login
    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("adminUser") != null;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        List<ClothItem> items = fileService.loadMenu();
        List<String> orders = fileService.loadOrdersRaw();

        model.addAttribute("items", items);
        model.addAttribute("orders", orders);
        return "admin-dashboard";
    }

    @PostMapping("/admin/add-item")
    public String addItem(@RequestParam String name,
                          @RequestParam double price,
                          @RequestParam String category,
                          @RequestParam String imageUrl,
                          @RequestParam String sizes,
                          HttpSession session) {

        if (!isAdmin(session)) return "redirect:/login";

        // Generate New ID
        List<ClothItem> currentItems = fileService.loadMenu();
        int newId = currentItems.stream().mapToInt(ClothItem::getId).max().orElse(0) + 1;

        ClothItem newItem = new ClothItem(newId, name, price, category, imageUrl, sizes);

        // Append to file
        String line = String.format("%d|%s|%.2f|%s|%s|%s", newId, name, price, category, imageUrl, sizes);
        fileService.saveOrder(line); // Reusing saveOrder logic just to append text

        return "redirect:/admin-dashboard";
    }

    @GetMapping("/admin/delete-item/{id}")
    public String deleteItem(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        List<ClothItem> items = fileService.loadMenu();
        items.removeIf(item -> item.getId() == id); // Remove item with matching ID

        fileService.rewriteMenu(items); // Save updated list
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/admin/delete-order/{index}")
    public String deleteOrder(@PathVariable int index, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        List<String> orders = fileService.loadOrdersRaw();
        if (index >= 0 && index < orders.size()) {
            orders.remove(index);
            fileService.rewriteOrders(orders);
        }
        return "redirect:/admin-dashboard";
    }
}
