package com.project.ecommerce.payload;

import com.project.ecommerce.entity.Address;
import lombok.Data;

import java.util.Map;

@Data
public class StripePaymentDto {
    private Long amount;
    private String currency;
    private String email;
    private String name;
    private Address address;
    private String description;
    private Map<String,String> metadata;
}
