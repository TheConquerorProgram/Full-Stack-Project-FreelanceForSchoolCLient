package com.vigilant.vigilant_shop.controller;

import com.vigilant.vigilant_shop.model.CartItem;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private FileService fileService;

    // ADD TO CART
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable int id, HttpSession session) {
        // 1. Load all items to find the specific one
        List<ClothItem> allItems = fileService.loadMenu();
        ClothItem itemToAdd = allItems.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);

        if (itemToAdd != null) {
            // 2. Get Cart from Session (or create new)
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            // 3. Check if item exists (Update Quantity) or Add new
            boolean found = false;
            for (CartItem ci : cart) {
                if (ci.getClothItem().getId() == itemToAdd.getId()) {
                    ci.setQuantity(ci.getQuantity() + 1);
                    found = true;
                    break;
                }
            }

            if (!found) {
                cart.add(new CartItem(itemToAdd, 1));
            }

            // 4. Update Session
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    // VIEW CART
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        // Calculate Total (Polymorphism: item.getTotalPrice())
        double grandTotal = 0;
        if (cart != null) {
            for (CartItem item : cart) {
                grandTotal += item.getTotalPrice();
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("grandTotal", grandTotal);
        return "cart";
    }

    // REMOVE ITEM (Optional but helpful)
    @GetMapping("/cart/remove/{index}")
    public String removeFromCart(@PathVariable int index, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    // CLEAR CART (Used after checkout)
    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/";
    }
}
