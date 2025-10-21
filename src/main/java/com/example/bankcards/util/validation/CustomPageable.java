package com.example.bankcards.util.validation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class CustomPageable {

    @Min(0)
    private int page;

    @Min(1)
    @Max(100)
    private int size;

    private String sortBy;
    private String direction;

    public Pageable toPageable() {
        Sort.Direction dir = (direction != null && direction.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = (sortBy != null) ? sortBy : "cardId";
        return PageRequest.of(page, size, Sort.by(dir, sortField));
    }
}
