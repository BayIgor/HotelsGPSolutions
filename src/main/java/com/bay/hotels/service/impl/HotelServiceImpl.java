package com.bay.hotels.service.impl;

import com.bay.hotels.dto.HotelCreateRequest;
import com.bay.hotels.dto.HotelDetailsDto;
import com.bay.hotels.dto.HotelSummaryDto;
import com.bay.hotels.model.Hotel;
import com.bay.hotels.repository.HotelRepository;
import com.bay.hotels.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<HotelSummaryDto> getAllHotelSummaries() {
        try {
            return hotelRepository.findAll().stream()
                    .map(this::mapToSummaryDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting hotels", ex);
        }
    }

    @Override
    public HotelDetailsDto getHotelDetailsById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel with id " + id + " not found"));

        return new HotelDetailsDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getBrand(),
                hotel.getAddress(),
                hotel.getContacts(),
                hotel.getArrivalTime(),
                hotel.getAmenities()
        );
    }

    @Override
    public List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity) {
        try {
            List<Hotel> hotels = hotelRepository.findAll();

            return hotels.stream()
                    .filter(h -> name == null || h.getName().toLowerCase().contains(name.toLowerCase()))
                    .filter(h -> brand == null || h.getBrand().toLowerCase().contains(brand.toLowerCase()))
                    .filter(h -> city == null || h.getAddress().getCity().toLowerCase().contains(city.toLowerCase()))
                    .filter(h -> country == null || h.getAddress().getCountry().toLowerCase().contains(country.toLowerCase()))
                    .filter(h -> amenity == null || h.getAmenities().stream()
                            .anyMatch(a -> a.toLowerCase().contains(amenity.toLowerCase())))
                    .map(this::mapToSummaryDto)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while searching hotels", ex);
        }
    }

    @Override
    public HotelSummaryDto createHotel(HotelCreateRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotel name is required");
        }
        if (request.getAddress() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotel address is required");
        }
        if (request.getContacts() == null || request.getContacts().getPhone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotel contact phone is required");
        }

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());
        hotel.setAddress(request.getAddress());
        hotel.setContacts(request.getContacts());
        hotel.setArrivalTime(request.getArrivalTime());

        Hotel saved = hotelRepository.save(hotel);
        return mapToSummaryDto(saved);
    }

    @Override
    public HotelDetailsDto addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel with id " + hotelId + " not found"));

        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new HashSet<>());
        }
        hotel.getAmenities().addAll(amenities);

        Hotel saved = hotelRepository.save(hotel);
        return getHotelDetailsById(saved.getId());
    }

    private HotelSummaryDto mapToSummaryDto(Hotel hotel) {
        String fullAddress = String.format(
                "%d %s, %s, %s, %s",
                hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(),
                hotel.getAddress().getCity(),
                hotel.getAddress().getPostCode(),
                hotel.getAddress().getCountry()
        );
        return new HotelSummaryDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                fullAddress,
                hotel.getContacts().getPhone()
        );
    }
}