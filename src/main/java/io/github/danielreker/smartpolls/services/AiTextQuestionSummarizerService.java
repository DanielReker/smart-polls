package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionSummaryTagEntity;
import io.github.danielreker.smartpolls.dao.repositories.answers.TextAnswerTagRepository;
import io.github.danielreker.smartpolls.dao.repositories.questions.TextQuestionRepository;
import io.github.danielreker.smartpolls.dao.repositories.questions.TextQuestionSummaryTagRepository;
import io.github.danielreker.smartpolls.model.answers.TagWithEmbedding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AiTextQuestionSummarizerService {

    private final TextAnswerTagRepository textAnswerTagRepository;

    private final TextQuestionSummaryTagRepository textQuestionSummaryTagRepository;

    private final TextQuestionRepository textQuestionRepository;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void summarizeTextQuestion(Long textQuestionId) {
        final TextQuestionEntity questionReference =
                textQuestionRepository.getReferenceById(textQuestionId);

        textQuestionSummaryTagRepository.deleteAllByTextQuestionId(textQuestionId);

        final List<TagWithEmbedding> answerTags = textAnswerTagRepository
                .findByTextQuestionId(textQuestionId)
                .stream()
                .map(tagEntity ->
                        new TagWithEmbedding(tagEntity.getId(), tagEntity.getName(), tagEntity.getEmbedding()))
                .toList();

        final Clusterer<TagWithEmbedding> clusterer =
                new MultiKMeansPlusPlusClusterer<>(new KMeansPlusPlusClusterer<>(5), 10);

        final List<TextQuestionSummaryTagEntity> questionTagEntities = clusterer
                .cluster(answerTags)
                .stream()
                .map(cluster -> {
                    log.debug("Cluster: {}", cluster.getPoints().stream().map(TagWithEmbedding::tag).collect(Collectors.joining(", ")));

                    final TextQuestionSummaryTagEntity questionTagEntity = new TextQuestionSummaryTagEntity();
                    questionTagEntity.setQuestion(questionReference);
                    questionTagEntity.setName(cluster.getPoints().getFirst().tag());
                    questionTagEntity.setCount((long) cluster.getPoints().size());
                    return questionTagEntity;
                })
                .collect(Collectors.toList());

        long clusteredCount = questionTagEntities
                .stream()
                .map(TextQuestionSummaryTagEntity::getCount)
                .reduce(0L, Long::sum);

        long unclusteredCount = answerTags.size() - clusteredCount;

        if (unclusteredCount > 0) {
            final TextQuestionSummaryTagEntity otherQuestionTagEntity = new TextQuestionSummaryTagEntity();
            otherQuestionTagEntity.setQuestion(questionReference);
            otherQuestionTagEntity.setName("Other");
            otherQuestionTagEntity.setCount(unclusteredCount);
            questionTagEntities.add(otherQuestionTagEntity);
        }

        textQuestionSummaryTagRepository.saveAll(questionTagEntities);
    }

}
