package com.soroosh.auth.commons.http.requests;

import org.springframework.data.domain.Sort;

public record PageAndSortRequest(int perPage,
                                 int page,
                                 Sort.Direction direction,
                                 String sortBy) {
}
