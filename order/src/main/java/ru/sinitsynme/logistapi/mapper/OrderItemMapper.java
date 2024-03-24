package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.OrderItem;
import ru.sinitsynme.logistapi.entity.OrderItemId;
import ru.sinitsynme.logistapi.rest.dto.order.OrderItemRequestDto;
import ru.sinitsynme.logistapi.rest.dto.order.OrderItemResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    default OrderItem fromRequestDto(OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemRequestDto.getQuantity());

        OrderItemId id = new OrderItemId();
        id.setProductId(orderItemRequestDto.getProductId());
        orderItem.setId(id);

        return orderItem;
    }

    @Mapping(expression = "java(orderItem.getId().getProductId())", target = "productId")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);
}
