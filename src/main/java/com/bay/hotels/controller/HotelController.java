package com.bay.hotels.controller;

import com.bay.hotels.dto.HotelCreateRequest;
import com.bay.hotels.dto.HotelDetailsDto;
import com.bay.hotels.dto.HotelSummaryDto;
import com.bay.hotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property-view/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    @Operation(summary = "Получить все отели", description = "Возвращает краткую информацию обо всех отелях")
    public List<HotelSummaryDto> getAllHotels() {
        return hotelService.getAllHotelSummaries();
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить отель по id", description = "Возвращает полную информацию об отеле")
    public ResponseEntity<HotelDetailsDto> getHotelById(@PathVariable Long id) {
        HotelDetailsDto dto = hotelService.getHotelDetailsById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    @Operation(summary = "Получить все отели с фильтрацией по полям", description = "Возвращает информацию об отфильтрованных отелях")
    public ResponseEntity<List<HotelSummaryDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenity
    ) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenity));
    }

    @PostMapping
    @Operation(summary = "Создать отель", description = "Возвращает информацию о созданном отеле")
    public ResponseEntity<HotelSummaryDto> createHotel(@RequestBody HotelCreateRequest request) {
        HotelSummaryDto created = hotelService.createHotel(request);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("{id}/amenities")
    @Operation(summary = "Добавить удобства к отелю", description = "Возвращает информацию об изменённом отеле")
    public ResponseEntity<HotelDetailsDto> addAmenitiesToHotel(
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        HotelDetailsDto updatedHotel = hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok(updatedHotel);
    }
}