CREATE TABLE evaluation_results (
    id UUID PRIMARY KEY,
    interview_id UUID NOT NULL UNIQUE,
    candidate_id UUID NOT NULL,
    correctness INT NOT NULL,
    clarity INT NOT NULL,
    depth INT NOT NULL,
    communication INT NOT NULL,
    summary TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
