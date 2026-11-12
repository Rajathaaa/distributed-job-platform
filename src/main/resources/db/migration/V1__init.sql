CREATE TABLE app_users (
 id BIGSERIAL PRIMARY KEY, email VARCHAR(255) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL,
 role VARCHAR(30) NOT NULL DEFAULT 'USER', created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE jobs (
 id UUID PRIMARY KEY, owner_id BIGINT NOT NULL REFERENCES app_users(id), job_type VARCHAR(40) NOT NULL,
 payload TEXT NOT NULL, priority INTEGER NOT NULL DEFAULT 5, status VARCHAR(30) NOT NULL,
 attempts INTEGER NOT NULL DEFAULT 0, max_attempts INTEGER NOT NULL DEFAULT 4,
 result TEXT, error_message TEXT, created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
 updated_at TIMESTAMPTZ NOT NULL DEFAULT now(), started_at TIMESTAMPTZ, completed_at TIMESTAMPTZ
);
CREATE INDEX idx_jobs_owner_created ON jobs(owner_id, created_at DESC);
CREATE INDEX idx_jobs_status_created ON jobs(status, created_at);
CREATE TABLE outbox_events (
 id BIGSERIAL PRIMARY KEY, aggregate_id UUID NOT NULL, event_type VARCHAR(80) NOT NULL,
 payload TEXT NOT NULL, created_at TIMESTAMPTZ NOT NULL DEFAULT now(), published_at TIMESTAMPTZ
);
CREATE INDEX idx_outbox_unpublished ON outbox_events(id) WHERE published_at IS NULL;
CREATE TABLE job_attempts (
 id BIGSERIAL PRIMARY KEY, job_id UUID NOT NULL REFERENCES jobs(id), attempt_number INTEGER NOT NULL,
 worker_id VARCHAR(120) NOT NULL, outcome VARCHAR(30) NOT NULL, error_message TEXT,
 started_at TIMESTAMPTZ NOT NULL DEFAULT now(), finished_at TIMESTAMPTZ
);
