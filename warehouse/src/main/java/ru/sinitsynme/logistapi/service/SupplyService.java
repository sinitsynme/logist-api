package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.repository.StoredProductRepository;
import ru.sinitsynme.logistapi.rest.dto.AvailableStoredProductMinPricesDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductInfoResponseDto;
import ru.sinitsynme.logistapi.rest.dto.SupplyResponseDto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.STORED_PRODUCT_NOT_FOUND_CODE;

@Service
public class SupplyService {

    private final StoredProductRepository storedProductRepository;
    private final StoredProductMapper storedProductMapper;

    @Autowired
    public SupplyService(StoredProductRepository storedProductRepository,
                         StoredProductMapper storedProductMapper) {
        this.storedProductRepository = storedProductRepository;
        this.storedProductMapper = storedProductMapper;
    }

    public List<AvailableStoredProductMinPricesDto> getAvailableStoredProductsMinPrices(List<UUID> productIds) {
        return storedProductRepository
                .calculateMinPriceOfEachProduct(productIds)
                .stream()
                .map(it -> new AvailableStoredProductMinPricesDto(it.getProductId(), it.getMinimalPrice()))
                .toList();
    }

    public StoredProductInfoResponseDto getFullStoredProductInfo(UUID productId) {
        List<SupplyResponseDto> supplyResponseDtos = storedProductRepository
                .findByProductId(productId)
                .stream()
                .map(storedProductMapper::toSupplyResponseDto)
                .filter(it -> it.getAvailableForReserveQuantity() > 0)
                .toList();

        StoredProductInfoResponseDto responseDto = StoredProductInfoResponseDto
                .builder()
                .productId(productId)
                .supplyList(supplyResponseDtos)
                .build();

        if (!supplyResponseDtos.isEmpty()) {
            responseDto.setAvailable(true);
            responseDto.setMinimalPrice(calculateMinimalPrice(supplyResponseDtos));
        }

        return responseDto;
    }

    private BigDecimal calculateMinimalPrice(List<SupplyResponseDto> supplyResponseDtoList) {
        return supplyResponseDtoList
                .stream()
                .min(Comparator.comparing(SupplyResponseDto::getPrice))
                .get().getPrice();
    }
}
