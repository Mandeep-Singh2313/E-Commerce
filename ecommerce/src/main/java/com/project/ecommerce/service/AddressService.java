package com.project.ecommerce.service;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddressById(Long addressId, @Valid AddressDTO address);

    String deleteAddressById(Long addressId);
}
