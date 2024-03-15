package org.caneroksuz.service;

import org.caneroksuz.dto.request.CustomerRequestDto;
import org.caneroksuz.dto.response.CustomerResponseDto;
import org.caneroksuz.exception.CustomerManagerException;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.repository.ICustomerRepository;
import org.caneroksuz.repository.entity.Customer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ICustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testCreateNewCustomerWithEmailAlreadyExists() {
        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setEmail("caneroksuz@mail.cm");
        dto.setName("Caner");
        dto.setSurname("Oksuz");

        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        try {
            customerService.createNewCustomer(dto);

        } catch (CustomerManagerException e) {
            assertEquals(ErrorType.EMAIL_ALREADY_EXIST, e.getErrorType());
        }
    }

    @Test
    public void testCreateNewCustomerWithValidEmail(){
        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setEmail("caneroksuz@mail.cm");
        dto.setName("Caner");
        dto.setSurname("Oksuz");

        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        CustomerResponseDto responseDto = customerService.createNewCustomer(dto);

        assertNotNull(responseDto);
        assertFalse(responseDto.isAuthorized());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testUpdateCustomer() {
        Customer existingCustomer = Customer.builder()
                .email("caner@mail.cm")
                .name("Caner")
                .surname("Öksüz")
                .address("Tekirdağ")
                .phone("54545454")
                .isAuthorized(false)
                .build();

        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setName("Mehmet");
        dto.setSurname("Oksuz");
        dto.setAddress("Çanakkale");
        dto.setPhone("55555555");
        dto.setAuthorized(true);

        when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existingCustomer));

        String result = customerService.updateCustomer(dto);

        assertNotNull(result);
        assertEquals("Başarılı bir şekilde güncellendi.", result);


        assertEquals("Mehmet", existingCustomer.getName());
        assertEquals("Oksuz", existingCustomer.getSurname());
        assertEquals("Çanakkale", existingCustomer.getAddress());
        assertEquals("55555555", existingCustomer.getPhone());
        assertTrue(existingCustomer.isAuthorized());

        verify(customerRepository, times(1)).findByEmail(dto.getEmail());
        verify(customerRepository, times(1)).save(existingCustomer);
    }



    @Test
    public void testUpdateCustomerWhenCustomerNotFound(){

        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setEmail("asd@mail.cm");
        dto.setName("Mehmet");
        dto.setSurname("Oksuz");
        dto.setAddress("Çanakkale");
        dto.setPhone("55555555");
        dto.setAuthorized(true);

        when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        try{
            customerService.updateCustomer(dto);
        }catch (CustomerManagerException e){
            assertEquals(ErrorType.CUSTOMER_NOT_FOUND, e.getErrorType());
        }

        verify(customerRepository, times(1)).findByEmail(dto.getEmail());
        verify(customerRepository,never()).save(any(Customer.class));
    }


    @Test
    public void testFindAllCustomer(){

        Customer customer1 = Customer.builder()
                .email("caner@mail.cm")
                .name("caner")
                .surname("oksuz")
                .address("Tekirdağ")
                .phone("55555")
                .isAuthorized(false)
                .build();

        Customer customer2 = Customer.builder()
                .email("mehmet@mail.cm")
                .name("mehmet")
                .surname("oksuz")
                .address("Çanakkale")
                .phone("55555")
                .isAuthorized(false)
                .build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1,customer2));

        List<CustomerResponseDto> customerResponseDtos = customerService.findAllCustomer();

        assertNotNull(customerResponseDtos);
        assertEquals(2,customerResponseDtos.size());

        CustomerResponseDto dto1 = customerResponseDtos.get(0);
        assertEquals(customer1.getEmail(),dto1.getEmail());
        assertEquals(customer1.getName(),dto1.getName());
        assertEquals(customer1.getSurname(),dto1.getSurname());
        assertEquals(customer1.getAddress(),dto1.getAddress());
        assertEquals(customer1.getPhone(),dto1.getPhone());
        assertEquals(customer1.isAuthorized(),dto1.isAuthorized());

        CustomerResponseDto dto2 = customerResponseDtos.get(1);
        assertEquals(customer2.getEmail(),dto2.getEmail());
        assertEquals(customer2.getName(),dto2.getName());
        assertEquals(customer2.getSurname(),dto2.getSurname());
        assertEquals(customer2.getAddress(),dto2.getAddress());
        assertEquals(customer2.getPhone(),dto2.getPhone());
        assertEquals(customer2.isAuthorized(),dto2.isAuthorized());

        verify(customerRepository, times(1)).findAll();

    }

    @Test
    public void testFindByIdCustomer(){

        Customer customer = Customer.builder()
                .id(1L)
                .name("Caner")
                .surname("Öksüz")
                .email("caner@mail.cm")
                .address("Tekirdağ")
                .phone("555555")
                .isAuthorized(true)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDto customerResponseDto = customerService.findByIdCustomer(1L);

        assertNotNull(customerResponseDto);
        assertEquals("Caner",customerResponseDto.getName());
        assertEquals("Öksüz",customerResponseDto.getSurname());
        assertEquals("caner@mail.cm",customerResponseDto.getEmail());
        assertEquals("Tekirdağ",customerResponseDto.getAddress());
        assertEquals("555555",customerResponseDto.getPhone());
        assertEquals(true,customerResponseDto.isAuthorized());

        verify(customerRepository,times(1)).findById(1L);
    }

    @Test
    public void testFindByIdCustomerWhenNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            customerService.findByIdCustomer(1L);

        } catch (CustomerManagerException e) {
            assertEquals(ErrorType.CUSTOMER_NOT_FOUND, e.getErrorType());
        }

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteByIdCustomer(){

        Customer customer = Customer.builder()
                .id(1L)
                .name("Caner")
                .surname("Öksüz")
                .email("caner@mail.cm")
                .address("Tekirdağ")
                .phone("555555")
                .isAuthorized(true)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        String return1 = customerService.deleteByIdCustomer(1L);

        assertNotNull(return1);
        assertEquals("Başarılı bir şekilde silindi.", return1);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(customer);

    }

    @Test
    public void testDeleteByIdCustomerWhenCustomerNotFound(){

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            customerService.deleteByIdCustomer(1L);
        }catch (CustomerManagerException e) {
            assertEquals(ErrorType.CUSTOMER_NOT_FOUND, e.getErrorType());
        }

        verify(customerRepository,times(1)).findById(1L);
        verify(customerRepository,never()).delete(any(Customer.class));
    }




}
