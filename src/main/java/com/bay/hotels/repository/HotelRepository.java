package com.bay.hotels.repository;

import com.bay.hotels.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByNameContainingIgnoreCase(String name);

    List<Hotel> findByBrandContainingIgnoreCase(String brand);

    List<Hotel> findByAddress_CityIgnoreCase(String city);

    List<Hotel> findByAddress_CountryIgnoreCase(String country);

    List<Hotel> findByAmenitiesIgnoreCase(String amenity);
}