package com.cloud.demo.gateway.reactor;

import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author : cuixiuyin
 * @date : 2019/8/10
 */
public class Demo1 {

    public static void main(String[] args) {
        flux();
        mono();
    }

    private static void flux() {
        // 1. just 方式声明数据流
        Flux<Integer> flux1 = Flux.just(1, 2, 3, 4, 5, 6);
        // 2. array 方式声明数据流
        Integer[] array = {1, 2, 3, 4, 5, 6};
        Flux<Integer> flux2 = Flux.fromArray(array);
        // 3. Collection 方式声明数据流
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        Flux<Integer> flux3 = Flux.fromIterable(list);
        // 4. Stream 的方式
        Flux<Integer> flux4 = Flux.fromStream(list.stream());
        // 5. 只有完成信号的空数据量
        Flux.just();
        Flux.empty();
        // 6. 只有错误信号的数据流
        Flux.error(new IllegalAccessError("illegal access error"));

        /*订阅者*/
        flux1.subscribe(e -> System.out.print(e));
        System.out.println();

        flux1.subscribe(
                // 输出元素
                System.out::println,
                // 接收到错误信号时输出
                System.err::println,
                // 接收到完成信号时输出
                () -> System.out.println("completed!"));
    }

    private static void mono() {
        // 1. just 方式声明数据流
        Mono<Integer> mono1 = Mono.just(1);
        // 2. 只有完成信号的空数据量
        Mono.empty();
        Mono.justOrEmpty(Optional.empty());
        // 3. 只有错误信号的数据流
        Mono.error(new IllegalAccessError("illegal access error"));

        /*订阅者*/
        mono1.subscribe(e-> System.out.print(e));
        System.out.println();

        mono1.subscribe(e-> System.out.println(e),
                e -> e.printStackTrace(),
                () -> System.out.println("元素输出完成")
                );
    }
}
