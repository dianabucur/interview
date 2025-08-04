package com.interview.business;

import com.interview.business.client.TrailInfoResponse;
import com.interview.business.dto.HikeDto;
import com.interview.business.dto.HikeListingDto;
import com.interview.business.dto.TrailDto;
import com.interview.business.dto.UpsertHikeDto;
import com.interview.business.exception.EntityNotFoundException;
import com.interview.business.exception.InvalidParameterException;
import com.interview.business.mapper.HikeMapper;
import com.interview.business.util.SpecificationFactory;
import com.interview.data.entity.Hike;
import com.interview.data.entity.Trail;
import com.interview.data.entity.User;
import com.interview.data.enumeration.TrailDifficulty;
import com.interview.data.repository.HikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HikeServiceTest {

    @Mock
    private HikeRepository hikeRepository;

    @Mock
    private TrailService trailService;

    @Mock
    private HikeMapper hikeMapper;

    @Mock
    private TrailAdditionalInfoService trailAdditionalInfoService;

    @Mock
    private SpecificationFactory<Hike> specificationFactory;

    @InjectMocks
    private HikeService hikeService;

    private HikeDto hikeDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hikeDto = createHikeDto();
        user = new User();
        user.setId(10L);
    }

    @Test
    void getAllPublicHikes_shouldReturnPageOfHikeListingDto() {
        Specification<Hike> spec = mock(Specification.class);
        HikeListingDto dto = mock(HikeListingDto.class);
        Page<Hike> hikePage = new PageImpl<>(List.of(mock(Hike.class)));

        when(specificationFactory.filterEqualInFields(anyMap())).thenReturn(spec);
        when(specificationFactory.filterContainingInJoinFields(anyString(), any(), anyString(), anyString()))
                .thenReturn(spec);
        when(specificationFactory.filterEqualInJoinField(anySet(), any(), anyString()))
                .thenReturn(spec);
        when(spec.and(spec)).thenReturn(spec);
        when(hikeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(hikePage);
        when(hikeMapper.toHikeListingDto(any(Hike.class))).thenReturn(dto);

        Page<HikeListingDto> result = hikeService.getAllPublicHikes(Set.of(TrailDifficulty.EASY), "forest",
                "durationHours,DESC", 0, 5);

        assertThat(result).isNotNull();
        verify(hikeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(hikeMapper).toHikeListingDto(any(Hike.class));
    }

    @Test
    void getAllPublicHikes_invalidSortBy() {
        assertThrows(InvalidParameterException.class,
                () -> hikeService.getAllPublicHikes(Set.of(TrailDifficulty.EASY), "forest",
                        "durationHours", 0, 5));
    }

    @Test
    void getHike_existingHike_returnsHikeDtoWithTrailInfo() {
        Long hikeId = 1L;

        Hike hike = mock(Hike.class);
        Trail trail = mock(Trail.class);
        TrailInfoResponse trailInfoResponse = new TrailInfoResponse();

        when(hike.getTrail()).thenReturn(trail);
        when(trail.getLocation()).thenReturn("loc1");
        when(hikeRepository.findByIdAndUserId(hikeId, user.getId())).thenReturn(Optional.of(hike));
        when(trailAdditionalInfoService.getAdditionalTrailInfo("loc1")).thenReturn(trailInfoResponse);
        when(hikeMapper.toHikeDto(hike, trailInfoResponse)).thenReturn(hikeDto);

        HikeDto result = hikeService.getHike(hikeId, user);

        assertThat(result).isEqualTo(hikeDto);
        verify(hikeRepository).findByIdAndUserId(hikeId, user.getId());
        verify(trailAdditionalInfoService).getAdditionalTrailInfo("loc1");
        verify(hikeMapper).toHikeDto(hike, trailInfoResponse);
    }

    @Test
    void getHike_existingHike_noTrailInfo() {
        Long hikeId = 1L;
        Hike hike = mock(Hike.class);
        Trail trail = mock(Trail.class);

        when(hike.getTrail()).thenReturn(trail);
        when(trail.getLocation()).thenReturn("loc1");
        when(hikeRepository.findByIdAndUserId(hikeId, user.getId())).thenReturn(Optional.of(hike));
        when(trailAdditionalInfoService.getAdditionalTrailInfo("loc1")).thenReturn(null);
        when(hikeMapper.toHikeDto(hike)).thenReturn(hikeDto);

        HikeDto result = hikeService.getHike(hikeId, user);

        assertThat(result).isEqualTo(hikeDto);
        verify(trailAdditionalInfoService).getAdditionalTrailInfo("loc1");
        verify(hikeMapper).toHikeDto(hike);
    }

    @Test
    void getHike_missingHike() {
        Long hikeId = 1L;

        when(hikeRepository.findByIdAndUserId(hikeId, user.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> hikeService.getHike(hikeId, user));
    }

    @Test
    void createHike() {
        UpsertHikeDto dto = mock(UpsertHikeDto.class);
        Trail trail = new Trail();
        Hike hike = new Hike();
        Hike savedHike = new Hike();

        when(dto.trailId()).thenReturn(100L);
        when(trailService.findById(100L)).thenReturn(trail);
        when(hikeMapper.toHike(dto, trail, user)).thenReturn(hike);
        when(hikeRepository.save(hike)).thenReturn(savedHike);
        when(hikeMapper.toHikeDto(savedHike)).thenReturn(hikeDto);

        HikeDto result = hikeService.createHike(dto, user);

        assertThat(result).isEqualTo(hikeDto);
        verify(hikeRepository).save(hike);
        verify(hikeMapper).toHikeDto(savedHike);
    }

    @Test
    void deleteHike_existingHike() {
        Long hikeId = 1L;

        Hike hike = new Hike();
        when(hikeRepository.findByIdAndUserId(hikeId, user.getId())).thenReturn(Optional.of(hike));

        hikeService.deleteHike(hikeId, user);

        verify(hikeRepository).delete(hike);
    }

    private HikeDto createHikeDto() {
        return new HikeDto(
                1L,
                LocalDate.of(2025, 8, 3),
                4.5,
                "Sunny",
                "Nice trail with great views",
                createTrailDto()
        );
    }

    private TrailDto createTrailDto() {
        return new TrailDto(
                1L,
                "Rocky Mountain",
                12.5,
                800,
                TrailDifficulty.MODERATE,
                "A beautiful trail through rocky terrain.",
                "39.7392N",
                "104.9903W",
                5
        );
    }
}