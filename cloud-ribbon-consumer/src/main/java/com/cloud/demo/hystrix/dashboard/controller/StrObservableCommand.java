package com.cloud.demo.hystrix.dashboard.controller;

import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 */
public class StrObservableCommand extends HystrixObservableCommand<String> {

    private RestTemplate restTemplate;

    public StrObservableCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
                        subscriber.onNext(result.getBody());
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    // 定义回调降级函数，当命令执行失败的时候，Hystrix 会将 Observable 中的结果通知给所有的订阅者。
    @Override
    protected Observable<String> resumeWithFallback() {
        return Observable.just("fallback return");
    }
}
