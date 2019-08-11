package com.cloud.demo.gateway.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

/**
 * 错误处理
 *
 * @author : cuixiuyin
 * @date : 2019/8/11
 */
public class Demo5 {

    public static void main(String[] args) {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                // 收到错误信号的时候，提供一个缺省值
                .onErrorReturn(0)
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                // 收到错误信号的时候，提供新的数据流
                .onErrorResume(e -> Mono.just(new Random().nextInt(6)))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        Flux.range(1, 6)
                .map(i -> i * i)
                // 我们收到异常后并不想立即处理，而是会包装成一个业务相关的异常交给后续的逻辑处理
                .onErrorMap(original -> new Exception("SLA exceeded", original));

        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                // 只读地拿到错误信息，错误信号会继续向下游传递
                .doOnError((throwable) -> {
                    throwable.printStackTrace();
                }).subscribe(System.out::println, System.err::println);


        // 用 LongAdder 进行统计
        LongAdder statsCancel = new LongAdder();

        Flux.just("foo", "bar")
                .doFinally(type -> {
                    // SignalType 检查了终止信号的类型
                    if (type == SignalType.CANCEL) {
                        statsCancel.increment();
                    }
                })
                // 能够在发出1个元素后取消流。
                .take(1).subscribe(System.out::print);
        System.out.println(statsCancel.intValue());
    }
}
