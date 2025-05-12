package com.arty.university_console.service;

import com.arty.university_console.dto.DegreeCountDTO;
import com.arty.university_console.model.Degree;
import com.arty.university_console.model.Lector;
import com.arty.university_console.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public String getDepartmentHeadFullName(String departmentName) {
        validateDepartmentName(departmentName);
        ensureDepartmentExists(departmentName);
        return departmentRepository.findHeadByDepartmentName(departmentName)
                .map(Lector::getFullName)
                .orElse(null);
    }

    public Map<String, Long> getDepartmentStatistics(String departmentName) {
        validateDepartmentName(departmentName);
        ensureDepartmentExists(departmentName);
        Map<String, Long> counts = new HashMap<>();
        for (Degree degree : Degree.values()) {
            counts.put(degree.name().toLowerCase(), 0L);
        }
        List<DegreeCountDTO> results = departmentRepository.countLectorsByDegree(departmentName);
        for (DegreeCountDTO result : results) {
            counts.put(result.degree().name().toLowerCase(), result.count());
        }
        return counts;
    }

    public BigDecimal getDepartmentAverageSalary(String departmentName) {
        validateDepartmentName(departmentName);
        ensureDepartmentExists(departmentName);
        return departmentRepository.findAverageSalaryByDepartmentName(departmentName)
                .orElse(BigDecimal.ZERO);
    }

    public Long getDepartmentEmployeeCount(String departmentName) {
        validateDepartmentName(departmentName);
        ensureDepartmentExists(departmentName);
        return departmentRepository.countLectorsByDepartmentName(departmentName);
    }

    private void validateDepartmentName(String name) {
        if (name == null || name.isBlank() ) {
            throw new IllegalArgumentException("Department name cannot be null or blank");
        }
    }

    private void ensureDepartmentExists(String departmentName) {
        if (!departmentRepository.existsByName(departmentName)) {
            throw new EntityNotFoundException("Department " + departmentName + " not found");
        }
    }
}
