package org.caneroksuz.manager;

import org.caneroksuz.dto.response.GetCustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${feign.customer-base-url}", decode404 = true, name = "customer-order")
public interface IOrderManager {


    @GetMapping("/find_by_id/{id}")
    ResponseEntity<GetCustomerDto> findById(@PathVariable Long id);
}
