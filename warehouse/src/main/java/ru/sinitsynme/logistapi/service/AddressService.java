package ru.sinitsynme.logistapi.service;

import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.exception.ExceptionSeverity;
import ru.sinitsynme.logistapi.exception.service.NotFoundException;
import ru.sinitsynme.logistapi.mapper.AddressMapper;
import ru.sinitsynme.logistapi.repository.AddressRepository;
import ru.sinitsynme.logistapi.rest.dto.AddressRequestDto;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ADDRESS_NOT_FOUND_CODE;

@Service
public class AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    public AddressService(AddressMapper addressMapper, AddressRepository addressRepository) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
    }

    public Address saveAddress(AddressRequestDto requestDto) {
        Address address = addressMapper.requestDtoToAddress(requestDto);
        return addressRepository.save(address);
    }

    public Address editAddress(AddressRequestDto requestDto, UUID id) {
        Address address = getAddress(id);
        address.setLatitude(requestDto.getLatitude());
        address.setLongitude(requestDto.getLongitude());
        return addressRepository.save(address);
    }

    public Address getAddress(UUID id) {
        return addressRepository.findById(id).orElseThrow(() ->
                new NotFoundException(
                        String.format("Address with id = %s not found", id),
                        null,
                        NOT_FOUND,
                        ADDRESS_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN
                )
        );
    }
}
