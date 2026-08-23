package com.mick.customer.controller;

import com.mick.customer.dto.CustomerDetails;
import com.mick.customer.dto.GenreUpdateRequest;
import com.mick.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
@CrossOrigin is enabled for local testing because each microservice runs on a different origin (port),
so the browser would otherwise block cross-origin requests. In production, this is handled by the API Gateway.
* */
@CrossOrigin
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDetails> getCustomer(@PathVariable Integer customerId){
        var customerDetails = this.customerService.getCustomer(customerId);
        return ResponseEntity.ok(customerDetails);
    }

    @PatchMapping("/{customerId}/genre")
    public ResponseEntity<Void> updateGenre(@PathVariable Integer customerId, @RequestBody GenreUpdateRequest request){
        this.customerService.updateCustomerGenre(customerId, request);
        return ResponseEntity.noContent().build(); // 204
    }

}
