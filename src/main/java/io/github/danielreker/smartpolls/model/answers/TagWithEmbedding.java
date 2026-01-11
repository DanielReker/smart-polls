package io.github.danielreker.smartpolls.model.answers;

import org.apache.commons.math3.ml.clustering.Clusterable;

public record TagWithEmbedding(
        Long tagId,
        String tag,
        float[] embedding
) implements Clusterable {

    @Override
    public double[] getPoint() {
        double[] result = new double[embedding.length];
        for (int i = 0; i < embedding.length; i++) {
            result[i] = embedding[i];
        }
        return result;
    }

}
