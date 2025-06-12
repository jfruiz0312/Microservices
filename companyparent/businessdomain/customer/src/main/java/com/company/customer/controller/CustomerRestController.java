package com.company.customer.controller;

import com.company.customer.entities.Customer;
import com.company.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author ruiz_
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> opCustomer = customerRepository.findById(id);
        if (opCustomer.isPresent()) {
            Customer newCustomer = opCustomer.get();
            newCustomer.setName(customer.getName());
            newCustomer.setAddress(customer.getAddress());
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setPhone(customer.getPhone());

            Customer customerSave = customerRepository.save(newCustomer);
            return new ResponseEntity<>(customerSave, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> post(@Valid @RequestBody Customer customer) {
        customer.setId(null);
        Customer save = customerRepository.save(customer);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
