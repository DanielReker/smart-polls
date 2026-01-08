package io.github.danielreker.smartpolls.model;

import lombok.Getter;

public enum QuestionType {
    TEXT(Names.TEXT),
    SINGLE_CHOICE(Names.SINGLE_CHOICE),
    MULTI_CHOICE(Names.MULTI_CHOICE);

    @Getter
    private final String name;

    QuestionType(String name) {
        this.name = name;
    }

    public static class Names {
        public static final String TEXT = "text";
        public static final String SINGLE_CHOICE = "single-choice";
        public static final String MULTI_CHOICE = "multi-choice";
    }
}
