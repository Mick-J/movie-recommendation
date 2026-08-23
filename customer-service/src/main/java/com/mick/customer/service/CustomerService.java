package com.mick.customer.service;

import com.mick.customer.dto.CustomerDetails;
import com.mick.customer.dto.GenreUpdateRequest;
import com.mick.customer.exception.CustomerNotFoundException;
import com.mick.customer.mapper.CustomerMapper;
import com.mick.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    public CustomerDetails getCustomer(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerDetails)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    // do not forget Transactional
    @Transactional
    public void updateCustomerGenre(Integer customerId, GenreUpdateRequest request) {
        var customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        customer.setFavoriteGenre(request.favoriteGenre());
        // why customer genre is not save in customer-service -> customer table ?
        eventPublisher.publishEvent(
                CustomerMapper
                        .toGenreUpdatedEvent(customerId, request.favoriteGenre())
        );
    }

}
