package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            //判断是否延时消息
            if (message.getMessageProperties().getReceivedDelay() > 0) {
                return;
            }
            log.error("消息发送到队列失败，应答码:{}，原因:{}，交换机:{}，路由key:{}，消息:{}",
                    replyCode,replyText,exchange,routingKey,message.toString());
        //    有需要可对消息进行重发
        });
    }
}
