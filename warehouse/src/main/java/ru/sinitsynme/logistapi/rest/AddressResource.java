package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.mapper.AddressMapper;
import ru.sinitsynme.logistapi.rest.dto.AddressResponseDto;
import ru.sinitsynme.logistapi.service.AddressService;

import java.util.UUID;

@Tag(name = "Управление адресами")
@RestController
@RequestMapping("/rest/api/v1/address")
public class AddressResource {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressResource(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @Operation(summary = "Получить адрес по ID")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDto> getAddress(@PathVariable UUID id) {
        Address address = addressService.getAddress(id);
        AddressResponseDto responseDto = addressMapper.addressToResponseDto(address);
        return ResponseEntity.ok(responseDto);
    }
}
