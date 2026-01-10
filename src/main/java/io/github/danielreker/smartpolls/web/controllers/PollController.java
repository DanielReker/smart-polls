package io.github.danielreker.smartpolls.web.controllers;

import io.github.danielreker.smartpolls.model.auth.AuthenticatedUser;
import io.github.danielreker.smartpolls.services.PollService;
import io.github.danielreker.smartpolls.web.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/polls")
class PollController {

    private final PollService pollService;

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping
    public ResponseEntity<PollResponse> createPoll(
            @RequestBody @Valid PollCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.createPoll(request, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.getPoll(pollId, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/{pollId}/questions")
    public ResponseEntity<PollResponse> upsertQuestions(
            @PathVariable Long pollId,
            @RequestBody @Valid PollQuestionsUpsertRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.upsertQuestions(pollId, request, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{pollId}/requests/start")
    public ResponseEntity<PollResponse> startPoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.startPoll(pollId, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{pollId}/requests/finish")
    public ResponseEntity<PollResponse> finishPoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.finishPoll(pollId, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{pollId}/submissions")
    public ResponseEntity<SubmissionResponse> createSubmission(
            @PathVariable Long pollId,
            @RequestBody @Valid SubmissionCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final SubmissionResponse response = pollService.createSubmission(pollId, request, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{pollId}/stats")
    public ResponseEntity<StatsResponse> getStats(
            @PathVariable Long pollId
    ) {
        final StatsResponse response = pollService.getStats(pollId);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{pollId}/questions:ai-generate")
    public ResponseEntity<PollResponse> aiGenerateQuestions(
            @PathVariable Long pollId,
            @RequestBody @Valid AiGenerateQuestionsRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final PollResponse response = pollService.aiGenerateQuestions(pollId, request, user);

        return ResponseEntity
                .ok()
                .body(response);
    }

}
