package com.cloud.demo.gateway.reactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * 在订阅（subscribe）时才触发数据流，这种数据流叫做 “冷” 数据流，就像插座插上电器才会有电流一样，还有一种数据流不管是否有订阅者订阅它都会一直发出数据，称之为“热”数据流，Reactor中几乎都是“冷”数据流；
 *
 * @author : cuixiuyin
 * @date : 2019/8/11
 */
public class Demo6 {

    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 6)
                .map(i -> 10 / (3 - i))
                // retry 对于上游Flux是采取的重订阅（re-subscribing）的方式，因此重试之后实际上已经一个不同的序列了，发出错误信号的序列仍然是终止了的。
                .retry(1)
                .subscribe(System.out::println, System.err::println);
        Thread.sleep(100);

        // Flux.range 是一个快的Publisher
        Flux.range(1, 6)
                // 在每次 request 的时候打印 request 个数
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))
                // 通过重写 BaseSubscriber的方法来自定义Subscriber
                .subscribe(new BaseSubscriber<Integer>() {

                    // hookOnSubscribe定义在订阅的时候执行的操作；
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed and make a request...");
                        // 订阅时首先向上游请求1个元素；
                        request(1);
                    }

                    // hookOnNext 定义每次在收到一个元素的时候的操作
                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            // sleep 1秒钟来模拟慢的Subscriber；
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 打印收到的元素
                        System.out.println("Get value [" + value + "]");
                        // 每次处理完1个元素后再请求1个
                        request(1);
                    }
                });
    }
}
