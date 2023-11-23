package ru.sinitsynme.logistapi.rest.dto;

public record WarehouseResponseDto(Long id, String name, String contactNumber, String email, AddressResponseDto addressResponseDto, OrganizationResponseDto organizationResponseDto) {
}
