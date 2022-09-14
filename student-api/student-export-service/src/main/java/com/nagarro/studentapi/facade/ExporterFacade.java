package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.service.ExporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class ExporterFacade {
    private final ExporterService exporterService;
    public String exportStudent(String uuid) {
        return exporterService.exportStudent(uuid);
    }
}
