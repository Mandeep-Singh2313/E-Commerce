package com.project.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min=5, message = "building name must be at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min=4, message = "city name must be at least 4 characters")
    private String cityName;

    @NotBlank
    @Size(min=2, message = "state name must be at least 2 characters")
    private String state;

    @NotBlank
    @Size(min=2, message = "country name must be at least 2 characters")
    private String country;

    @NotBlank
    @Size(min=6, message = "Pincode must be atleast 6 characters")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Address(String street, String buildingName, String cityName, String state, String country, String pinCode) {
        this.street = street;
        this.buildingName = buildingName;
        this.cityName = cityName;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
