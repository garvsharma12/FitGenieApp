package com.draig.activityservice.service;

import com.draig.activityservice.dto.ActivityRequest;
import com.draig.activityservice.dto.ActivityResponse;
import com.draig.activityservice.model.Activity;
import com.draig.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser) {
            throw new RuntimeException("User is not valid "+request.getUserId());
        }
        Activity activity = Activity.builder().
                userId(request.getUserId()).
                type(request.getType()).
                duration(request.getDuration()).
                caloriesBurned(request.getCaloriesBurned()).
                startTime(request.getStartTime()).
                additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);
        try{
            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
            log.info("Activity event sent to Kafka for activity id: {}", savedActivity.getId());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return mapToActivityResponse(savedActivity);
    }

    private ActivityResponse mapToActivityResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapToActivityResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .map(this::mapToActivityResponse).orElseThrow(() -> new RuntimeException("Activity Not Found with Id"+activityId));
    }
}
