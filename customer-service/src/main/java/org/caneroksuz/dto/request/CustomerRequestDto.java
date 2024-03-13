package org.caneroksuz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerRequestDto {

    @NotBlank(message = "İsim alanı doldurulmalıdır.")
    private String name;
    @NotBlank(message = "Soyisim alanı doldurulmalıdır.")
    private String surname;
    @NotBlank(message = "Email alanı doldurulmalıdır.")
    private String email;
    private String address;
    private String phone;
    private boolean isAuthorized;

}
