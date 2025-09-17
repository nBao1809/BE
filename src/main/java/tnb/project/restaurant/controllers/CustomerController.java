package tnb.project.restaurant.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tnb.project.restaurant.DTO.CreateCustomerRequest;
import tnb.project.restaurant.entities.Customer;
import tnb.project.restaurant.services.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    ResponseEntity<?> getCustomers(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(customerService.getCustomersPage(page, size));
        } else {
            return ResponseEntity.ok(customerService.getCustomers());
        }
    }

    @GetMapping("/{customerId}")
    ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        Customer customer = customerService.getCustomer(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer createdCustomer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PatchMapping("/{customerId}")
    ResponseEntity<Customer> updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(customerId, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-by-phone")
    public ResponseEntity<Customer> findByPhone(@RequestParam String phone) {
        Customer customer = customerService.findByPhone(phone);
        return ResponseEntity.ok(customer);
    }
}
