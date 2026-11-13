package io.github.danielreker.smartquizzes.web.controllers;

import io.github.danielreker.smartquizzes.model.auth.AuthenticatedUser;
import io.github.danielreker.smartquizzes.services.QuizService;
import io.github.danielreker.smartquizzes.web.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizzes")
@CrossOrigin
class QuizController {

    private final QuizService quizService;


    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(
            @RequestBody @Valid QuizCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService.createQuiz(request, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> getQuiz(
            @PathVariable UUID quizId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService.getQuiz(quizId, user);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping("/{quizId}/questions")
    public ResponseEntity<QuizResponse> upsertQuestions(
            @PathVariable UUID quizId,
            @RequestBody @Valid QuizQuestionsUpsertRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService
                .upsertQuestions(quizId, request, user);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{quizId}/requests/start")
    public ResponseEntity<QuizResponse> startQuiz(
            @PathVariable UUID quizId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService.startQuiz(quizId, user);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{quizId}/requests/finish")
    public ResponseEntity<QuizResponse> finishQuiz(
            @PathVariable UUID quizId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService.finishQuiz(quizId, user);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{quizId}/submissions")
    public ResponseEntity<SubmissionResponse> createSubmission(
            @PathVariable UUID quizId,
            @RequestBody @Valid SubmissionCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final SubmissionResponse submissionResponse =
                quizService.createSubmission(quizId, request, user);

        return ResponseEntity
                .ok(submissionResponse);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{quizId}/stats")
    public ResponseEntity<StatsResponse> getStats(
            @PathVariable UUID quizId
    ) {
        final StatsResponse response = quizService.getStats(quizId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/{quizId}/questions:ai-generate")
    public ResponseEntity<QuizResponse> aiGenerateQuestions(
            @PathVariable UUID quizId,
            @RequestBody @Valid AiGenerateQuestionsRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizResponse response = quizService.aiGenerateQuestions(quizId, request, user);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/my")
    public ResponseEntity<QuizzesResponse> getMyQuizzes(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final QuizzesResponse response = quizService.getUserQuizzes(user);

        return ResponseEntity
                .ok(response);
    }

}
