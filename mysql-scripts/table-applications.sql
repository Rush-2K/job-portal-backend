USE `job-portal`;

CREATE TABLE applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    job_id INT,
    resume_url TEXT NOT NULL,
    status ENUM('APPLIED', 'REVIEWED', 'REJECTED', 'ACCEPTED') DEFAULT 'APPLIED',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);
