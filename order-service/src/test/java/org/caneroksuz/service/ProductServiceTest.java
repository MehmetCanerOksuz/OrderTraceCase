package org.caneroksuz.service;

import org.caneroksuz.dto.request.ProductRequestDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.exception.ProductManagerException;
import org.caneroksuz.repository.IProductRepository;
import org.caneroksuz.repository.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test

    public void testCreateNewProduct(){

        ProductRequestDto dto = ProductRequestDto.builder()
                .brand("Iphone")
                .model("14")
                .price(50000)
                .quantity(1)
        .build();

        Product product = Product.builder()
                .id(1L)
                .brand(dto.getBrand())
                .model(dto.getModel())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto responseDto = productService.createNewProduct(dto);

        assertNotNull(responseDto);
        assertEquals(product.getId(),1L);
        assertEquals(product.getBrand(),responseDto.getBrand());
        assertEquals(product.getModel(),responseDto.getModel());
        assertEquals(product.getPrice(),responseDto.getPrice());
        assertEquals(product.getQuantity(),responseDto.getQuantity());

        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void testUpdateProduct(){

        // Arrange
        ProductRequestDto dto = ProductRequestDto.builder()
                .brand("Iphone")
                .model("14")
                .price(50000)
                .quantity(1)
                .build();

        Product product = Product.builder()
                .id(1L)
                .brand("Samsung")
                .model("12")
                .price(25000)
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        String return1 = productService.updateProduct(1L, dto);

        // Assert
        assertEquals("Başarılı bir şekilde güncellendi.", return1);
        assertEquals(dto.getBrand(), product.getBrand());
        assertEquals(dto.getModel(), product.getModel());
        assertEquals(dto.getPrice(), product.getPrice());
        assertEquals(dto.getQuantity(), product.getQuantity());

        // Verifying the method calls
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }




    @Test
    public void testUpdateProductWhenProductNotFound(){

        ProductRequestDto dto = ProductRequestDto.builder()
                .brand("Iphone")
                .model("14")
                .price(50000)
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productService.updateProduct(1L,dto);

        }catch (ProductManagerException e){
            assertEquals(ErrorType.PRODUCT_NOT_FOUND,e.getErrorType());
        }

        verify(productRepository,times(1)).findById(1L);
        verify(productRepository,never()).save(any(Product.class));
    }

    @Test
    public void testFindAllProduct(){

        Product product1 = Product.builder()
                .brand("Samsung")
                .model("12")
                .price(25000)
                .quantity(1)
                .build();

        Product product2 = Product.builder()
                .brand("Iphone")
                .model("14")
                .price(50000)
                .quantity(1)
                .build();

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1,product2));

        List<ProductResponseDto> productResponseDtos = productService.findAllProduct();

        assertNotNull(productResponseDtos);
        assertEquals(2,productResponseDtos.size());

        ProductResponseDto dto1 = productResponseDtos.get(0);
        assertEquals(product1.getBrand(),dto1.getBrand());
        assertEquals(product1.getModel(),dto1.getModel());
        assertEquals(product1.getPrice(),dto1.getPrice());
        assertEquals(product1.getQuantity(),dto1.getQuantity());


        ProductResponseDto dto2 = productResponseDtos.get(1);
        assertEquals(product2.getBrand(),dto2.getBrand());
        assertEquals(product2.getModel(),dto2.getModel());
        assertEquals(product2.getPrice(),dto2.getPrice());
        assertEquals(product2.getQuantity(),dto2.getQuantity());


        verify(productRepository, times(1)).findAll();

    }


    @Test
    public void testFindByIdProduct(){

        Product product = Product.builder()
                .id(1L)
                .brand("Samsung")
                .model("12")
                .price(25000)
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDto productResponseDto = productService.findByIdProduct(1L);

        assertNotNull(productResponseDto);
        assertEquals("Samsung",productResponseDto.getBrand());
        assertEquals("12",productResponseDto.getModel());
        assertEquals(25000,productResponseDto.getPrice());
        assertEquals(1,productResponseDto.getQuantity());


        verify(productRepository,times(1)).findById(1L);
    }


    @Test
    public void testFindByIdProductWhenNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productService.findByIdProduct(1L);

        } catch (ProductManagerException e) {
            assertEquals(ErrorType.PRODUCT_NOT_FOUND, e.getErrorType());
        }

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteByIdProduct(){

        Product product = Product.builder()
                .id(1L)
                .brand("Samsung")
                .model("12")
                .price(25000)
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String return1 = productService.deleteByIdProduct(1L);

        assertNotNull(return1);
        assertEquals("Başarılı bir şekilde silindi.", return1);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);

    }

    @Test
    public void testDeleteByIdProductWhenProductNotFound(){

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            productService.deleteByIdProduct(1L);
        }catch (ProductManagerException e) {
            assertEquals(ErrorType.PRODUCT_NOT_FOUND, e.getErrorType());
        }

        verify(productRepository,times(1)).findById(1L);
        verify(productRepository,never()).delete(any(Product.class));
    }

}