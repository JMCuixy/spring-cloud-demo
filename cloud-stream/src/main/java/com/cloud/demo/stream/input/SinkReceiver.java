package com.cloud.demo.stream.input;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author : cuixiuyin
 * @date : 2019/7/31
 */
// 1. 该注解用来指定一个或多个定义了 @input 或 @Output 注解的接口，以此实现对消息通道(Channel) 的绑定。
// 2. Sink 接口是 Spring Cloud Stream 中默认实现的对输入消息通道绑定的定义。
// 3. Sink 接口绑定输入，Source 接口绑定输出。Processor 接口同时绑定输入输出。

@EnableBinding({Source.class, Sink.class})
public class SinkReceiver {

    private Logger log = LoggerFactory.getLogger(SinkReceiver.class);

    // 定义在方法上，作用是将被修饰的方法注册为消息中间件上数据流的事件监听器，注解中的属性值对应了监听的消息通道名。
    // 如果不设置 value，将默认使用方法名作为消息通道名

    @StreamListener(Sink.INPUT)
    @SendTo(Source.OUTPUT)
    public Object processInput(String message) {
        log.info("Input Stream Receiver:{}", message);
        return message;
    }

    @StreamListener(Source.OUTPUT)
    public void processOutPut(String message) {
        log.info("Output Stream Receiver:{}", message);
    }


}
