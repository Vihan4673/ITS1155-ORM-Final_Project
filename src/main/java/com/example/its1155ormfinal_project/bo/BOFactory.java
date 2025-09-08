package com.example.its1155ormfinal_project.bo;

import lk.ijse.supermarketfx.bo.custom.impl.CustomerBOImpl;
import lk.ijse.supermarketfx.bo.custom.impl.ItemBOImpl;
import lk.ijse.supermarketfx.bo.custom.impl.PlaceOrderBOImpl;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 7/11/2025 10:19 AM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? (boFactory = new BOFactory()) : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <Hello extends SuperBO> Hello getBO(BOTypes boType) {
        return switch (boType) {
            case CUSTOMER -> (Hello) new CustomerBOImpl();
            case ITEM -> (Hello) new ItemBOImpl();
            case PLACE_ORDER -> (Hello) new PlaceOrderBOImpl();
        };
    }
//    public SuperBO getBO(BOTypes boType) {
//        return switch (boType) {
//            case CUSTOMER ->  new CustomerBOImpl();
//            case ITEM -> new ItemBOImpl();
//            case PLACE_ORDER -> new PlaceOrderBOImpl();
//        };
//    }
}


