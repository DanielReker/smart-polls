package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerTagEntity;
import io.github.danielreker.smartpolls.dao.repositories.answers.TextAnswerRepository;
import io.github.danielreker.smartpolls.web.dtos.answers.TextAnswerTagsDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
public class AiTextAnswerTaggerService {

    private static final String SYSTEM_PROMPT = """
            You are an expert Data Analyst specializing in Sentiment Analysis and NLP tagging.
            Your task is to analyze open-ended survey responses and extract the main *semantic insights* as tags.
            
            INPUT FORMAT:
            A raw text response from a user (string).
            
            OUTPUT FORMAT:
            JSON object: { "tags": ["Tag 1", "Tag 2"] }
            
            TAGGING RULES:
            1.  **Evaluation, not just Topic:** Never use single-word nouns like "Price", "Sound", "Food". These are categories. You must capture *what* is wrong or right about them.
                *   BAD: "Price"
                *   GOOD: "High price"
                *   BAD: "Sound"
                *   GOOD: "Poor sound quality"
            
            2.  **Generalization, not Quotation:** Do not just copy the user's words. Abstract slightly to a professional business term.
                *   BAD: "Microphone kept cutting out" (Too specific)
                *   GOOD: "Unstable audio connection" (Professional summary)
            
            3.  **Conciseness:** Keep tags between 2 and 4 words.
            
            4.  **Politeness:** If the text contains profanity, extract the underlying meaning and rephrase it into neutral, professional language.
                *   Input: "The UI is sh*t" -> Tag: "Poor UI design"
            
            5.  **Atomic Insights:** If a text contains multiple distinct ideas, split them into separate tags.
                *   Input: "Expensive and slow support." -> Tags: ["High price", "Slow support"]
            
            EXAMPLES:
            Input: "The app crashes every time I try to upload a photo."
            Output: { "tags": ["App crash", "Photo upload bug"] }
            
            Input: "I love the design, but the subscription is a rip-off."
            Output: { "tags": ["Great design", "Unreasonable price"] }
            
            Analyze the following text and provide the tags in JSON:
            """;


    private final EmbeddingModel embeddingModel;

    private final ChatClient chatClient;

    private final TextAnswerRepository textAnswerRepository;

    public AiTextAnswerTaggerService(
            EmbeddingModel embeddingModel,
            ChatClient.Builder chatClientBuilder,
            TextAnswerRepository textAnswerRepository
    ) {
        this.embeddingModel = embeddingModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
        this.textAnswerRepository = textAnswerRepository;
    }


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void tagTextAnswer(Long textAnswerId) {
        final TextAnswerEntity textAnswerEntity = textAnswerRepository
                .findById(textAnswerId)
                .orElseThrow();

        final List<String> tags = generateTextTags(textAnswerEntity.getValue());

        final List<float[]> embeddings = embeddingModel.embed(tags);

        final List<TextAnswerTagEntity> tagEntities = IntStream
                .range(0, tags.size())
                .mapToObj(i -> {
                    final TextAnswerTagEntity tagEntity = new TextAnswerTagEntity();
                    tagEntity.setAnswer(textAnswerEntity);
                    tagEntity.setName(tags.get(i));
                    tagEntity.setEmbedding(embeddings.get(i));
                    return tagEntity;
                })
                .toList();

        textAnswerEntity
                .getTags()
                .addAll(tagEntities);

        textAnswerRepository.save(textAnswerEntity);
    }


    private List<String> generateTextTags(String text) {
        final TextAnswerTagsDto tagsResponse = chatClient
                .prompt()
                .user(Objects.requireNonNull(text))
                .call()
                .entity(TextAnswerTagsDto.class);

        return Objects
                .requireNonNull(tagsResponse)
                .tags();
    }

}
