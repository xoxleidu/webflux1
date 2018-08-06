package com.imooc.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class Controller {

    @GetMapping("/getMono")
    private Mono<String> getMono(){

        log.info("get start");
        Mono<String> result = Mono.fromSupplier(() -> createStr());
        log.info("get end");
        return result;

    }

    @GetMapping(value = "/getFlux",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> getFlux(){
        Flux<String> stringFlux = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "flux data--> " + i;
        }));
        return stringFlux;
    }

    private String createStr() {

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "some string...";

    }

}
