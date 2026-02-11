package com.topbits.patientmanagment.common.error;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    private String code;
    private String message;
    private List<FieldErrorItem> details;
    private String path;
    private Instant timestamp;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorItem {
        private String field;
        private String message;
    }
}
