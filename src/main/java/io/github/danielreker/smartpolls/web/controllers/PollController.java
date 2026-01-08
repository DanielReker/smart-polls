package io.github.danielreker.smartpolls.web.controllers;

import io.github.danielreker.smartpolls.services.PollService;
import io.github.danielreker.smartpolls.web.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/polls")
class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@RequestBody @Valid PollCreateRequest request) {
        final PollResponse response = pollService.createPoll(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPoll(@PathVariable Long pollId) {
        final PollResponse response = pollService.getPoll(pollId);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PutMapping("/{pollId}/questions")
    public ResponseEntity<PollResponse> upsertQuestions(
            @PathVariable Long pollId,
            @RequestBody @Valid PollQuestionsUpsertRequest request
    ) {
        final PollResponse response = pollService.upsertQuestions(pollId, request);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/{pollId}/requests/start")
    public ResponseEntity<PollResponse> startPoll(
            @PathVariable Long pollId
    ) {
        final PollResponse response = pollService.startPoll(pollId);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/{pollId}/requests/finish")
    public ResponseEntity<PollResponse> finishPoll(
            @PathVariable Long pollId
    ) {
        final PollResponse response = pollService.finishPoll(pollId);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/{pollId}/submissions")
    public ResponseEntity<SubmissionResponse> createSubmission(
            @PathVariable Long pollId,
            @RequestBody @Valid SubmissionCreateRequest request
    ) {
        final SubmissionResponse response = pollService.createSubmission(pollId, request);

        return ResponseEntity
                .ok()
                .body(response);
    }

}
