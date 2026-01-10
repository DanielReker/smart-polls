package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.repositories.PollRepository;
import io.github.danielreker.smartpolls.dao.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PollSecurityService {

    private final PollRepository pollRepository;

    private final SubmissionRepository submissionRepository;


    public boolean isUserPollOwner(Long userId, Long pollId) {
        return pollRepository.existsByIdAndOwnerId(pollId, userId);
    }

    public Long countUserSubmissionsToPoll(Long userId, Long pollId) {
        return submissionRepository.countByOwnerIdAndPollId(userId, pollId);
    }


}
