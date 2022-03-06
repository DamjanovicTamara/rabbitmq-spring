package com.example.rabbit_commons.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)

public class TripWaypoint extends BaseEntity {
    protected TripWaypoint() {
        super();
    }

    private String locality;

    private String longitude;

    private String latitude;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    @JsonIgnoreProperties("tripWaypointList")
    private Booking booking;


    @Override
    public String toString() {
        return "TripWaypoint{" +
                "locality='" + locality + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
