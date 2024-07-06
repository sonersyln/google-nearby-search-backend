package com.codexist.nearbyplaces.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceResultDTO {
    private String place_id;
    private String name;
    private Geometry geometry;
    private String vicinity;
}