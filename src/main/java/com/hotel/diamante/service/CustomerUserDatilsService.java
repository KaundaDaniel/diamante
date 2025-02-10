package com.hotel.diamante.service;

import com.hotel.diamante.exception.CustomerException;
import com.hotel.diamante.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDatilsService {
    private final UserRepository userRepository;

    public CustomerUserDatilsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->new CustomerException("Não foi possível achar o usuário"));

    }

}
