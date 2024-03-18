package org.caneroksuz.service;

import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.GetCustomerDto;
import org.caneroksuz.dto.response.OrderResponseDto;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.exception.OrderManagerException;
import org.caneroksuz.exception.ProductManagerException;
import org.caneroksuz.manager.IOrderManager;
import org.caneroksuz.mapper.IOrderMapper;
import org.caneroksuz.repository.IOrderRepository;
import org.caneroksuz.repository.entity.Order;
import org.caneroksuz.repository.entity.OrderProduct;
import org.caneroksuz.repository.entity.Product;
import org.caneroksuz.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class OrderService extends ServiceManager<Order, Long> {

    private final IOrderRepository orderRepository;
    private final ProductService productService;
    private final IOrderManager orderManager;


    public OrderService(IOrderRepository orderRepository, ProductService productService, IOrderManager orderManager) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderManager = orderManager;
    }

    public String createNewOrder(OrderRequestDto dto) {

        GetCustomerDto getCustomerDto = orderManager.findById(dto.getCustomerId()).getBody();

        if (getCustomerDto.isAuthorized()){
            Order order = IOrderMapper.INSTANCE.toOrder(dto);
            save(order);
            return "Sipariş başarılı bir şekilde oluşturuldu.";
        }else {
            throw new OrderManagerException(ErrorType.ORDER_NOT_CREATED);
        }

    }

    public String addProduct(Long orderId, Long productId) {
        Optional<Order> orderOptional = findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        Order order = orderOptional.get();

        Optional<Product> productOptional = productService.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();

        boolean productExists = false;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            if (orderProduct.getProduct().getId().equals(product.getId())) {
                orderProduct.setQuantity(orderProduct.getQuantity() + 1);
                productExists = true;
                break;
            }
        }

        if (!productExists) {

            OrderProduct newOrderProduct = new OrderProduct();
            newOrderProduct.setOrder(order);
            newOrderProduct.setProduct(product);
            newOrderProduct.setQuantity(1);
            order.getOrderProducts().add(newOrderProduct);
        }
        update(order);

        return "Ürün başarılı bir şekilde eklendi.";
    }

    public String removeProduct(Long orderId, Long productId) {
        Optional<Order> orderOptional = findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        Order order = orderOptional.get();

        Optional<Product> productOptional = productService.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();

        boolean productExists = false;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            if (orderProduct.getProduct().getId().equals(product.getId())) {
                if (orderProduct.getQuantity() == product.getQuantity()) {
                    order.getOrderProducts().remove(orderProduct);
                    productExists = true;
                    break;
                }
                if (orderProduct.getQuantity() > product.getQuantity()) {
                    orderProduct.setQuantity(orderProduct.getQuantity() - product.getQuantity());
                    productExists = true;
                    break;
                }
            }
        }

        if (!productExists) {
            throw new OrderManagerException(ErrorType.PRODUCT_NOT_FOUND_IN_ORDER);
        }

        update(order);

        return "Ürün başarılı bir şekilde silindi.";
    }

    public String  updateOrder(Long orderId, OrderRequestDto orderRequestDto) {

        Optional<Order> order = findById(orderId);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        Long createDate = order.get().getCreateDate();
        order.get().setCustomerId(orderRequestDto.getCustomerId());
        order.get().setOrderProducts(orderRequestDto.getOrderProducts());
        order.get().setId(orderId);
        order.get().setCreateDate(createDate);
        update(order.get());

        return "Ürün başarılı bir şekilde güncellendi.";

    }


    public List<OrderResponseDto> findAllOrder() {
        List<Order> orders = findAll();
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setCustomerId(order.getCustomerId());

            Map<Product, Integer> productQuantities = new HashMap<>();
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                productQuantities.put(orderProduct.getProduct(), orderProduct.getQuantity());
            }
            orderResponseDto.setProductQuantities(productQuantities);

            orderResponseDtos.add(orderResponseDto);
        }

        return orderResponseDtos;
    }


    public OrderResponseDto findByIdOrder(Long id) {
        Optional<Order> order = findById(id);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setCustomerId(order.get().getCustomerId());

        Map<Product, Integer> productQuantities = new HashMap<>();
        for (OrderProduct orderProduct : order.get().getOrderProducts()) {
            productQuantities.put(orderProduct.getProduct(), orderProduct.getQuantity());
        }
        orderResponseDto.setProductQuantities(productQuantities);

        return orderResponseDto;
    }

    public String deleteByIdOrder(Long id) {
        Optional<Order> order = findById(id);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }
        delete(order.get());
        return "Başarılı bir şekilde silindi.";
    }


}
