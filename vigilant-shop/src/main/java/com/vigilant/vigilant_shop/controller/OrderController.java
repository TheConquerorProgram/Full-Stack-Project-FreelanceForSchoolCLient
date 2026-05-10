package com.vigilant.vigilant_shop.controller;

import com.vigilant.vigilant_shop.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vigilant.vigilant_shop.service.FileService;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private FileService fileService;

    // SHOW CHECKOUT PAGE
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        double grandTotal = 0;
        for (CartItem item : cart) {
            grandTotal += item.getTotalPrice();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("grandTotal", grandTotal);
        return "checkout";
    }

    // PROCESS ORDER
    @PostMapping("/process-order")
    public String processOrder(
            @RequestParam String customerName,
            @RequestParam String address,
            @RequestParam String gcashRef,
            HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null && !cart.isEmpty()) {
            double grandTotal = 0;
            StringBuilder orderItems = new StringBuilder();

            for (CartItem item : cart) {
                grandTotal += item.getTotalPrice();
                orderItems.append(item.getClothItem().getName())
                           .append(" (x").append(item.getQuantity()).append("), ");
            }

            String orderData = String.format("%s|%s|%s|%s|%s|%.2f",
                    java.time.LocalDate.now().toString(),
                    customerName,
                    address,
                    gcashRef,
                    orderItems.toString(),
                    grandTotal
            );

            fileService.saveOrder(orderData);
            session.removeAttribute("cart");
        }

        return "redirect:/success";
    }

    // ADD THIS METHOD HERE:
    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }
}
