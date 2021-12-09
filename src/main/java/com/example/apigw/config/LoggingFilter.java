package com.example.apigw.config;


import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {

        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

//        //Custom Pre Filter
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();  //reactive import!
//            ServerHttpResponse response = exchange.getResponse();
//
//            logger.info("Logging Filter: bassMsg -> {}", config.getBaseMsg());
//
//            if (config.isPreLogger()){
//                logger.info("Logging filter start: request uri -< {}", request.getURI());
//            }
//
//            //Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//
//                if (config.isPostLogger()){
//                    logger.info("Logging filter end: response code->{}", response.getStatusCode());
//                }
//
//            }));
//        };

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();  //reactive import!
            ServerHttpResponse response = exchange.getResponse();

            logger.info("Logging Filter: bassMsg -> {}", config.getBaseMsg());

            if (config.isPreLogger()){
                logger.info("Logging filter start: request uri -< {}", request.getURI());
            }

            //Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{

                if (config.isPostLogger()){
                    logger.info("Logging filter end: response code->{}", response.getStatusCode());
                }

            }));
        }, Ordered.HIGHEST_PRECEDENCE); //우선순위 조절

        return filter;
    }


    @Data
    public static class Config {
        private String baseMsg;
        private boolean preLogger;
        private boolean postLogger;

    }


}
