package com.example.demo.src.cart.query;

public class CartQuery {

    public static String createCartQuery = "INSERT INTO cart(user_id, user_address_id, restaurant_id) VALUES(?, ?, ?);";

    public static String getMenuListFromCartQuery = "SELECT menu_id,\n" +
            "       price as menu_price,\n" +
            "       count as menu_count,\n" +
            "       menu_name,\n" +
            "       menu_order\n" +
            "FROM cart_menu\n" +
            "WHERE menu_id = parent_menu_id\n" +
            "  AND cart_id = ?\n" +
            "ORDER BY menu_order;";

}
