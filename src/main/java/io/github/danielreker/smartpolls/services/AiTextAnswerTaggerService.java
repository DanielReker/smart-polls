package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerTagEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartpolls.dao.repositories.answers.TextAnswerRepository;
import io.github.danielreker.smartpolls.model.QuestionType;
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
            Ты — эксперт-аналитик данных, специализирующийся на анализе тональности (Sentiment Analysis) и тегировании естественного языка (NLP).
            Твоя задача — анализировать открытые ответы на опросы и извлекать основные *семантические инсайты* в виде тегов.

            ФОРМАТ ВВОДА:
            Необработанный текстовый ответ пользователя (строка).

            ФОРМАТ ВЫВОДА:
            JSON-объект: { "tags": ["Тег 1", "Тег 2"] }

            ПРАВИЛА ТЕГИРОВАНИЯ:

            1. **Оценка, а не просто тема:** Никогда не используй одиночные существительные, такие как «Цена», «Звук», «Еда». Это категории. Ты должен уловить, *что именно* с ними так или не так.

            * ПЛОХО: «Цена»
            * ХОРОШО: «Высокая цена»
            * ПЛОХО: «Звук»
            * ХОРОШО: «Низкое качество звука»

            2. **Обобщение, а не цитирование:** Не просто копируй слова пользователя. Делай небольшое абстрагирование до профессионального бизнес-термина.

            * ПЛОХО: «Микрофон постоянно отключался» (Слишком конкретно)
            * ХОРОШО: «Нестабильное аудиосоединение» (Профессиональное обобщение)

            3. **Краткость:** Теги должны состоять из 2–4 слов.
            4. **Вежливость:** Если текст содержит ненормативную лексику, извлеки ее смысл и перефразируй нейтральным профессиональным языком.

            * Ввод: «Интерфейс — дер*мо» -> Тег: «Плохой дизайн интерфейса»

            5. **Атомарные инсайты:** Если текст содержит несколько разных идей, раздели их на отдельные теги.

            * Ввод: «Дорого и медленная поддержка.» -> Теги: ["Высокая цена", "Медленная поддержка"]

            ПРИМЕРЫ:
            Ввод: «Приложение вылетает каждый раз, когда я пытаюсь загрузить фото.»
            Вывод: { "tags": ["Сбои приложения", "Ошибка загрузки фото"] }

            Ввод: «Мне нравится дизайн, но подписка — это грабеж.»
            Вывод: { "tags": ["Отличный дизайн", "Необоснованная цена"] }

            Проанализируй следующий текст и предоставь теги в формате JSON:
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
    public void tagSubmissionTextAnswers(Long submissionId) {
        textAnswerRepository
                .findAllBySubmissionId(submissionId)
                .stream()
                .filter(answerEntity ->
                        answerEntity.getQuestionType() == QuestionType.TEXT)
                .filter(textAnswerEntity ->
                        ((TextQuestionEntity) textAnswerEntity.getQuestion()).getNeedAiSummary())
                .forEach(this::tagTextAnswer);
    }

    private void tagTextAnswer(TextAnswerEntity textAnswerEntity) {
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
