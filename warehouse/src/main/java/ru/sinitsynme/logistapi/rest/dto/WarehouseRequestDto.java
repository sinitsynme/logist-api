package ru.sinitsynme.logistapi.rest.dto;

public record WarehouseRequestDto(String name, String contactNumber, String email, AddressRequestDto addressRequestDto, long organizationId) {
}
