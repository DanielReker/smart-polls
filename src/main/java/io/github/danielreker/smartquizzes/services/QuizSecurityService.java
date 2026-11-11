package io.github.danielreker.smartquizzes.services;

import io.github.danielreker.smartquizzes.dao.repositories.QuizRepository;
import io.github.danielreker.smartquizzes.dao.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class QuizSecurityService {

    private final QuizRepository quizRepository;

    private final SubmissionRepository submissionRepository;


    public boolean isUserQuizOwner(UUID userId, UUID quizId) {
        return quizRepository.existsByIdAndOwnerId(quizId, userId);
    }

    public Long countUserSubmissionsToQuiz(UUID userId, UUID quizId) {
        return submissionRepository.countByOwnerIdAndQuizId(userId, quizId);
    }


}
