package com.resort.platform.backnode.foodtracker.model.rest.request;

import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewFoodTrackerUserRequest extends NewUserRequest {
    private List<String> departments;
}
