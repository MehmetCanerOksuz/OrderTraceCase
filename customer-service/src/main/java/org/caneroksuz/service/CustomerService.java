package org.caneroksuz.service;

import org.caneroksuz.dto.request.CustomerRequestDto;
import org.caneroksuz.dto.response.CustomerResponseDto;
import org.caneroksuz.exception.CustomerManagerException;
import org.caneroksuz.exception.ErrorType;
import org.caneroksuz.mapper.ICustomerMapper;
import org.caneroksuz.repository.ICustomerRepository;
import org.caneroksuz.repository.entity.Customer;
import org.caneroksuz.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerService extends ServiceManager<Customer,Long> {

    private final ICustomerRepository customerRepository;

    public CustomerService(ICustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDto createNewCustomer(CustomerRequestDto dto) {

            if (customerRepository.existsByEmail(dto.getEmail())){
                throw new CustomerManagerException(ErrorType.EMAIL_ALREADY_EXIST);
            }
            Customer customer= ICustomerMapper.INSTANCE.toCustomer(dto);
            customer.setAuthorized(false);
            save(customer);
            return ICustomerMapper.INSTANCE.toCustomerResponseDto(customer);


    }

    public String updateCustomer(CustomerRequestDto dto){
        Optional<Customer> customer = customerRepository.findByEmail(dto.getEmail());
        if (customer.isEmpty()){
            throw new CustomerManagerException(ErrorType.CUSTOMER_NOT_FOUND);
        }
        customer.get().setName(dto.getName());
        customer.get().setSurname(dto.getSurname());
        customer.get().setAddress(dto.getAddress());
        customer.get().setPhone(dto.getPhone());
        customer.get().setAuthorized(dto.isAuthorized());
        update(customer.get());

        return "Başarılı bir şekilde güncellendi.";

    }

    public List<CustomerResponseDto> findAllCustomer() {
        List<Customer> customers = findAll();
        return customers.stream().map(x-> ICustomerMapper.INSTANCE.toCustomerResponseDto(x)).collect(Collectors.toList());
    }


    public CustomerResponseDto findByIdCustomer(Long id) {
        Optional<Customer> customer = findById(id);
        if (customer.isEmpty()){
            throw new CustomerManagerException(ErrorType.CUSTOMER_NOT_FOUND);
        }
        System.out.println(customer.get());
        return CustomerResponseDto.builder().address(customer.get().getAddress())
                .email(customer.get().getEmail())
                .isAuthorized(customer.get().isAuthorized())
                .phone(customer.get().getPhone())
                .name(customer.get().getName())
                .surname(customer.get().getSurname())
        .build();
    }

    public String deleteByIdCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()){
            throw new CustomerManagerException(ErrorType.CUSTOMER_NOT_FOUND);
        }
        delete(customer.get());
        return "Başarılı bir şekilde silindi.";
    }
}
