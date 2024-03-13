package org.caneroksuz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.caneroksuz.repository.entity.Product;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDto {

    private Long customerId;
    private List<Product> products;
}
