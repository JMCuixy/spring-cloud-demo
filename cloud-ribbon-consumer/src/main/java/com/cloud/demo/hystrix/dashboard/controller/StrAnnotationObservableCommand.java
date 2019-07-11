package com.cloud.demo.hystrix.dashboard.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 */
@Controller
public class StrAnnotationObservableCommand {

    @Autowired
    private RestTemplate restTemplate;

    // EAGER 表示使用 observe () 执行方式
    // LAZY 表示使用 toObservable()执行方式
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER)
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
}
