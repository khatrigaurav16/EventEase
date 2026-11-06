package com.eventease.backend.repository;

import com.eventease.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category")
    List<Event> findAll();

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category WHERE e.status = ?1")
    List<Event> findByStatus(String status);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category WHERE e.category.id = ?1")
    List<Event> findByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Event> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category WHERE e.status = ?1 AND e.eventDate >= ?2")
    List<Event> findByStatusAndEventDateGreaterThanEqual(String status, LocalDate date);

    @Query("SELECT count(e) FROM Event e WHERE e.status = ?1")
    long countByStatus(String status);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category WHERE e.id = ?1")
    Optional<Event> findById(Long id);

    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);

    boolean existsById(Long id);
}
