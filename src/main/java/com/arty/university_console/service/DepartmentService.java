package com.arty.university_console.service;

import com.arty.university_console.dto.DegreeCountDTO;
import com.arty.university_console.model.Degree;
import com.arty.university_console.model.Department;
import com.arty.university_console.model.Lector;
import com.arty.university_console.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Map<Degree, Long> getDepartmentStatistics(String departmentName) {
        validateDepartmentName(departmentName);
        ensureDepartmentExists(departmentName);
        EnumMap<Degree, Long> counts = new EnumMap<>(Degree.class);
        for (Degree degree : Degree.values()) {
            counts.put(degree, 0L);
        }
        List<DegreeCountDTO> results = departmentRepository.countLectorsByDegree(departmentName);
        for (DegreeCountDTO(Degree degree, Long count) : results) {
            counts.put(degree, count);
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
