package com.codexist.nearbyplaces.services.abstracts;

import com.codexist.nearbyplaces.services.dtos.NearbyPlaceDTO;

import java.io.IOException;
import java.util.List;

public interface PlaceService {
    List<NearbyPlaceDTO> getNearbyPlaces(double latitude, double longitude, int radius) throws IOException;
}
