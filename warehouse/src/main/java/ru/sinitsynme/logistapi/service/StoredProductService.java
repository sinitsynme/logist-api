package ru.sinitsynme.logistapi.service;

import dto.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsynme.logistapi.client.ProductClient;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.entity.StoredProductId;
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.repository.StoredProductRepository;
import ru.sinitsynme.logistapi.rest.dto.StoredProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductResponseDto;

import java.util.Optional;

@Service
public class StoredProductService {

    private final Logger logger = LoggerFactory.getLogger(StoredProductService.class);
    private final StoredProductRepository storedProductRepository;
    private final WarehouseService warehouseService;
    private final StoredProductMapper storedProductMapper;
    private final ProductClient productClient;

    @Autowired
    public StoredProductService(StoredProductRepository storedProductRepository,
                                WarehouseService warehouseService,
                                StoredProductMapper storedProductMapper,
                                ProductClient productClient) {
        this.storedProductRepository = storedProductRepository;
        this.warehouseService = warehouseService;
        this.storedProductMapper = storedProductMapper;
        this.productClient = productClient;
    }

    @Transactional
    public StoredProduct addStoredProductToWarehouse(StoredProductRequestDto requestDto) {

        Warehouse warehouse = warehouseService.getWarehouse(requestDto.getWarehouseId());
        productClient.getProduct(requestDto.getProductId());

        StoredProductId possiblyExistingStoredProductId = new StoredProductId(
                requestDto.getProductId(),
                warehouse
        );

        Optional<StoredProduct> possiblyExistingStoredProduct = storedProductRepository
                .findById(possiblyExistingStoredProductId);

        if (possiblyExistingStoredProduct.isPresent()) {
            StoredProduct storedProductFromDb = possiblyExistingStoredProduct.get();
            storedProductFromDb.setQuantity(storedProductFromDb.getQuantity() + requestDto.getQuantity());
            return storedProductRepository.save(storedProductFromDb);
        }
        else {
            StoredProduct storedProduct = storedProductMapper.toEntity(requestDto);
            storedProduct.setWarehouseCode(warehouse.getStoredProductsCodeCounterString());

            warehouseService.increaseWarehouseProductCodeCounter(warehouse.getId());

            StoredProductId id = new StoredProductId(requestDto.getProductId(), warehouse);
            storedProduct.setId(id);

            return storedProductRepository.save(storedProduct);
        }
    }


}
