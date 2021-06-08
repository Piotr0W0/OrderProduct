package com.company.interview.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderPeriod {
    @JsonAlias("start_date")
    private LocalDateTime startDate;
    @JsonAlias("end_date")
    private LocalDateTime endDate;

    public boolean hasInvalidAttributes() {
        return startDate == null || endDate == null || !startDate.isBefore(endDate);
    }

}
