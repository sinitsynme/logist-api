package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.entity.OrderEvent;
import ru.sinitsynme.logistapi.mapper.OrderEventMapper;
import ru.sinitsynme.logistapi.rest.dto.orderEvent.OrderEventResponseDto;
import ru.sinitsynme.logistapi.service.OrderEventService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Получение событий заказов")
@RestController
@RequestMapping("/rest/api/v1/order/event")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderEventResource {

    private final OrderEventService orderEventService;
    private final OrderEventMapper orderEventMapper;

    @Autowired
    public OrderEventResource(
            OrderEventService orderEventService,
            OrderEventMapper orderEventMapper
    ) {
        this.orderEventService = orderEventService;
        this.orderEventMapper = orderEventMapper;
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Получить список событий по заказу")
    public ResponseEntity<List<OrderEventResponseDto>> getOrderEvents(@PathVariable UUID orderId) {
        List<OrderEvent> orderEvents = orderEventService.getEventsOfOrder(orderId);

        return ResponseEntity.ok(
                orderEvents.stream().map(orderEventMapper::toResponseDto).toList()
        );
    }
}
