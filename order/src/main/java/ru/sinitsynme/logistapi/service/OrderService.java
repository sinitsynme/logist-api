package ru.sinitsynme.logistapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.*;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;
import ru.sinitsynme.logistapi.mapper.OrderItemMapper;
import ru.sinitsynme.logistapi.mapper.OrderMapper;
import ru.sinitsynme.logistapi.repository.OrderRepository;
import ru.sinitsynme.logistapi.rest.dto.order.OrderRequestDto;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private final ClientOrganizationService clientOrganizationService;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        OrderRepository orderRepository,
                        AddressService addressService,
                        ClientOrganizationService clientOrganizationService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.addressService = addressService;
        this.clientOrganizationService = clientOrganizationService;
    }

    public Order saveOrder(OrderRequestDto requestDto) {
        Order order = orderMapper.fromRequestDto(requestDto);

        ClientOrganization clientOrganization = clientOrganizationService.getClientOrganization(
                requestDto.getClientOrganizationInn());
        Address address = addressService.getAddress(requestDto.getAddressId());

        //TODO add integration with WarehouseService - getWarehouse
        //TODO add integration with ProductService - getProduct

        order.setClientOrganization(clientOrganization);
        order.setActualOrderAddress(address);
        order.setStatus(OrderStatus.NEW);
        order.setPaymentStatus(PaymentStatus.PENDING_PAYMENT);

        order.setOrderItemList(requestDto
                .getItemRequestDto().stream()
                .map(orderItemMapper::fromRequestDto)
                .collect(Collectors.toList())
        );

        for (OrderItem item: order.getOrderItemList()
             ) {
            item.getId().setOrder(order);
            item.setPrice(BigDecimal.TEN); // remove
        }

        order = orderRepository.save(order);

        logger.info("Registered order with ID = {}", order.getId());
        return order;
    }
}
