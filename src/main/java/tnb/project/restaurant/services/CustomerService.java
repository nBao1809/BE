package tnb.project.restaurant.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import tnb.project.restaurant.DTO.CreateCustomerRequest;
import tnb.project.restaurant.DTO.PageResponse;
import tnb.project.restaurant.entities.Customer;
import tnb.project.restaurant.repositorires.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }


    public Customer getCustomer(String id) {
        return customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        String name = request.getName();
        String phone = request.getPhone();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone must not be empty");
        }
        // Kiểm tra tồn tại phone
        if (customerRepo.findAll().stream().anyMatch(c -> c.getPhone().equals(phone))) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
        Customer c = new Customer();
        c.setName(name);
        c.setPhone(phone);
        return customerRepo.save(c);
    }

    public Customer updateCustomer(String id,Customer customer) {
        Customer existingCustomer = getCustomer(id);
        if (customer.getName() != null && !customer.getName().trim().isEmpty()) {
            existingCustomer.setName(customer.getName());
        }
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            existingCustomer.setPhone(customer.getPhone());
        }
        return customerRepo.save(existingCustomer);
    }

    public void deleteCustomer(String id) {
        customerRepo.deleteById(id);
    }

    public Customer findByPhone(String phone) {
        return customerRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

    }

    public PageResponse<Customer> getCustomersPage(Integer page, Integer size) {
        if (page != null && size != null) {
            Page<Customer> customerPage = customerRepo.findAll(PageRequest.of(page, size));
            return new PageResponse<>(customerPage.getContent(), customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(), customerPage.getTotalPages());
        } else {
            List<Customer> customers = customerRepo.findAll();
            return new PageResponse<>(customers, 0, customers.size(), customers.size(), 1);
        }
    }
}
