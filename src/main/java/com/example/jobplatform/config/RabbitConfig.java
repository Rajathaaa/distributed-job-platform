package com.example.jobplatform.config;
import org.springframework.amqp.core.*; import org.springframework.amqp.rabbit.connection.ConnectionFactory; import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory; import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter; import org.springframework.context.annotation.*;
@Configuration public class RabbitConfig {
 public static final String EXCHANGE="jobs.exchange", QUEUE="jobs.queue", ROUTING="jobs.execute", RETRY_EXCHANGE="jobs.retry.exchange", RETRY_QUEUE="jobs.retry.queue", DLX="jobs.dlx", DLQ="jobs.dead.queue";
 @Bean DirectExchange jobsExchange(){return new DirectExchange(EXCHANGE);}
 @Bean DirectExchange retryExchange(){return new DirectExchange(RETRY_EXCHANGE);}
 @Bean DirectExchange deadExchange(){return new DirectExchange(DLX);}
 @Bean Queue jobsQueue(){return QueueBuilder.durable(QUEUE).maxPriority(10).deadLetterExchange(DLX).deadLetterRoutingKey("dead").build();}
 @Bean Queue retryQueue(){return QueueBuilder.durable(RETRY_QUEUE).ttl(10000).deadLetterExchange(EXCHANGE).deadLetterRoutingKey(ROUTING).build();}
 @Bean Queue deadQueue(){return QueueBuilder.durable(DLQ).build();}
 @Bean Binding mainBinding(){return BindingBuilder.bind(jobsQueue()).to(jobsExchange()).with(ROUTING);}
 @Bean Binding retryBinding(){return BindingBuilder.bind(retryQueue()).to(retryExchange()).with("retry");}
 @Bean Binding deadBinding(){return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead");}
 @Bean Jackson2JsonMessageConverter converter(){return new Jackson2JsonMessageConverter();}
 @Bean SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory cf,Jackson2JsonMessageConverter converter){var f=new SimpleRabbitListenerContainerFactory();f.setConnectionFactory(cf);f.setMessageConverter(converter);f.setConcurrentConsumers(4);f.setMaxConcurrentConsumers(8);f.setPrefetchCount(1);f.setDefaultRequeueRejected(false);return f;}
}
