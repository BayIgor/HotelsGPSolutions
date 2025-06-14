package com.bay.hotels.dto;

import com.bay.hotels.model.Address;
import com.bay.hotels.model.Contact;
import com.bay.hotels.model.ArrivalTime;

import java.util.Set;

public class HotelDetailsDto {
    private Long id;
    private String name;
    private String brand;
    private Address address;
    private Contact contacts;
    private ArrivalTime arrivalTime;
    private Set<String> amenities;

    public HotelDetailsDto() {
    }

    public HotelDetailsDto(Long id, String name, String brand, Address address,
                           Contact contacts, ArrivalTime arrivalTime, Set<String> amenities) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.contacts = contacts;
        this.arrivalTime = arrivalTime;
        this.amenities = amenities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Contact getContacts() {
        return contacts;
    }

    public void setContacts(Contact contacts) {
        this.contacts = contacts;
    }

    public ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ArrivalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Set<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<String> amenities) {
        this.amenities = amenities;
    }
}