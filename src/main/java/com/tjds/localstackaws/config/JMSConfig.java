package com.tjds.localstackaws.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@Configuration
@EnableJms
public class JMSConfig {

    private SQSConnectionFactory connectionFactory;

    @Autowired
    private AmazonSQSAsync amazonSQSAsync;

    @PostConstruct
    public void init() {
        connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(), amazonSQSAsync);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Qualifier("response-executor") Executor topicExecutor) {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setTaskExecutor(topicExecutor);
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new JmsTemplate(connectionFactory);
    }

}
