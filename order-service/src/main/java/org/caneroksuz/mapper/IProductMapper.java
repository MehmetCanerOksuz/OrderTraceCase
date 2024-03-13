package org.caneroksuz.mapper;



import org.caneroksuz.dto.request.ProductRequestDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.repository.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductMapper {

  IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

  Product toProduct(ProductRequestDto dto);

  ProductResponseDto toProductResponseDto(Product product);





}
