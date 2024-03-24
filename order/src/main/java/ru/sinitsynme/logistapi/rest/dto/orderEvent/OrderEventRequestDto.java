package ru.sinitsynme.logistapi.rest.dto.orderEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventRequestDto {

    private UUID id;
}
