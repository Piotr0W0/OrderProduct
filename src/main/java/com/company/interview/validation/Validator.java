package com.company.interview.validation;

import java.math.BigDecimal;

public class Validator {
    public static boolean checkId(Long id) {
        return !(id == null || id <= 0);
    }

    public static boolean checkAttributes(String name, BigDecimal price) {
        return !(name == null || price == null || price.doubleValue() < 0.0);
    }

    public static boolean checkAttributes(Long orderId, Long productId, Long quantity) {
        return !(orderId == null || orderId <= 0 || productId == null || productId <= 0 || quantity == null || quantity <= 0);
    }
}
