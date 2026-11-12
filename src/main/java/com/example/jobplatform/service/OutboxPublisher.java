package com.example.jobplatform.service;
import com.example.jobplatform.config.RabbitConfig; import com.example.jobplatform.dto.JobDtos.JobMessage; import com.example.jobplatform.entity.OutboxEvent; import com.example.jobplatform.repository.OutboxRepository; import com.fasterxml.jackson.databind.ObjectMapper; import org.springframework.amqp.rabbit.core.RabbitTemplate; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component;
@Component public class OutboxPublisher {
 private final OutboxRepository outbox;private final RabbitTemplate rabbit;private final ObjectMapper mapper;
 public OutboxPublisher(OutboxRepository o,RabbitTemplate r,ObjectMapper m){outbox=o;rabbit=r;mapper=m;}
 @Scheduled(fixedDelayString="${app.jobs.outbox-poll-ms:1000}") public void publish(){for(OutboxEvent e:outbox.findPending())try{JobMessage msg=mapper.readValue(e.getPayload(),JobMessage.class);rabbit.convertAndSend(RabbitConfig.EXCHANGE,RabbitConfig.ROUTING,msg,m-> {m.getMessageProperties().setPriority(msg.priority());m.getMessageProperties().setMessageId(msg.jobId().toString());return m;});outbox.markPublished(e.getId());}catch(Exception ex){/* leave unpublished; next poll retries */}}
}
