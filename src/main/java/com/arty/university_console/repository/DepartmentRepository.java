package com.arty.university_console.repository;

import com.arty.university_console.dto.DegreeCountDTO;
import com.arty.university_console.model.Department;
import com.arty.university_console.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT l FROM Department d JOIN d.head l WHERE d.name = :name")
    Optional<Lector> findHeadByDepartmentName(@Param("name") String name);

    @Query("SELECT new com.arty.university_console.dto.DegreeCountDTO(l.degree, COUNT(l.id)) " +
            "FROM Department d JOIN d.lectors l WHERE d.name = :name GROUP BY l.degree")
    List<DegreeCountDTO> countLectorsByDegree(@Param("name") String name);

    @Query("SELECT AVG(l.salary) FROM Department d JOIN d.lectors l WHERE d.name = :name")
    Optional<BigDecimal> findAverageSalaryByDepartmentName(@Param("name") String name);

    @Query("SELECT COUNT(l.id) FROM Department d JOIN d.lectors l WHERE d.name = :name")
    Long countLectorsByDepartmentName(@Param("name") String name);
}
