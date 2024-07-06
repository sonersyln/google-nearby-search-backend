package com.codexist.nearbyplaces.repositories;


import com.codexist.nearbyplaces.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p WHERE p.latitude = :lat AND p.longitude = :lng AND p.radius = :rad")
    List<Place> findByLatitudeAndLongitudeAndRadius(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("rad") int radius
    );

    Optional<Place> findByPlaceId(String placeId);
}
