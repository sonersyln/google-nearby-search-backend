package com.codexist.nearbyplaces.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyPlaceDTO {
    private String placeId;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
}