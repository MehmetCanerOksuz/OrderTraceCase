package org.caneroksuz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.caneroksuz.repository.entity.OrderProduct;
import org.caneroksuz.repository.entity.Product;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDto {

    private Long customerId;
    private List<OrderProduct> orderProducts;

}
