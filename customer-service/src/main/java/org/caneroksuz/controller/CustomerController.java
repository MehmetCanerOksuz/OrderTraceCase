package org.caneroksuz.controller;

import lombok.RequiredArgsConstructor;
import org.caneroksuz.dto.request.CustomerRequestDto;
import org.caneroksuz.dto.response.CustomerResponseDto;
import org.caneroksuz.repository.entity.Customer;
import org.caneroksuz.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;



    @PostMapping("/save")
    public ResponseEntity<CustomerResponseDto> save(@RequestBody @Valid CustomerRequestDto dto) {
        return ResponseEntity.ok(customerService.createNewCustomer(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody @Valid CustomerRequestDto dto){
        return ResponseEntity.ok(customerService.updateCustomer(dto));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CustomerResponseDto>> findAll(){
        return ResponseEntity.ok(customerService.findAllCustomer());
    }

    @GetMapping("/find_by_id/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.findByIdCustomer(id));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<String> deleteByEmail(Long id){
        return ResponseEntity.ok(customerService.deleteByEmail(id));
    }

}
