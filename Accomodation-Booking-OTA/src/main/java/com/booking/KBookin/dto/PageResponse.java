package com.booking.KBookin.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PageResponse<T> implements Serializable {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private T data;
}