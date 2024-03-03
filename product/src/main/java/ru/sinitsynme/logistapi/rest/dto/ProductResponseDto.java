package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private ProductStatus status;
    private String name;
    private String description;
    private ProductCategoryDto productCategory;
    private ManufacturerResponseDto manufacturer;
    private double weight;
    private double volume;
    private String link;
}
