package com.interview.business;

import com.interview.data.entity.Trail;
import com.interview.data.repository.TrailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.interview.business.exception.ExceptionSupplierFactory.forEntityWithIdNotFound;

@Service
@RequiredArgsConstructor
public class TrailService {

    private final TrailRepository trailRepository;

    public Trail findById(Long id) {
        return trailRepository.findById(id)
                .orElseThrow(forEntityWithIdNotFound(Trail.class.getSimpleName(), id));
    }
}
