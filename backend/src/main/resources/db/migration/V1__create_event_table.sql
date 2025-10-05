CREATE TABLE event
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    start_date_time TIMESTAMP    NOT NULL,
    end_date_time   TIMESTAMP    NOT NULL,
    location        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)