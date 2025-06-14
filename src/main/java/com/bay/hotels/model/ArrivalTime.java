package com.bay.hotels.model;

import jakarta.persistence.*;

@Embeddable
public class ArrivalTime {
    private String checkIn;
    private String checkOut;

    public ArrivalTime() {
    }

    public ArrivalTime(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}