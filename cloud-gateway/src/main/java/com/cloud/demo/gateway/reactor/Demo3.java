package com.cloud.demo.gateway.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 通常情况下，我们需要对源发布者发出的原始数据流进行多个阶段的处理，并最终得到我们需要的数据。这种感觉就像是一条流水线，从流水线的
 * 源头进入传送带的是原料，经过流水线上各个工位的处理，逐渐由原料变成半成品、零件、组件、成品，最终成为消费者需要的包装品。这其中，
 * 流水线源头的下料机就相当于源发布者，消费者就相当于订阅者，流水线上的一道道工序就相当于一个一个的操作符（Operator）
 *
 * @author : cuixiuyin
 * @date : 2019/8/10
 */
public class Demo3 {

    public static void main(String[] args) throws InterruptedException {
        // 1. Flux.range(1, 6) 用于生成从 "1" 开始的，自增为 1 的 "6" 个整型数据
        // 2. map 将元素映射为新元素
        Flux<Integer> flux = Flux.range(1, 6).map(integer -> integer * integer);
        flux.subscribe(System.out::print);
        System.out.println();

        // 3.flatMap 操作可以将每个数据元素转换/映射为一个流，然后将这些流合并为一个大的数据流
        Flux.just("flux", "mono")
                // 对于每一个字符串 str，将其拆分为包含一个字符的字符串流；
                .flatMap(str -> Flux.fromArray(str.split("\\s*"))).subscribe(e -> System.out.print(e));
        System.out.println();

        // 4.filter 过滤
        Flux.range(1, 6).filter(integer -> integer % 2 == 0).subscribe(e -> System.out.print(e));
        System.out.println();

        // 5.zip 能够将多个流一对一的合并起来
        // 在异步条件下，数据流的流速不同，使用 zip 能够一对一地将两个或多个数据流的元素对齐发出

        // 初始为1，则会等待执行1次 countDown方法，不使用它的话，测试方法所在的线程会直接返回而不会等待数据流发出完毕
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux.zip(
                getZipDescFlux(),
                // 使用Flux.interval 声明一个每 200ms 发出一个元素的 long 数据流；因为 zip 操作是一对一的，故而将其与字符串流 zip 之后，字符串流也将具有同样的速度；
                Flux.interval(Duration.ofMillis(200)))
                .subscribe(t -> System.out.print(t.getT1() + " "), null, countDownLatch::countDown);
        // 会等待countDown倒数至0，最多等待10秒钟。
        countDownLatch.await(10, TimeUnit.SECONDS);

        System.out.println();
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        getZipDescFlux().zipWith(Flux.interval(Duration.ofMillis(200))).subscribe(t -> System.out.print(t.getT1() + " "),
                null, countDownLatch1::countDown);
        // 会等待countDown倒数至0，最多等待10秒钟。
        countDownLatch1.await(10, TimeUnit.SECONDS);

        System.out.println();

        // 用于编程方式自定义生成数据流的 create 和 generate 等及其变体方法；
        // 用于"无副作用的peek"场景的 doOnNext、doOnError、doOncomplete、doOnSubscribe、doOnCancel等及其变体方法；
        // 用于数据流转换的 when、and/or、merge、concat、collect、count、repeat 等及其变体方法；
        // 用于过滤/拣选的 take、first、last、sample、skip、limitRequest 等及其变体方法；
        // 用于错误处理的 timeout、onErrorReturn、onErrorResume、doFinally、retryWhen 等及其变体方法；
        // 用于分批的 window、buffer、group 等及其变体方法；
        // 用于线程调度的 publishOn 和 subscribeOn 方法。

    }

    private static Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        // 将英文说明用空格拆分为字符串流
        return Flux.fromArray(desc.split("\\s+"));
    }
}
