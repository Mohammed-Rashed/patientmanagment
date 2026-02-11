package com.topbits.patientmanagment.common.paging;

import com.topbits.patientmanagment.api.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class PageMapper {
    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        String sort = toSortString(page.getSort());

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .sort(sort)
                .build();
    }

    private static String toSortString(Sort sort) {
        if (sort == null || sort.isUnsorted()) return "";
        return sort.stream()
                .map(o -> o.getProperty() + "," + o.getDirection().name().toLowerCase())
                .collect(Collectors.joining(";"));
    }
}
