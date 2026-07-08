package io.github.dongjulim.domain.common.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
