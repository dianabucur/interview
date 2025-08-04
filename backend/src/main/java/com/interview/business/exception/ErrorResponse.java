package com.interview.business.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private List<String> messages;
    private String path;
    private String logId = UUID.randomUUID().toString();

    public ErrorResponse(List<String> messages, String path) {
        this.messages = messages;
        this.path = path;
    }
}
