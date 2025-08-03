package com.interview.business.mapper;

import com.interview.business.client.TrailInfoResponse;
import com.interview.business.dto.HikeDto;
import com.interview.business.dto.HikeListingDto;
import com.interview.business.dto.UpsertHikeDto;
import com.interview.data.entity.Hike;
import com.interview.data.entity.Trail;
import com.interview.data.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TrailMapper.class)
public interface HikeMapper {

    @Mapping(target = "id", ignore = true)
    Hike toHike(UpsertHikeDto upsertHikeDto, Trail trail, User user);

    HikeDto toHikeDto(Hike hike);

    HikeDto toHikeDto(Hike hike, @Context TrailInfoResponse trailInfo);

    @Mapping(target = "userDisplayName", source = "hike.user.displayName")
    @Mapping(target = "trailName", source = "hike.trail.name")
    @Mapping(target = "trailLocation", source = "hike.trail.location")
    @Mapping(target = "distanceKm", source = "hike.trail.distanceKm")
    @Mapping(target = "elevationGainMeters", source = "hike.trail.elevationGainMeters")
    @Mapping(target = "difficulty", source = "hike.trail.difficulty")
    HikeListingDto toHikeListingDto(Hike hike);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void populateFromHikeDto(@MappingTarget Hike hike, UpsertHikeDto hikeDto, Trail trail);
}
