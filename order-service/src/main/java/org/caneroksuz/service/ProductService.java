package org.caneroksuz.service;

import org.caneroksuz.dto.request.ProductRequestDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.exception.ProductManagerException;
import org.caneroksuz.mapper.IProductMapper;
import org.caneroksuz.repository.IProductRepository;
import org.caneroksuz.repository.entity.Product;
import org.caneroksuz.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService extends ServiceManager <Product, Long> {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }


    public ProductResponseDto createNewProduct(ProductRequestDto dto) {

        Product product= IProductMapper.INSTANCE.toProduct(dto);
        save(product);
        return IProductMapper.INSTANCE.toProductResponseDto(product);


    }

    public String updateProduct(Long id, ProductRequestDto dto){
        Optional<Product> product = findById(id);
        if (product.isEmpty()){
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        Long createDate = product.get().getCreateDate();
        product = Optional.of(IProductMapper.INSTANCE.toProduct(dto));
        product.get().setCreateDate(createDate);
        product.get().setId(id);
        update(product.get());

        return "Başarılı bir şekilde güncellendi.";

    }

    public List<ProductResponseDto> findAllProduct() {
        List<Product> products = findAll();
        return products.stream().map(x-> IProductMapper.INSTANCE.toProductResponseDto(x)).collect(Collectors.toList());
    }


    public ProductResponseDto findByIdProduct(Long id) {
        Optional<Product> product = findById(id);
        if (product.isEmpty()){
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }

        return IProductMapper.INSTANCE.toProductResponseDto(product.get());
    }

    public String deleteByIdProduct(Long id) {
        Optional<Product> product = findById(id);
        if (product.isEmpty()){
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        delete(product.get());
        return "Başarılı bir şekilde silindi.";
    }
}
