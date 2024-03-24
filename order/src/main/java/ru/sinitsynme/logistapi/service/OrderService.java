package ru.sinitsynme.logistapi.service;

import dto.business.warehouse.StoredProductResponseDto;
import dto.business.warehouse.WarehouseResponseDto;
import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.client.WarehouseClient;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.entity.Order;
import ru.sinitsynme.logistapi.entity.OrderItem;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;
import ru.sinitsynme.logistapi.mapper.OrderItemMapper;
import ru.sinitsynme.logistapi.mapper.OrderMapper;
import ru.sinitsynme.logistapi.repository.OrderRepository;
import ru.sinitsynme.logistapi.rest.dto.order.OrderRequestDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.*;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private final ClientOrganizationService clientOrganizationService;
    private final WarehouseClient warehouseClient;
    private final Clock clock;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        OrderRepository orderRepository,
                        AddressService addressService,
                        ClientOrganizationService clientOrganizationService,
                        WarehouseClient warehouseClient, Clock clock) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.addressService = addressService;
        this.clientOrganizationService = clientOrganizationService;
        this.warehouseClient = warehouseClient;
        this.clock = clock;
    }

    public Order saveOrder(OrderRequestDto requestDto) {
        Order order = orderMapper.fromRequestDto(requestDto);

        ClientOrganization clientOrganization = clientOrganizationService.getClientOrganization(
                requestDto.getClientOrganizationInn());
        Address address = addressService.getAddress(requestDto.getAddressId());

        WarehouseResponseDto warehouseResponseDto = warehouseClient.getWarehouse(order.getWarehouseId());

        order.setClientOrganization(clientOrganization);
        order.setActualOrderAddress(address);
        order.setStatus(OrderStatus.NEW);
        order.setPaymentStatus(PaymentStatus.PENDING_PAYMENT);
        order.setCreatedAt(LocalDateTime.now(clock));

        order.setOrderItemList(requestDto
                .getItemRequestDto().stream()
                .map(orderItemMapper::fromRequestDto)
                .collect(Collectors.toList())
        );

        checkIfItemsAreAvailableAndSetPrice(order);

        for (OrderItem item : order.getOrderItemList()
        ) {
            item.getId().setOrder(order);
        }

        reserveItemsInOrder(order);

        order = orderRepository.save(order);

        logger.info("Registered order with ID = {}", order.getId());
        return order;
    }

    public Page<Order> getPageOfWarehouseOrdersByStatuses(Long warehouseId, List<String> statusStrings, Pageable pageable) {
        List<OrderStatus> statuses = convertStatusesString(statusStrings);

        return orderRepository.findByWarehouseIdAndStatusIn(warehouseId, statuses, pageable);
    }

    public Page<Order> getPageOfClientOrdersByStatuses(String inn, List<String> statusStrings, Pageable pageable) {
        ClientOrganization clientOrganization = clientOrganizationService.getClientOrganization(inn);
        List<OrderStatus> statuses = convertStatusesString(statusStrings);

        return orderRepository.findByClientOrganizationAndStatusIn(clientOrganization, statuses, pageable);
    }

    public Page<Order> getPageOfAddressOrdersByStatuses(UUID addressId, List<String> statusStrings, Pageable pageable) {
        Address address = addressService.getAddress(addressId);
        List<OrderStatus> statuses = convertStatusesString(statusStrings);

        return orderRepository.findAllByActualOrderAddressAndStatusIn(address, statuses, pageable);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException(
                        String.format("Order with id %s wasn't found", orderId),
                        null,
                        NOT_FOUND,
                        ORDER_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN)
        );
    }

    private void checkIfItemsAreAvailableAndSetPrice(Order order) {
        for (OrderItem orderItem : order.getOrderItemList()
        ) {
            StoredProductResponseDto storedProductResponseDto = warehouseClient.getStoredProduct(
                    orderItem.getId().getProductId(),
                    order.getWarehouseId()
            );

            if (orderItem.getQuantity() > storedProductResponseDto.getAvailableForReserveQuantity()) {
                throw new BadRequestException(
                        String.format("Not enough available stored product with ID = %s to reserve at warehouse with ID = %d",
                                orderItem.getId().getProductId(),
                                order.getWarehouseId()),
                        null,
                        BAD_REQUEST,
                        NOT_ENOUGH_AVAILABLE_STORED_PRODUCT_TO_RESERVE_CODE,
                        ExceptionSeverity.WARN
                );
            }

            orderItem.setPrice(storedProductResponseDto.getPrice());
        }
    }

    private void reserveItemsInOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItemList()) {
            StoredProductResponseDto storedProductResponseDto = warehouseClient.reserveStoredProduct(
                    orderItem.getId().getProductId(),
                    order.getWarehouseId(),
                    orderItem.getQuantity()
            );
        }
    }

    private List<OrderStatus> convertStatusesString(List<String> statusStrings) {
        try {
            return statusStrings.stream().map(OrderStatus::valueOf).toList();
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(
                    "Wrong status was given",
                    null,
                    BAD_REQUEST,
                    WRONG_ORDER_STATUS_CODE,
                    ExceptionSeverity.WARN
            );

        }
    }
}
