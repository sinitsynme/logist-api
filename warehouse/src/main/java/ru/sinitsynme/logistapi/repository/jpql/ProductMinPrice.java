package ru.sinitsynme.logistapi.repository.jpql;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductMinPrice {
    UUID getProductId();
    BigDecimal getMinimalPrice();
}
