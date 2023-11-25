package com.resort.platform.backnode.foodtracker.model.planday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseModel {

    PagingSubModel paging;
    List<EmployeeSubModel> data;
}
