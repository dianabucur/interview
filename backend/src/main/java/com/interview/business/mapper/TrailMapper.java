package com.interview.business.mapper;

import com.interview.business.client.TrailInfoResponse;
import com.interview.business.dto.TrailDto;
import com.interview.data.entity.Trail;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrailMapper {

    @Mapping(target = "latitude", expression = "java(trailInfo.getLatitude())")
    @Mapping(target = "longitude", expression = "java(trailInfo.getLongitude())")
    @Mapping(target = "placeRank", expression = "java(trailInfo.getPlaceRank())")
    TrailDto toTrailDto(Trail trail, @Context TrailInfoResponse trailInfo);
}
