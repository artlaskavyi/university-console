package com.arty.university_console.service;

import com.arty.university_console.repository.LectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public List<String> performGlobalSearch(String template) {
        validateSearchTemplate(template);
        return lectorRepository.findFullNamesByTemplate(template);
    }

    private void validateSearchTemplate(String template) {
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("Search template cannot be null or blank");
        }
    }
}
