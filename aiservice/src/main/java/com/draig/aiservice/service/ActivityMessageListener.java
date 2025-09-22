package com.draig.aiservice.service;

import com.draig.aiservice.model.Activity;
import com.draig.aiservice.model.Recommendation;
import com.draig.aiservice.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());

        Recommendation recommendation = aiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

        log.info("Saved recommendation for activity: {}", activity.getId());
    }
}
