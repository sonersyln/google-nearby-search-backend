package com.codexist.nearbyplaces.controllers;

import com.codexist.nearbyplaces.services.abstracts.PlaceService;
import com.codexist.nearbyplaces.services.concretes.PlaceManager;
import com.codexist.nearbyplaces.services.dtos.NearbyPlaceDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private PlaceService placeService;

    public PlaceController(PlaceManager placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/nearby-places")
    public List<NearbyPlaceDTO> getNearbyPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int radius) throws IOException {
        return placeService.getNearbyPlaces(latitude, longitude, radius);
    }
}
