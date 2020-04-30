package com.example.demo.router

import com.example.demo.handler.ArticleHandler
import com.example.demo.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class HttpRouters {

    @Bean
    fun articleRouter(articleHandler: ArticleHandler): RouterFunction<ServerResponse> {
        return RouterFunctions.route(
                RequestPredicates.GET("/api/article").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                HandlerFunction { request: ServerRequest -> articleHandler.findAll(request) }
        ).and(RouterFunctions.route(
                RequestPredicates.GET("/api/article/{slug}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                HandlerFunction { request: ServerRequest -> articleHandler.findOne(request) }
        ))
    }

    @Bean
    fun userRouter(userHandler: UserHandler): RouterFunction<ServerResponse> {
        return RouterFunctions.route(
                RequestPredicates.GET("/api/user").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                HandlerFunction { request: ServerRequest -> userHandler.findAll(request) }
        ).and(RouterFunctions.route(
                RequestPredicates.GET("/api/user/{userName}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                HandlerFunction { request: ServerRequest -> userHandler.findOne(request) }
        ))
    }

}
