package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Order;
import ru.sinitsynme.logistapi.rest.dto.order.OrderRequestDto;
import ru.sinitsynme.logistapi.rest.dto.order.OrderResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AddressMapper.class, })
public interface OrderMapper {

    Order fromRequestDto(OrderRequestDto requestDto);

    @Mapping(source = "clientOrganization.inn", target = "clientOrganizationInn")
    @Mapping(source = "documentsList", target = "documentResponseDtos")
    OrderResponseDto toResponseDto(Order order);
}
