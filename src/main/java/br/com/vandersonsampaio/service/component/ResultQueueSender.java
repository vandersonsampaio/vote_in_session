package br.com.vandersonsampaio.service.component;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ResultQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public ResultQueueSender(RabbitTemplate rabbitTemplate, Queue queue){
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void send(String result) {
        rabbitTemplate.convertAndSend(this.queue.getName(), result);
    }
}
