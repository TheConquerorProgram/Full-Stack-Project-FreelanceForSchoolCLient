package com.vigilant.vigilant_shop.controller;

import com.vigilant.vigilant_shop.model.User;
import com.vigilant.vigilant_shop.service.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private FileService fileService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        User user = fileService.authenticateUser(username, password);

        if (user != null && user.getRole().equals(User.Role.ADMIN)) {
            // Set session attribute
            session.setAttribute("adminUser", user);
            return "redirect:/admin-dashboard";
        } else {
            model.addAttribute("error", "Access Denied: Invalid Credentials or Clearance Level");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
