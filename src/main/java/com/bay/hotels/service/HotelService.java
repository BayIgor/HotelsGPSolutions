package com.bay.hotels.service;

import com.bay.hotels.dto.HotelCreateRequest;
import com.bay.hotels.dto.HotelDetailsDto;
import com.bay.hotels.dto.HotelSummaryDto;
import com.bay.hotels.model.Hotel;

import java.util.List;

public interface HotelService {
    List<HotelSummaryDto> getAllHotelSummaries();

    HotelDetailsDto getHotelDetailsById(Long id);

    List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity);

    HotelSummaryDto createHotel(HotelCreateRequest request);

    HotelDetailsDto addAmenities(Long hotelId, List<String> amenities);
}