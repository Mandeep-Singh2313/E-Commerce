package com.project.ecommerce.service;

import com.project.ecommerce.entity.Address;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.payload.AddressDTO;
import com.project.ecommerce.repo.AddressRepository;
import com.project.ecommerce.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO, Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress=addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addressList=addressRepository.findAll();
        List<AddressDTO> addressDTOList=addressList.stream().
                map(address -> modelMapper.map(address, AddressDTO.class)).
                toList();
        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId).
                orElseThrow(()->new ResourceNotFoundException("Address","id",addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addressList=user.getAddresses();
        return addressList.stream().
                map(address -> modelMapper.map(address, AddressDTO.class)).
                toList();
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO address) {
        Address prevAddress=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","id",addressId));
        prevAddress.setState(address.getState());
        prevAddress.setCityName(address.getCityName());
        prevAddress.setCountry(address.getCountry());
        prevAddress.setPinCode(address.getPinCode());
        prevAddress.setBuildingName(address.getBuildingName());
        prevAddress.setStreet(address.getStreet());
        Address updatedAddress=addressRepository.save(prevAddress);

        User user=prevAddress.getUser();
        user.getAddresses().removeIf(address1 ->
                address1.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","id",addressId));
        User user=address.getUser();
        user.getAddresses().removeIf(address1 ->
                address1.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(address);
        return "Address with id "+addressId+" deleted successfully";
    }


}
