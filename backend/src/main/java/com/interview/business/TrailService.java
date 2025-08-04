package com.interview.business;

import com.interview.data.entity.Trail;
import com.interview.data.repository.TrailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.interview.business.exception.ExceptionSupplierFactory.forEntityWithIdNotFound;

@Service
@RequiredArgsConstructor
public class TrailService {

    private final TrailRepository trailRepository;

    @Transactional(readOnly = true)
    public Trail findById(Long id) {
        return trailRepository.findById(id)
                .orElseThrow(forEntityWithIdNotFound(Trail.class.getSimpleName(), id));
    }
}
