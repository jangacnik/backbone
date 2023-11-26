package com.resort.platform.backnode.foodtracker.model.rest.request;

import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewFoodTrackerUserRequest extends NewUserRequest {
    private List<String> departments;
}
