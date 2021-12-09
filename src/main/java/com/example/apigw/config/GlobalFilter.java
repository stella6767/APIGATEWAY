package com.example.apigw.config;


import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalFilter.class);

    public GlobalFilter() {

        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        //Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();  //reactive import!
            ServerHttpResponse response = exchange.getResponse();

            logger.info("Global Filter: bassMsg -> {}", config.getBaseMsg());

            if (config.isPreLogger()){
                logger.info("Global filter start: request id -< {}", request.getId());
            }

            //Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{

                if (config.isPostLogger()){
                    logger.info("Global filter end: response code->{}", response.getStatusCode());
                }

            }));
        };
    }


    @Data
    public static class Config {
        private String baseMsg;
        private boolean preLogger;
        private boolean postLogger;

    }


}
