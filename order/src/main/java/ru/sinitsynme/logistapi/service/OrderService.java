package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ORDER_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.WRONG_ORDER_STATUS_CODE;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private final ClientOrganizationService clientOrganizationService;
    private final Clock clock;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        OrderRepository orderRepository,
                        AddressService addressService,
                        ClientOrganizationService clientOrganizationService,
                        Clock clock) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.addressService = addressService;
        this.clientOrganizationService = clientOrganizationService;
        this.clock = clock;
    }

    public Order saveOrder(OrderRequestDto requestDto) {
        Order order = orderMapper.fromRequestDto(requestDto);

        ClientOrganization clientOrganization = clientOrganizationService.getClientOrganization(
                requestDto.getClientOrganizationInn());
        Address address = addressService.getAddress(requestDto.getAddressId());

        //TODO add integration with WarehouseService - getWarehouse
        //TODO add integration with ProductService - getProduct
        //TODO add integration with WarehouseService - reserve stored product

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

        for (OrderItem item : order.getOrderItemList()
        ) {
            item.getId().setOrder(order);
            item.setPrice(BigDecimal.TEN); // remove
        }

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
