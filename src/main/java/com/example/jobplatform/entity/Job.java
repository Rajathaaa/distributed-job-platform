package com.example.jobplatform.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="jobs")
public class Job {
 @Id private UUID id;
 @Column(name="owner_id",nullable=false) private Long ownerId;
 @Column(name="job_type",nullable=false) private String jobType;
 @Column(nullable=false,columnDefinition="text") private String payload;
 @Column(nullable=false) private int priority=5;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private JobStatus status=JobStatus.QUEUED;
 @Column(nullable=false) private int attempts=0;
 @Column(name="max_attempts",nullable=false) private int maxAttempts=4;
 @Column(columnDefinition="text") private String result;
 @Column(name="error_message",columnDefinition="text") private String errorMessage;
 @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
 @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
 @Column(name="started_at") private Instant startedAt;
 @Column(name="completed_at") private Instant completedAt;
 public UUID getId(){return id;} public void setId(UUID v){id=v;} public Long getOwnerId(){return ownerId;} public void setOwnerId(Long v){ownerId=v;}
 public String getJobType(){return jobType;} public void setJobType(String v){jobType=v;} public String getPayload(){return payload;} public void setPayload(String v){payload=v;}
 public int getPriority(){return priority;} public void setPriority(int v){priority=v;} public JobStatus getStatus(){return status;} public void setStatus(JobStatus v){status=v;}
 public int getAttempts(){return attempts;} public void setAttempts(int v){attempts=v;} public int getMaxAttempts(){return maxAttempts;} public void setMaxAttempts(int v){maxAttempts=v;}
 public String getResult(){return result;} public void setResult(String v){result=v;} public String getErrorMessage(){return errorMessage;} public void setErrorMessage(String v){errorMessage=v;}
 public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public void touch(){updatedAt=Instant.now();}
 public Instant getStartedAt(){return startedAt;} public void setStartedAt(Instant v){startedAt=v;} public Instant getCompletedAt(){return completedAt;} public void setCompletedAt(Instant v){completedAt=v;}
}
