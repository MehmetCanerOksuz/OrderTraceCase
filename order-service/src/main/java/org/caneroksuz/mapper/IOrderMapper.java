package org.caneroksuz.mapper;


import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.OrderResponseDto;
import org.caneroksuz.repository.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderMapper {

  IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

  Order toOrder(OrderRequestDto dto);


  OrderResponseDto toOrderResponseDto(Order order);




}
