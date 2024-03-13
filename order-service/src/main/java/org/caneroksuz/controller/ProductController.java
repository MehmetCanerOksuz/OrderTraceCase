package org.caneroksuz.controller;


import lombok.RequiredArgsConstructor;
import org.caneroksuz.dto.request.ProductRequestDto;
import org.caneroksuz.dto.response.ProductResponseDto;
import org.caneroksuz.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/save")
    public ResponseEntity<ProductResponseDto> save(@RequestBody @Valid ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createNewProduct(dto));
    }

    @PutMapping("/update" + "/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid ProductRequestDto dto){
        return ResponseEntity.ok(productService.updateProduct(id,dto));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ProductResponseDto>> findAll(){
        return ResponseEntity.ok(productService.findAllProduct());
    }

    @GetMapping("/find_by_id/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findByIdProduct(id));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<String> deleteById(Long id){
        return ResponseEntity.ok(productService.deleteByIdProduct(id));
    }
}
