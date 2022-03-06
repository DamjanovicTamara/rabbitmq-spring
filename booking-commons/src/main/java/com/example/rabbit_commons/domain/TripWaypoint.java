package com.example.rabbit_commons.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
public class TripWaypoint extends BaseEntity {
/*
    protected TripWaypoint() {
        super();
    }*/

    private String locality;

    private String longitude;

    private String latitude;
    @ManyToOne(cascade = CascadeType.ALL)
    private Booking booking;

}
