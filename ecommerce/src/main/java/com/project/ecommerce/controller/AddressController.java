package com.project.ecommerce.controller;

import com.project.ecommerce.entity.Address;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.payload.AddressDTO;
import com.project.ecommerce.service.AddressService;
import com.project.ecommerce.util.AuthUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody
                                                        AddressDTO addressDTO){
        User user=authUtil.loggedInUser();
        AddressDTO savedAddressDTO=addressService.createAddress(addressDTO,user);
        return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressList=addressService.getAllAddresses();
        return new ResponseEntity<List<AddressDTO>>(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO=addressService.getAddressById(addressId);
        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addressList=addressService.getUserAddresses(user);
        return new ResponseEntity<List<AddressDTO>>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDTO address) {
        AddressDTO addressDTO=addressService.updateAddressById(addressId, address);
        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId){
        String status=addressService.deleteAddressById(addressId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
