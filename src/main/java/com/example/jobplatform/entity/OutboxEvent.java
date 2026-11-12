package com.example.jobplatform.entity;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="outbox_events")
public class OutboxEvent {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="aggregate_id",nullable=false) private UUID aggregateId;
 @Column(name="event_type",nullable=false) private String eventType;
 @Column(nullable=false,columnDefinition="text") private String payload;
 @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
 @Column(name="published_at") private Instant publishedAt;
 public Long getId(){return id;} public UUID getAggregateId(){return aggregateId;} public void setAggregateId(UUID v){aggregateId=v;}
 public String getEventType(){return eventType;} public void setEventType(String v){eventType=v;} public String getPayload(){return payload;} public void setPayload(String v){payload=v;}
 public Instant getPublishedAt(){return publishedAt;} public void setPublishedAt(Instant v){publishedAt=v;}
}
