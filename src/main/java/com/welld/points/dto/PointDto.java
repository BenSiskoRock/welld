package com.welld.points.dto;

import jakarta.validation.constraints.NotNull;


public record PointDto(
    @NotNull Integer x, @NotNull  Integer y) {

}
