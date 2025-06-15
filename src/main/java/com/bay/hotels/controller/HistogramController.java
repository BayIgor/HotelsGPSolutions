package com.bay.hotels.controller;

import com.bay.hotels.model.Hotel;
import com.bay.hotels.repository.HotelRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/property-view/histogram")
public class HistogramController {

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping("/{param}")
    @Operation(summary = "Получить отчет об отелях", description = "Возвращает информацию о количестве отелей по выбранному полю")
    public ResponseEntity<?> getHistogram(@PathVariable String param) {
        return switch (param.toLowerCase()) {
            case "brand" -> ResponseEntity.ok(groupByBrand());
            case "city" -> ResponseEntity.ok(groupByCity());
            case "country" -> ResponseEntity.ok(groupByCountry());
            case "amenities" -> ResponseEntity.ok(groupByAmenities());
            default -> ResponseEntity.badRequest().body("Invalid parameter: " + param);
        };
    }

    private Map<String, Long> groupByBrand() {
        return hotelRepository.findAll().stream()
                .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
    }

    private Map<String, Long> groupByCity() {
        return hotelRepository.findAll().stream()
                .collect(Collectors.groupingBy(h -> h.getAddress().getCity(), Collectors.counting()));
    }

    private Map<String, Long> groupByCountry() {
        return hotelRepository.findAll().stream()
                .collect(Collectors.groupingBy(h -> h.getAddress().getCountry(), Collectors.counting()));
    }

    private Map<String, Long> groupByAmenities() {
        return hotelRepository.findAll().stream()
                .flatMap(hotel -> hotel.getAmenities().stream())
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()));
    }
}
