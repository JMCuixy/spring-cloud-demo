package com.cloud.demo.hystrix.dashboard;

import com.cloud.demo.hystrix.dashboard.controller.StrObservableCommand;
import com.cloud.demo.hystrix.dashboard.controller.StrCommand;
import com.netflix.hystrix.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerApplication.class)
public class StrCommandTest {

    @Autowired
    private RestTemplate restTemplate;

    public void test1() throws ExecutionException, InterruptedException {
        // 1. Hystrix 会根据组来组织和统计命令的告警、仪表盘等信息
        // 2. 默认情况下， Hystrix 会让相同组名的命令使用同一个线程池
        // 3. 可以通过 HystrixThreadPoolKey 对线程池进行设置，通过它我们可以实现更细粒度的线程池划分。
        // 4. 通常情况下，尽量通过 HystrixThreadPoolKey 的方式来指定线程池的划分，而不是通过组名的默认方式实现划分，因为多个不同的命令可能从业务逻辑上来看属于同一个组，但是往往从实现本身上需要跟其他命令进行隔离。
        HystrixCommand.Setter setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("strGroupCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("strCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("strThreadPool"));

        // 配置信号量隔离
        HystrixCommandProperties.Setter commandPropertiesSetter = HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE);
        setter.andCommandPropertiesDefaults(commandPropertiesSetter);

        StrCommand strCommand = new StrCommand(setter, restTemplate);
        // 1. 同步执行
        String execute = strCommand.execute();
        // 2. 异步执行
        Future<String> future = strCommand.queue();
        //通过调用 get 方法获取结果
        String queue = future.get();
    }

    public void test2() {
        HystrixObservableCommand.Setter setter = HystrixObservableCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("strGroupObservableCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("strObservableCommand"));
        StrObservableCommand observableCommand = new StrObservableCommand(setter, restTemplate);

        // 1. 返回的是一个 Hot Obserable, 该命令会在 observe ()调用的时候立即执行，当 Observable 每次被订阅的时候会重放它的行为；
        Observable<String> observable = observableCommand.observe();
        // 2. 返回的是一个 Cold Observable, toObservable() 执行之后，命令不会被立即执行，只有当所有订阅者都订阅它之后才会执行。
        Observable<String> toObservable = observableCommand.toObservable();
    }
}
