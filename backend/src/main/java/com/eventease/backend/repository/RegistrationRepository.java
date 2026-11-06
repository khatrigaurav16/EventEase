package com.eventease.backend.repository;

import com.eventease.backend.entity.Registration;
import com.eventease.backend.entity.User;
import com.eventease.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("SELECT r FROM Registration r LEFT JOIN FETCH r.event LEFT JOIN FETCH r.user WHERE r.user.id = ?1")
    List<Registration> findByUserId(Long userId);

    @Query("SELECT r FROM Registration r LEFT JOIN FETCH r.event LEFT JOIN FETCH r.user WHERE r.user = ?1 AND r.status = ?2")
    List<Registration> findByUserAndStatus(User user, String status);

    List<Registration> findByEventId(Long eventId);

    List<Registration> findByEventAndStatus(Event event, String status);

    Optional<Registration> findByUserIdAndEventIdAndStatus(Long userId, Long eventId, String status);

    long countByEventIdAndStatus(Long eventId, String status);

    boolean existsByUserIdAndEventIdAndStatus(Long userId, Long eventId, String status);

    @Query("SELECT count(r) FROM Registration r WHERE r.status = ?1")
    long countByStatus(String status);
}
