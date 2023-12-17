package ru.sinitsynme.logistapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @Size(min = 3, max = 100, message = "Name length should be more than 3 and less than 100")
    private String name;
    private String description;
    private Long manufacturerId;
    private String categoryCode;
    @DecimalMin(value = "0.01", message = "Price be more than 0")
    private BigDecimal price;
    @DecimalMin(value = "0.001", message = "Weight should be more than 0 kg")
    private double weight;
    @DecimalMin(value = "0.001", message = "Volume should be more than 0 dm3")
    private double volume;
    @Schema(example = "false", type = "boolean")
    private boolean isPackaged;
    @Schema(example = "1", type = "int")
    private int quantityInPackage;
}
