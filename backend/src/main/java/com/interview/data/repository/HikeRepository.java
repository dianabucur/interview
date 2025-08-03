package com.interview.data.repository;

import com.interview.data.entity.Hike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HikeRepository extends JpaRepository<Hike, Long>, JpaSpecificationExecutor<Hike> {

    @EntityGraph(attributePaths = {"user", "trail"})
    Optional<Hike> findByIdAndUserId(Long hikeLogId, Long userId);

    @EntityGraph(attributePaths = {"user", "trail"})
    Page<Hike> findAll(Specification<Hike> spec, Pageable pageable);
}
