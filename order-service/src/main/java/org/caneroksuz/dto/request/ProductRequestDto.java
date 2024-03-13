package org.caneroksuz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequestDto {

    private String brand;
    private String model;
    private int quantity;
    private double price;
}
