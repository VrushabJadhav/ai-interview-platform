CREATE TABLE interview_sessions (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL,
    role VARCHAR(120) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    submitted_at TIMESTAMPTZ
);
CREATE TABLE candidate_answers (
    id UUID PRIMARY KEY,
    interview_id UUID NOT NULL REFERENCES interview_sessions(id),
    question TEXT NOT NULL,
    answer TEXT NOT NULL
);
