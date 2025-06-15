package com.bay.hotels;

import com.bay.hotels.dto.HotelCreateRequest;
import com.bay.hotels.dto.HotelDetailsDto;
import com.bay.hotels.dto.HotelSummaryDto;
import com.bay.hotels.model.Address;
import com.bay.hotels.model.ArrivalTime;
import com.bay.hotels.model.Contact;
import com.bay.hotels.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllHotels() throws Exception {
        HotelSummaryDto dto = new HotelSummaryDto(1L, "Hotel 1", "desc 1", "Some Address", "+123456789");
        Mockito.when(hotelService.getAllHotelSummaries())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].name").value(dto.getName()))
                .andExpect(jsonPath("$[0].description").value(dto.getDescription()))
                .andExpect(jsonPath("$[0].address").value(dto.getAddress()))
                .andExpect(jsonPath("$[0].phone").value(dto.getPhone()));
    }

    @Test
    void testGetHotelById() throws Exception {
        Address address = new Address();
        address.setStreet("Main St");
        address.setCity("CityX");
        address.setCountry("CountryX");

        Contact contact = new Contact();
        contact.setPhone("+123456");
        contact.setEmail("hotel@example.com");

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");

        Set<String> amenities = Set.of("WiFi", "Pool");

        HotelDetailsDto dto = new HotelDetailsDto(
                1L, "Hotel 1", "BrandX", address, contact, arrivalTime, amenities
        );

        Mockito.when(hotelService.getHotelDetailsById(1L)).thenReturn(dto);

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.brand").value(dto.getBrand()))
                .andExpect(jsonPath("$.address.street").value(address.getStreet()))
                .andExpect(jsonPath("$.address.city").value(address.getCity()))
                .andExpect(jsonPath("$.address.country").value(address.getCountry()))
                .andExpect(jsonPath("$.contacts.phone").value(contact.getPhone()))
                .andExpect(jsonPath("$.contacts.email").value(contact.getEmail()))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value(arrivalTime.getCheckIn()))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value(arrivalTime.getCheckOut()))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities").value(org.hamcrest.Matchers.containsInAnyOrder("WiFi", "Pool")));
    }


    @Test
    void testSearchHotels() throws Exception {
        HotelSummaryDto dto = new HotelSummaryDto(2L, "Hotel 2", "desc 2", "Other Address", "+987654321");
        Mockito.when(hotelService.searchHotels(any(), any(), any(), any(), any()))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/hotels/search")
                        .param("name", "Hotel")
                        .param("city", "CityX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].name").value(dto.getName()))
                .andExpect(jsonPath("$[0].description").value(dto.getDescription()))
                .andExpect(jsonPath("$[0].address").value(dto.getAddress()))
                .andExpect(jsonPath("$[0].phone").value(dto.getPhone()));
    }

    @Test
    void testCreateHotel() throws Exception {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("New Hotel");
        request.setBrand("BrandNew");

        HotelSummaryDto createdDto = new HotelSummaryDto(3L, "New Hotel", "desc new", "Address new", "+111222333");
        Mockito.when(hotelService.createHotel(any(HotelCreateRequest.class))).thenReturn(createdDto);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdDto.getId()))
                .andExpect(jsonPath("$.name").value(createdDto.getName()))
                .andExpect(jsonPath("$.description").value(createdDto.getDescription()))
                .andExpect(jsonPath("$.address").value(createdDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(createdDto.getPhone()));
    }

    @Test
    void testAddAmenitiesToHotel() throws Exception {
        List<String> amenities = List.of("WiFi", "Pool");

        Address address = new Address();
        address.setStreet("Main St");
        address.setCity("CityX");
        address.setCountry("CountryX");

        Contact contact = new Contact();
        contact.setPhone("+123456");
        contact.setEmail("hotel@example.com");

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");

        HotelDetailsDto updatedDto = new HotelDetailsDto(
                1L, "Hotel 1", "BrandX", address, contact, arrivalTime, Set.copyOf(amenities)
        );

        Mockito.when(hotelService.addAmenities(eq(1L), anyList())).thenReturn(updatedDto);

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDto.getId()))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities").value(org.hamcrest.Matchers.containsInAnyOrder("WiFi", "Pool")));
    }
}
