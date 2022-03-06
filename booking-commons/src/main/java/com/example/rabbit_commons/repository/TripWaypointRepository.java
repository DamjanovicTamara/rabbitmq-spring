package com.example.rabbit_commons.repository;

import com.example.rabbit_commons.domain.TripWaypoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripWaypointRepository extends JpaRepository<TripWaypoint,Long> {

}
