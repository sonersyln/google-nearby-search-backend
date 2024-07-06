package com.codexist.nearbyplaces.services.concretes;

import com.codexist.nearbyplaces.models.Place;
import com.codexist.nearbyplaces.repositories.PlaceRepository;
import com.codexist.nearbyplaces.services.abstracts.PlaceService;
import com.codexist.nearbyplaces.services.dtos.GooglePlaceResultDTO;
import com.codexist.nearbyplaces.services.dtos.GooglePlacesApiResponseDTO;
import com.codexist.nearbyplaces.services.dtos.NearbyPlaceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceManager implements PlaceService {
    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    @Value("${google.api.key}")
    private String apiKey;

    public PlaceManager(PlaceRepository placeRepository, ObjectMapper objectMapper) {
        this.placeRepository = placeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<NearbyPlaceDTO> getNearbyPlaces(double latitude, double longitude, int radius) throws IOException {
        List<Place> existingPlaces = placeRepository.findByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);

        if (!existingPlaces.isEmpty()) {
            return convertToNearbyPlaceDTOList(existingPlaces);
        }

        List<GooglePlaceResultDTO> apiResults = fetchFromGooglePlacesAPI(latitude, longitude, radius);
        List<NearbyPlaceDTO> nearbyPlaceDTOList = new ArrayList<>();

        for (GooglePlaceResultDTO result : apiResults) {
            Place place = saveOrUpdatePlace(result, radius);
            nearbyPlaceDTOList.add(convertToNearbyPlaceDTO(place));
        }

        return nearbyPlaceDTOList;
    }

    //NOTE Google Places API'den veri çeken metot
    private List<GooglePlaceResultDTO> fetchFromGooglePlacesAPI(double latitude, double longitude, int radius) throws IOException {
        String locationParam = String.format(Locale.US, "%f,%f", latitude, longitude);
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s&radius=%d&key=%s", locationParam, radius, apiKey);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        GooglePlacesApiResponseDTO apiResponse = objectMapper.readValue(response.getBody(), GooglePlacesApiResponseDTO.class);

        return apiResponse.getResults();
    }

    //NOTE GooglePlaceResultDTO'yu Place'e çevirip veritabanına kaydeden metot
    private Place saveOrUpdatePlace(GooglePlaceResultDTO result, int radius) {
        Optional<Place> optionalPlace = placeRepository.findByPlaceId(result.getPlace_id());
        Place place = optionalPlace.orElseGet(Place::new);

        place.setPlaceId(result.getPlace_id());
        place.setName(result.getName());
        place.setLatitude(result.getGeometry().getLocation().getLat());
        place.setLongitude(result.getGeometry().getLocation().getLng());
        place.setAddress(result.getVicinity());
        place.setRadius(radius);

        return placeRepository.save(place);
    }

    //NOTE Place nesnelerini NearbyPlaceDTO'ya dönüştüren metot
    private List<NearbyPlaceDTO> convertToNearbyPlaceDTOList(List<Place> places) {
        return places.stream()
                .map(this::convertToNearbyPlaceDTO)
                .collect(Collectors.toList());
    }

    //NOTE Place nesnesini NearbyPlaceDTO'ya dönüştüren metot
    private NearbyPlaceDTO convertToNearbyPlaceDTO(Place place) {
        return new NearbyPlaceDTO(place.getPlaceId(), place.getName(), place.getLatitude(), place.getLongitude(), place.getAddress());
    }

}
