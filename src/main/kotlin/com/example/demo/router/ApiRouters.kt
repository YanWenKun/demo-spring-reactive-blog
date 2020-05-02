package com.example.demo.router

import com.example.demo.handler.ArticleHandler
import com.example.demo.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

// 路由层，负责接收请求，并转发给 Handler
// 参考： https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-fn-router-functions
// 利用 Kotlin，路由可以写得非常简略，这里为了有易于理解，用了最啰嗦的风格。
// 如果不喜欢函数式的 Handler - Router 模式，还可以用 Spring Web 的 Controller 注解，Spring Boot 会自动将其配置为响应式。

@Configuration
class ApiRouters {

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
