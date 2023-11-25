package com.resort.platform.backnode.foodtracker.model.planday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PagingSubModel {
    int offset;
    int limit;
    int total;
}
