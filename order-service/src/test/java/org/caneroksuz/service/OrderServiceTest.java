package org.caneroksuz.service;

import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.GetCustomerDto;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.exception.OrderManagerException;
import org.caneroksuz.exception.ProductManagerException;
import org.caneroksuz.manager.IOrderManager;
import org.caneroksuz.repository.IOrderRepository;
import org.caneroksuz.repository.entity.Order;
import org.caneroksuz.repository.entity.OrderProduct;
import org.caneroksuz.repository.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderManager orderManager;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;


    @Test
    public void testCreateNewOrderWhenCustomerIsAuthorized(){

        OrderRequestDto dto = OrderRequestDto.builder()
                .customerId(1L)
                .build();

        GetCustomerDto customerDto = GetCustomerDto.builder()
                .isAuthorized(true)
                .build();

        ResponseEntity<GetCustomerDto> customerResponse = ResponseEntity.ok(customerDto);

        when(orderManager.findById(1L)).thenReturn(customerResponse);

        String return1 = orderService.createNewOrder(dto);

        assertEquals("Sipariş başarılı bir şekilde oluşturuldu.",return1);
    }

    @Test
    public void testCreateNewOrderWhenCustomerIsNotAuthorized(){

        OrderRequestDto dto = OrderRequestDto.builder()
                .customerId(1L)
                .build();

        GetCustomerDto customerDto = GetCustomerDto.builder()
                .isAuthorized(false)
                .build();

        ResponseEntity<GetCustomerDto> customerResponse = ResponseEntity.ok(customerDto);

        when(orderManager.findById(1L)).thenReturn(customerResponse);

        try {
            orderService.createNewOrder(dto);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_CREATED,e.getErrorType());
        }
    }

    @Test
    public void testAddProductWhenOrderNotFound(){
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            orderService.addProduct(1L,1L);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository,times(1)).findById(1L);
    }


    @Test
    public void testAddProductWhenProductNotFound(){

        Order order = new Order();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.empty());

        try {
            orderService.addProduct(1L,1L);
        }catch (ProductManagerException e){
            assertEquals(ErrorType.PRODUCT_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository,times(1)).findById(1L);
        verify(productService, times(1)).findById(1L);
    }


    @Test
    public void testAddProductWhenProductExistInOrder(){

        Order order = new Order();
        order.setId(1L);
        Product product = new Product();
        product.setId(1L);
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(1);
        order.getOrderProducts().add(orderProduct);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        String return1 = orderService.addProduct(1L, 1L);

        assertEquals("Ürün başarılı bir şekilde eklendi.",return1);
        assertEquals(2,order.getOrderProducts().get(0).getQuantity());

        verify(orderRepository, times(1)).findById(1L);
        verify(productService, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testAddProductWhenProductNotExistInOrder(){

        Order order = new Order();
        order.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        String return1 = orderService.addProduct(1L, 1L);

        assertEquals("Ürün başarılı bir şekilde eklendi.", return1);
        assertEquals(1, order.getOrderProducts().size());
        assertEquals(1,order.getOrderProducts().get(0).getQuantity());

        verify(orderRepository, times(1)).findById(1L);
        verify(productService, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

}