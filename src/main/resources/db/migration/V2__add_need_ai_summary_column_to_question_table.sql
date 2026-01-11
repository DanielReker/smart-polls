ALTER TABLE question
    ADD need_ai_summary BOOLEAN;

UPDATE question
SET need_ai_summary = false
WHERE dtype = 'text' AND need_ai_summary IS NULL;