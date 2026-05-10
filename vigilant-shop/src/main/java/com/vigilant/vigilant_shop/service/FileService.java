package com.vigilant.vigilant_shop.service;

import com.vigilant.vigilant_shop.model.ClothItem;
import com.vigilant.vigilant_shop.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    // 5. Java File I/O (Reading)
    public List<ClothItem> loadMenu() {
        List<ClothItem> items = new ArrayList<>();
        // We assume the file is in the project root or resources.
        // For now, we place it in the project root for easy testing.
        File file = new File("menu.txt");

        // 4. Exception handling in Java
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: ID|Name|Price|Category|ImageURL|Sizes
                String[] data = line.split("\\|");
                if (data.length >= 6) {
                    ClothItem item = new ClothItem(
                        Integer.parseInt(data[0]),
                        data[1],
                        Double.parseDouble(data[2]),
                        data[3],
                        data[4],
                        data[5]
                    );
                    items.add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
            // Return empty list if file doesn't exist yet (Fail-safe)
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in menu file: " + e.getMessage());
        }
        return items;
    }

    // 5. Java File I/O (Writing/Appending)
    public void saveOrder(String orderDetails) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("orders.txt", true))) {
            bw.write(orderDetails);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    // Admin Authentication Logic
    public User authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: username|password|role
                String[] data = line.split("\\|");
                if (data.length >= 3) {
                    if (data[0].equals(username) && data[1].equals(password)) {
                        return new User(data[0], data[1], data[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        // ... existing methods ...

    // NEW: Overwrite Menu (Used for Deletion)
    public void rewriteMenu(List<ClothItem> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("menu.txt", false))) { // false = overwrite
            for (ClothItem item : items) {
                String line = String.format("%d|%s|%.2f|%s|%s|%s",
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getCategory(),
                        item.getImageUrl(),
                        item.getAvailableSizes()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error rewriting menu: " + e.getMessage());
        }
    }

    // NEW: Overwrite Orders (Used for Deletion)
    public void rewriteOrders(List<String> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("orders.txt", false))) {
            for (String order : orders) {
                bw.write(order);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error rewriting orders: " + e.getMessage());
        }
    }

    // NEW: Load All Orders (Raw strings for now)
    public List<String> loadOrdersRaw() {
        List<String> orders = new ArrayList<>();
        File file = new File("orders.txt");
        if (!file.exists()) return orders; // Return empty if no orders yet

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                orders.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

}
