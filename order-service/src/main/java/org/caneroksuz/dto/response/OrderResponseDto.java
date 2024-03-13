package org.caneroksuz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.caneroksuz.repository.entity.Product;


import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDto {

    private Long customerId;
    private Map<Product, Integer> productQuantities;
}
