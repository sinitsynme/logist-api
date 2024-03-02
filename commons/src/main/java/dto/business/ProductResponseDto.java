package dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double weight;
    private double volume;
}
