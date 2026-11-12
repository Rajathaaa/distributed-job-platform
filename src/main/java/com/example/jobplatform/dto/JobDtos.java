package com.example.jobplatform.dto;
import jakarta.validation.constraints.*; import java.time.Instant; import java.util.UUID;
public class JobDtos {
 public record SubmitJobRequest(@NotBlank @Pattern(regexp="ECHO|UPPERCASE|SLEEP|FAIL") String jobType,@NotBlank String payload,@Min(1) @Max(10) Integer priority) {}
 public record JobResponse(UUID id,String jobType,String status,int priority,int attempts,int maxAttempts,String result,String errorMessage,Instant createdAt,Instant startedAt,Instant completedAt) {}
 public record JobMessage(UUID jobId,int priority) {}
}
