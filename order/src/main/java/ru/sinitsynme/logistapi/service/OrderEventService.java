package ru.sinitsynme.logistapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Order;
import ru.sinitsynme.logistapi.entity.OrderEvent;
import ru.sinitsynme.logistapi.entity.enums.OrderEventType;
import ru.sinitsynme.logistapi.repository.OrderEventRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;
    private final OrderService orderService;
    private final Clock clock;

    @Autowired
    public OrderEventService(OrderEventRepository orderEventRepository, OrderService orderService, Clock clock) {
        this.orderEventRepository = orderEventRepository;
        this.orderService = orderService;
        this.clock = clock;
    }

    public void saveOrderEvent(Order order, OrderEventType eventType, UUID initiatorId) {
        saveOrderEvent(order, eventType, initiatorId, "");
    }
    public void saveOrderEvent(Order order, OrderEventType eventType, UUID initiatorId, String comment) {
        OrderEvent orderEvent = OrderEvent.builder()
                .type(eventType)
                .order(order)
                .newStatus(order.getStatus())
                .newPaymentStatus(order.getPaymentStatus())
                .initiatorId(initiatorId)
                .comment(comment)
                .registeredAt(LocalDateTime.now(clock))
                .build();

        orderEventRepository.save(orderEvent);
    }

    public List<OrderEvent> getEventsOfOrder(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        return orderEventRepository.findByOrder(order);
    }
}
