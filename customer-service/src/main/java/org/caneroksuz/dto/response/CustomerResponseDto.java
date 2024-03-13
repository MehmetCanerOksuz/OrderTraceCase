package org.caneroksuz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerResponseDto {

    private String name;
    private String surname;
    private String email;
    private String address;
    private String phone;
    private boolean isAuthorized;

}
