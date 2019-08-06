package com.cloud.demo.stream;

import com.cloud.demo.stream.input.SinkReceiver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author : cuixiuyin
 * @date : 2019/8/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StreamApplication.class)
@WebAppConfiguration
public class SinkOutputTest {

    @Autowired
    private Source source;
    @Autowired
    private SinkReceiver sinkReceiver;

    @Test
    public void source() {
        source.output().send(MessageBuilder.withPayload("From SinkSender").build());
    }

    @Test
    public void sinkReceiver() {
        sinkReceiver.processInput("Hello Stream");
    }
}
