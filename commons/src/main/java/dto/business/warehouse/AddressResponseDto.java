package dto.business.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private UUID id;
    private double latitude;
    private double longitude;
}
