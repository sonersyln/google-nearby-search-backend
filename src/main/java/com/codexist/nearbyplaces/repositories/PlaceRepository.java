package com.codexist.nearbyplaces.repositories;


import com.codexist.nearbyplaces.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
