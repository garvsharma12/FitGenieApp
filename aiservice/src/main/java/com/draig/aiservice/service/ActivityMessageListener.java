package com.draig.aiservice.service;

import com.draig.aiservice.model.Activity;
import com.draig.aiservice.model.Recommendation;
import com.draig.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getUserId());

        Recommendation recommendation = aiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

        log.info("Saved recommendation for activity: {}", activity.getId());
    }
}
