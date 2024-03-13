package org.caneroksuz.service;

import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.GetCustomerDto;
import org.caneroksuz.dto.response.OrderResponseDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.exception.OrderManagerException;
import org.caneroksuz.exception.ProductManagerException;
import org.caneroksuz.manager.IOrderManager;
import org.caneroksuz.mapper.IOrderMapper;
import org.caneroksuz.mapper.IProductMapper;
import org.caneroksuz.repository.IOrderRepository;
import org.caneroksuz.repository.IProductRepository;
import org.caneroksuz.repository.entity.Order;
import org.caneroksuz.repository.entity.Product;
import org.caneroksuz.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<Order> order = findById(orderId);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()){
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }

        boolean productExists = false;
        for (Product product1 : order.get().getProducts() ){
            if (product1.getId().equals(product.get().getId())){
                productExists = true;
                product1.setQuantity(product1.getQuantity()+product.get().getQuantity());
                break;
            }
        }

        if (!productExists){
            order.get().getProducts().add(product.get());
        }

        update(order.get());

        return "Ürün başarılı bir şekilde eklendi.";

    }

    public String removeProduct(Long orderId, Long productId) {
        Optional<Order> order = findById(orderId);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()){
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        boolean productExists = false;
        for (Product product1 : order.get().getProducts() ){
            if (product1.getId().equals(product.get().getId())){
                if(product1.getQuantity()==product.get().getQuantity()){
                    order.get().getProducts().remove(product1);
                    productExists = true;
                    break;
                }
                if(product1.getQuantity()>product.get().getQuantity()){
                    product1.setQuantity(product1.getQuantity()-product.get().getQuantity());
                    productExists = true;
                    break;
                }
            }
        }

        if (!productExists){
            throw new OrderManagerException(ErrorType.PRODUCT_NOT_FOUND_IN_ORDER);
        }

        update(order.get());

        return "Ürün başarılı bir şekilde silindi.";

    }

    public String  updateOrder(Long orderId, OrderRequestDto orderRequestDto) {

        Optional<Order> order = findById(orderId);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        Long createDate = order.get().getCreateDate();
        order = Optional.of(IOrderMapper.INSTANCE.toOrder(orderRequestDto));
        order.get().setId(orderId);
        order.get().setCreateDate(createDate);
        update(order.get());

        return "Ürün başarılı bir şekilde güncellendi.";

    }


    public List<OrderResponseDto> findAllOrder() {
        List<Order> orders = findAll();
        return orders.stream().map(x-> IOrderMapper.INSTANCE.toOrderResponseDto(x)).collect(Collectors.toList());
    }


    public OrderResponseDto findByIdOrder(Long id) {
        Optional<Order> order = findById(id);
        if (order.isEmpty()){
            throw new OrderManagerException(ErrorType.ORDER_NOT_FOUND);
        }

        return IOrderMapper.INSTANCE.toOrderResponseDto(order.get());
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
