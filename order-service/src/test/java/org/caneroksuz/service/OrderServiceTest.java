package org.caneroksuz.service;

import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.GetCustomerDto;
import org.caneroksuz.dto.response.OrderResponseDto;
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

import java.util.ArrayList;
import java.util.List;
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


    @Test
    public void testRemoveProductWhenOrderNotFound(){

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            orderService.removeProduct(1L,1L);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository, times(1)).findById(1L);

    }

    @Test
    public void testRemoveProductWhenProductNoFound(){

        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.empty());

        try {
            orderService.removeProduct(1L,1L);
        }catch (ProductManagerException e){
            assertEquals(ErrorType.PRODUCT_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository,times(1)).findById(1L);
        verify(productService,times(1)).findById(1L);
    }

    @Test
    public void testRemoveProductWhenProductExistInOrderWithSameQuantiy(){
        Order order= new Order();
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(1);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(1);
        order.getOrderProducts().add(orderProduct);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        String return1 = orderService.removeProduct(1L,1L);

        assertEquals("Ürün başarılı bir şekilde silindi.",return1);
        assertEquals(0,order.getOrderProducts().size());

        verify(orderRepository,times(1)).findById(1L);
        verify(productService,times(1)).findById(1L);
        verify(orderRepository,times(1)).save(order);
    }

    @Test
    public void testRemoveProductWhenProductExistInOrderWithNotSameQuantiy(){

        Order order= new Order();
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(1);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(3);
        order.getOrderProducts().add(orderProduct);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        String return1 = orderService.removeProduct(1L,1L);

        assertEquals("Ürün başarılı bir şekilde silindi.",return1);
        assertEquals(1,order.getOrderProducts().size());
        assertEquals(2,order.getOrderProducts().get(0).getQuantity());

        verify(orderRepository,times(1)).findById(1L);
        verify(productService,times(1)).findById(1L);
        verify(orderRepository,times(1)).save(order);
    }

    @Test
    public void testRemoveProductWhenProductNotFoundInOrder(){

        Order order= new Order();
        Product product = new Product();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        try {
            orderService.removeProduct(1L,1L);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.PRODUCT_NOT_FOUND_IN_ORDER,e.getErrorType());
        }

        verify(orderRepository,times(1)).findById(1L);
        verify(productService,times(1)).findById(1L);
    }

    @Test
    public void testUpdateOrder(){

        OrderRequestDto requestDto = new OrderRequestDto();
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String return1 = orderService.updateOrder(1L,requestDto);

        assertEquals("Ürün başarılı bir şekilde güncellendi.", return1);
        assertEquals(order.getId(),1L);

        verify(orderRepository,times(1)).findById(1L);
        verify(orderRepository,times(1)).save(order);


    }

    @Test
    public void testUpdateOrderWhenOrderNotFound(){

        OrderRequestDto requestDto = new OrderRequestDto();

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            orderService.updateOrder(1L,requestDto);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository,times(1)).findById(1L);
        verify(orderRepository,never()).save(any(Order.class));
    }

    @Test
    public void testFindAllOrder(){

        List<Order> orders = new ArrayList<>();


        Product product1 = new Product();
        product1.setId(1L);
        product1.setQuantity(1);
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity(2);
        Order order1 = new Order();
        order1.setId(1L);
        order1.setCustomerId(1L);
        order1.getOrderProducts().add(orderProduct1);
        orders.add(order1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setQuantity(1);
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity(2);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setCustomerId(2L);
        order2.getOrderProducts().add(orderProduct2);
        orders.add(order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponseDto> orderResponseDtos = orderService.findAllOrder();

        assertEquals(2,orderResponseDtos.size());

        OrderResponseDto orderResponseDto1 = orderResponseDtos.get(0);
        assertEquals(1L, orderResponseDto1.getCustomerId());
        assertEquals(1, orderResponseDto1.getProductQuantities().size());
        assertTrue(orderResponseDto1.getProductQuantities().containsKey(orderProduct1.getProduct()));
        assertEquals(2, orderResponseDto1.getProductQuantities().get(orderProduct1.getProduct()));

        OrderResponseDto orderResponseDto2 = orderResponseDtos.get(1);
        assertEquals(2L, orderResponseDto2.getCustomerId());
        assertEquals(1, orderResponseDto2.getProductQuantities().size());
        assertTrue(orderResponseDto2.getProductQuantities().containsKey(orderProduct2.getProduct()));
        assertEquals(2, orderResponseDto1.getProductQuantities().get(orderProduct1.getProduct()));

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testFindByIdOrderWhenOrderExists(){

        Product product = Product.builder().id(1L).build();
        OrderProduct orderProduct =OrderProduct.builder()
                .product(product)
                .quantity(2)
                .build();
        Order order = Order.builder()
                .id(1L)
                .customerId(1L)
                .build();
        order.getOrderProducts().add(orderProduct);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDto responseDto = orderService.findByIdOrder(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCustomerId());
        assertEquals(1, responseDto.getProductQuantities().size());
        assertTrue(responseDto.getProductQuantities().containsKey(orderProduct.getProduct()));
        assertEquals(2, responseDto.getProductQuantities().get(orderProduct.getProduct()));

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdOrderWhenOrderNotExist() {

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            orderService.findByIdOrder(1L);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_FOUND,e.getErrorType());
        }


        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteByIdOrderWhenOrderExists(){

        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String return1 = orderService.deleteByIdOrder(1L);

        assertEquals("Başarılı bir şekilde silindi.",return1);

        verify(orderRepository,times(1)).delete(order);
    }

    @Test
    public void testDeleteByIdOrderWhenOrderNotExists(){

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            orderService.deleteByIdOrder(1L);
        }catch (OrderManagerException e){
            assertEquals(ErrorType.ORDER_NOT_FOUND,e.getErrorType());
        }

        verify(orderRepository,never()).delete(any(Order.class));
    }

}