package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.exception.service.BadRequestException;
import ru.sinitsynme.logistapi.exception.service.NotFoundException;
import ru.sinitsynme.logistapi.mapper.WarehouseMapper;
import ru.sinitsynme.logistapi.repository.WarehouseRepository;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ORGANIZATION_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.WAREHOUSE_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessage.WAREHOUSE_ORGANIZATION_NOT_SET;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final OrganizationService organizationService;
    private final AddressService addressService;

    public WarehouseService(WarehouseRepository warehouseRepository,
                            WarehouseMapper warehouseMapper,
                            OrganizationService organizationService,
                            AddressService addressService) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.organizationService = organizationService;
        this.addressService = addressService;
    }

    public Warehouse createWarehouse(WarehouseRequestDto warehouseRequestDto) {
        long organizationId = warehouseRequestDto.getOrganizationId();

        if (organizationId == 0) {
            throw new BadRequestException(
                    WAREHOUSE_ORGANIZATION_NOT_SET,
                    new Throwable(),
                    NOT_FOUND,
                    ORGANIZATION_NOT_FOUND_CODE,
                    ExceptionSeverity.ERROR
            );
        }
        Organization organization = organizationService.getOrganizationById(organizationId);
        Address address = addressService.saveAddress(warehouseRequestDto.getAddressRequestDto());

        Warehouse warehouse = warehouseMapper.requestDtoToWarehouse(warehouseRequestDto);
        warehouse.setOrganization(organization);
        warehouse.setAddress(address);

        return warehouseRepository.save(warehouse);
    }

    public Warehouse editWarehouse(WarehouseRequestDto warehouseRequestDto, Long warehouseId) {

        Warehouse warehouseFromDb = getWarehouse(warehouseId);
        UUID addressId = warehouseFromDb.getAddress().getId();
        long organizationId = warehouseRequestDto.getOrganizationId();

        Organization organization = organizationService.getOrganizationById(organizationId);
        Address address = addressService.editAddress(warehouseRequestDto.getAddressRequestDto(), addressId);

        warehouseFromDb.setAddress(address);
        warehouseFromDb.setOrganization(organization);
        warehouseFromDb.setName(warehouseRequestDto.getName());
        warehouseFromDb.setContactNumber(warehouseRequestDto.getContactNumber());
        warehouseFromDb.setEmail(warehouseRequestDto.getEmail());

        return warehouseRepository.save(warehouseFromDb);
    }

    public Warehouse getWarehouse(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).orElseThrow(() ->
                new NotFoundException(
                        String.format("Warehouse with id = %d not found", warehouseId),
                        null,
                        NOT_FOUND,
                        WAREHOUSE_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN
                )
        );
    }

    public List<Warehouse> getPageOfWarehouse(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        Page<Warehouse> warehousePage = warehouseRepository.findAll(pageRequest);
        return warehousePage.getContent();
    }

    public void deleteWarehouse(Long warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new NotFoundException(
                    String.format("Warehouse with ID= %s not found", warehouseId),
                    new Throwable(),
                    NOT_FOUND,
                    WAREHOUSE_NOT_FOUND_CODE,
                    ExceptionSeverity.ERROR
            );
        }

        warehouseRepository.deleteById(warehouseId);
    }
}
