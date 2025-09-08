package tnb.project.restaurant.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tnb.project.restaurant.entities.Customer;
import tnb.project.restaurant.repositorires.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }


    public Customer getCustomer(String id) {
        return customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not exits"));
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    public Customer createCustomer(Customer c) {
        if (c.getName() == null || c.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty");
        }
        if (c.getPhone() == null || c.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone must not be empty");
        }
        return customerRepo.save(c);
    }

    public Customer updateCustomer(String id, Customer customer) {
        Customer existingCustomer = getCustomer(id);
        if (customer.getName() != null && !customer.getName().trim().isEmpty()) {
            existingCustomer.setName(customer.getName());
        }
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            existingCustomer.setPhone(customer.getPhone());
        }
        if (customer.getLoyaltyPoints() != null) {
            existingCustomer.setLoyaltyPoints(customer.getLoyaltyPoints());
        }
        return customerRepo.save(existingCustomer);
    }

    public void deleteCustomer(String id) {
        customerRepo.deleteById(id);
    }

    public Customer findByPhone(String phone) {
        return customerRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer with phone " + phone + " not found"));

    }
}
