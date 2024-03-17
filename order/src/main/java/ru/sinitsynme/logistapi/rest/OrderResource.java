package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Order;
import ru.sinitsynme.logistapi.mapper.OrderMapper;
import ru.sinitsynme.logistapi.rest.dto.order.*;
import ru.sinitsynme.logistapi.service.OrderService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление заказами")
@RestController
@RequestMapping("/rest/api/v1/order")
public class OrderResource {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderResource(OrderService orderService,
                         OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    @Operation(summary = "Сохранить заказ")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto orderRequestDto) {
        Order order = orderService.saveOrder(orderRequestDto);

        return ResponseEntity.ok(orderMapper.toResponseDto(order));
    }

    @GetMapping("/warehouseId/{warehouseId}")
    @Operation(summary = "Получить страницу заказов по ID склада и по статусам")
    public ResponseEntity<Page<OrderResponseDto>> getPageOfWarehouseOrdersByStatuses(
            @PathVariable Long warehouseId,
            @RequestParam List<String> statuses
    ) {
        return null;
    }

    @GetMapping("/clientId/{clientId}")
    @Operation(summary = "Получить страницу заказов по ID клиента и по статусам")
    public ResponseEntity<Page<OrderResponseDto>> getPageOfClientOrdersByStatuses(
            @PathVariable UUID clientId,
            @RequestParam List<String> statuses
    ) {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable UUID id) {
        return null;
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Изменить статус заказа")
    public ResponseEntity<?> changeOrderStatus(
            @PathVariable UUID id,
            @RequestBody OrderStatusRequestDto requestDto) {
        return null;
    }

    @PatchMapping("/{id}/payment/status")
    @Operation(summary = "Изменить статус оплаты заказа")
    public ResponseEntity<?> changeOrderPaymentStatus(
            @PathVariable UUID id,
            @RequestBody OrderStatusRequestDto requestDto) {
        return null;
    }

    @PatchMapping("/{orderId}/item/{productId}/price")
    @Operation(summary = "Изменить стоимость товара в заказе")
    public ResponseEntity<?> changeItemPrice(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody OrderItemChangePriceRequestDto priceRequestDto
    ) {
        return null;
    }

    @PatchMapping("/{orderId}/item/{productId}/add")
    @Operation(summary = "Дополнить заказ товарами по ID товара")
    public ResponseEntity<?> addItemToOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody OrderItemQuantityRequestDto quantityRequestDto
    ) {
        return null;
    }

    @PatchMapping("/{orderId}/item/{productId}/return")
    @Operation(summary = "Оформить возврат товара по ID товара")
    public ResponseEntity<?> returnItem(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody OrderItemQuantityRequestDto quantityRequestDto
    ) {
        return null;
    }
}
