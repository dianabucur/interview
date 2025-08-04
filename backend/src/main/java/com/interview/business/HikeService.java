package com.interview.business;

import com.interview.business.client.TrailInfoResponse;
import com.interview.business.dto.HikeDto;
import com.interview.business.dto.HikeListingDto;
import com.interview.business.dto.UpsertHikeDto;
import com.interview.business.mapper.HikeMapper;
import com.interview.business.util.SortByUtility;
import com.interview.business.util.SpecificationFactory;
import com.interview.data.entity.Hike;
import com.interview.data.entity.Trail;
import com.interview.data.entity.User;
import com.interview.data.enumeration.TrailDifficulty;
import com.interview.data.repository.HikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.interview.business.exception.ExceptionSupplierFactory.forEntityWithIdNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
public class HikeService {

    private final HikeRepository hikeRepository;
    private final TrailService trailService;
    private final HikeMapper hikeMapper;
    private final TrailAdditionalInfoService trailAdditionalInfoService;
    private final SpecificationFactory<Hike> specificationFactory;

    @Transactional(readOnly = true)
    public Page<HikeListingDto> getAllPublicHikes(Set<TrailDifficulty> difficulties, String searchText, String sortBy, int page, int size) {
        Sort sort = SortByUtility.getSorting(sortBy, Hike.class);
        Pageable pageable = PageRequest.of(page, size, sort);

        Map<String, Object> filteringMap = new HashMap<>();
        filteringMap.put(Hike.Fields.isPublic, Boolean.TRUE);

        Specification<Hike> specification = specificationFactory.filterEqualInFields(filteringMap);
        if (searchText != null) {
            specification = specification.and(specificationFactory.filterContainingInJoinFields(searchText, Hike.Fields.trail,
                    Trail.Fields.location, Trail.Fields.name));
        }
        if (difficulties != null) {
            specification = specification.and(specificationFactory.filterEqualInJoinField(difficulties, Hike.Fields.trail, Trail.Fields.difficulty));
        }

        return hikeRepository.findAll(specification, pageable)
                .map(hikeMapper::toHikeListingDto);
    }

    @Transactional(readOnly = true)
    public HikeDto getHike(Long id, User user) {
        Hike hike = hikeRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(forEntityWithIdNotFound(Hike.class.getSimpleName(), id));
        Optional<TrailInfoResponse> trailInfoResponse = getTrailAdditionalInfo(hike);

        return trailInfoResponse
                .map(trailInfo -> hikeMapper.toHikeDto(hike, trailInfo))
                .orElseGet(() -> hikeMapper.toHikeDto(hike));
    }

    @Transactional
    public HikeDto createHike(UpsertHikeDto hikeDto, User user) {
        Trail trail = trailService.findById(hikeDto.trailId());
        return hikeMapper.toHikeDto(hikeRepository.save(hikeMapper.toHike(hikeDto, trail, user)));
    }

    @Transactional
    public HikeDto updateHike(Long hikeId, UpsertHikeDto hikeDto, User user) {
        Hike hike = hikeRepository.findByIdAndUserId(hikeId, user.getId())
                .orElseThrow(forEntityWithIdNotFound(Hike.class.getSimpleName(), hikeId));
        Trail trail = trailService.findById(hikeDto.trailId());
        hikeMapper.populateFromHikeDto(hike, hikeDto, trail);
        hikeRepository.save(hike);

        Optional<TrailInfoResponse> trailInfoResponse = getTrailAdditionalInfo(hike);
        return trailInfoResponse
                .map(trailInfo -> hikeMapper.toHikeDto(hike, trailInfo))
                .orElseGet(() -> hikeMapper.toHikeDto(hike));
    }

    @Transactional
    public void deleteHike(Long id, User user) {
        Hike hike = hikeRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(forEntityWithIdNotFound(Hike.class.getSimpleName(), id));
        hikeRepository.delete(hike);
    }

    private Optional<TrailInfoResponse> getTrailAdditionalInfo(Hike hike) {
        try {
            return Optional.ofNullable(trailAdditionalInfoService.getAdditionalTrailInfo(hike.getTrail().getLocation()));
        } catch (Exception ex) {
            log.warn("Failed to fetch additional trail info for location '{}': {}", hike.getTrail().getLocation(), ex.toString());
            return Optional.empty();
        }
    }
}
