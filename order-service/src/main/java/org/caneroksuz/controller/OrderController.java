package org.caneroksuz.controller;


import lombok.RequiredArgsConstructor;
import org.caneroksuz.dto.request.OrderRequestDto;
import org.caneroksuz.dto.response.OrderResponseDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.repository.entity.Order;
import org.caneroksuz.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
        public ResponseEntity<String> save(@RequestBody @Valid OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createNewOrder(dto));
    }

    @PutMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestParam Long orderId, @RequestParam Long productId){
        return ResponseEntity.ok(orderService.addProduct(orderId,productId));
    }
    @PutMapping("/remove-product")
    public ResponseEntity<String> removeProduct(@RequestParam Long orderId, @RequestParam Long productId){
        return ResponseEntity.ok(orderService.removeProduct(orderId,productId));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateOrder(@RequestParam Long orderId, @RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.updateOrder(orderId,orderRequestDto));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderResponseDto>> findAll(){
        return ResponseEntity.ok(orderService.findAllOrder());
    }

    @GetMapping("/find_by_id/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findByIdOrder(id));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<String> deleteById(Long id){
        return ResponseEntity.ok(orderService.deleteByIdOrder(id));
    }

}
