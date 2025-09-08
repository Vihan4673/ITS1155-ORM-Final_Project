package com.example.its1155ormfinal_project.bo.custom;

import lk.ijse.supermarketfx.bo.SuperBO;
import lk.ijse.supermarketfx.dto.OrderDTO;

import java.sql.SQLException;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 7/11/2025 10:22 AM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

public interface PlaceOrderBO extends SuperBO {
    boolean placeOrder(OrderDTO dto) throws SQLException;

    String getNextId() throws SQLException;
}
