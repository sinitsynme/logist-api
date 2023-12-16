package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private ProductCategoryDto productCategory;
    private ManufacturerResponseDto manufacturer;
    private BigDecimal price;
    private double weight;
    private double volume;
    private boolean isPackaged;
    private int quantityInPackage;
}
