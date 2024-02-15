package com.resort.platform.backnode.foodtracker.model.planday;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ShiftsModel {
    List<ShiftsSubModel> data;
}
