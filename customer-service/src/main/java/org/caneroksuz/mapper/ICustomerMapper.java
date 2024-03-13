package org.caneroksuz.mapper;

import org.caneroksuz.dto.request.CustomerRequestDto;
import org.caneroksuz.dto.response.CustomerResponseDto;
import org.caneroksuz.repository.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerMapper {

  ICustomerMapper INSTANCE = Mappers.getMapper(ICustomerMapper.class);

  Customer toCustomer(CustomerRequestDto dto);

  CustomerResponseDto toCustomerResponseDto(Customer customer);




}
