package ru.sinitsynme.logistapi.rest.dto;

import java.util.UUID;

public record AddressResponseDto(UUID id, double latitude, double longitude) {
}
