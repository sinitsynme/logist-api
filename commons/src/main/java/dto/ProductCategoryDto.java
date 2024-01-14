package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDto {
    @Size(min = 3, max = 100, message = "Category code length should be more than 3 and less than 100")
    private String categoryCode;
    @Size(min = 3, max = 100, message = "Category name length should be more than 3 and less than 100")
    private String categoryName;
}
