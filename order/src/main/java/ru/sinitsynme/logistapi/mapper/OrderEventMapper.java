package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.OrderEvent;
import ru.sinitsynme.logistapi.rest.dto.orderEvent.OrderEventResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEventMapper {

    @Mapping(source = "order.id", target = "orderId")
    OrderEventResponseDto toResponseDto(OrderEvent orderEvent);
}
