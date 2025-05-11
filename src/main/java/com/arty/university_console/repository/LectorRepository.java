package com.arty.university_console.repository;

import com.arty.university_console.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
    @Query("SELECT l.fullName FROM Lector l WHERE LOWER(l.fullName) LIKE LOWER(CONCAT('%', :template, '%'))")
    List<String> findFullNamesByTemplate(@Param("template") String template);
}
