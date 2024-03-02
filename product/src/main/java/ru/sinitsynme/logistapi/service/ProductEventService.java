package ru.sinitsynme.logistapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.ProductEvent;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;
import ru.sinitsynme.logistapi.repository.ProductEventRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static security.JwtClaimsExtractor.*;

@Service
public class ProductEventService {

    private final ProductEventRepository productEventRepository;
    private final Clock clock;

    @Autowired
    public ProductEventService(ProductEventRepository productEventRepository,
                               Clock clock) {
        this.productEventRepository = productEventRepository;
        this.clock = clock;
    }

    public void saveProductEvent(UUID productId,
                                 ProductStatus newStatus,
                                 String authHeader) {

        LocalDateTime now = LocalDateTime.now(clock);

        ProductEvent productEvent = ProductEvent.builder()
                .productId(productId)
                .newStatus(newStatus)
                .registeredAt(now)
                .initiatorId(getUserIdFromHeader(authHeader))
                .build();

        productEventRepository.save(productEvent);
    }

    private UUID getUserIdFromHeader(String authHeader) {
        String token = extractTokenWithoutSignature(authHeader);
        return UUID.fromString(getUserId(token));
    }
}
