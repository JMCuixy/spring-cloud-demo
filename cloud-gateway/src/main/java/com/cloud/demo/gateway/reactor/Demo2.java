package com.cloud.demo.gateway.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author : cuixiuyin
 * @date : 2019/8/10
 */
public class Demo2 {

    public static void main(String[] args) {
        Flux<Integer> flux = Flux.just(1, 2, 3);
        // StepVerifier 断言
        Duration fluxMonoVerify = StepVerifier.create(flux)
                // 下一个测试的期望值
                .expectNext(1, 2, 3)
                // 下一个是否是完成信号
                .expectComplete()
                .verify();

        Mono<Object> mono = Mono.error(new IllegalAccessError("IllegalAccessError"));
        Duration monoVerify = StepVerifier.create(mono)
                // 断言错误信息
                .expectErrorMessage("IllegalAccessError")
                .verify();
    }
}
