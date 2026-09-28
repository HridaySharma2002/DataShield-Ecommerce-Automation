package com.datashield.automation.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseValidator {

    public static Map<String, Object> getUserByEmail(String email) {
        String sql = "SELECT user_id, username, email, status, role FROM USERS WHERE email = ?";
        Map<String, Object> userMap = new HashMap<>();

        try {
            Connection conn = DBConnectionManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        userMap.put("user_id", rs.getInt("user_id"));
                        userMap.put("username", rs.getString("username"));
                        userMap.put("email", rs.getString("email"));
                        userMap.put("status", rs.getString("status"));
                        userMap.put("role", rs.getString("role"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL query for user email: " + email, e);
        }
        return userMap;
    }

    public static boolean recordOrder(String orderId, int userId, double totalAmount, String paymentStatus) {
        String sql = "INSERT INTO ORDERS (order_id, user_id, total_amount, payment_status, shipping_status) VALUES (?, ?, ?, ?, 'PROCESSING')";
        try {
            Connection conn = DBConnectionManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, orderId);
                pstmt.setInt(2, userId);
                pstmt.setDouble(3, totalAmount);
                pstmt.setString(4, paymentStatus);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting order record into DB: " + orderId, e);
        }
    }

    public static Map<String, Object> getOrderDetails(String orderId) {
        String sql = "SELECT order_id, user_id, total_amount, payment_status, shipping_status FROM ORDERS WHERE order_id = ?";
        Map<String, Object> orderMap = new HashMap<>();

        try {
            Connection conn = DBConnectionManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, orderId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        orderMap.put("order_id", rs.getString("order_id"));
                        orderMap.put("user_id", rs.getInt("user_id"));
                        orderMap.put("total_amount", rs.getDouble("total_amount"));
                        orderMap.put("payment_status", rs.getString("payment_status"));
                        orderMap.put("shipping_status", rs.getString("shipping_status"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL query for order: " + orderId, e);
        }
        return orderMap;
    }

    public static int getProductStock(String sku) {
        String sql = "SELECT stock_quantity FROM PRODUCTS WHERE sku = ?";
        try {
            Connection conn = DBConnectionManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sku);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("stock_quantity");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying product stock for SKU: " + sku, e);
        }
        return -1;
    }

    public static boolean updateProductStock(String sku, int newQuantity) {
        String sql = "UPDATE PRODUCTS SET stock_quantity = ? WHERE sku = ?";
        try {
            Connection conn = DBConnectionManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newQuantity);
                pstmt.setString(2, sku);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product stock for SKU: " + sku, e);
        }
    }
}
