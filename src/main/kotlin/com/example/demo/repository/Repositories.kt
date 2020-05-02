package com.example.demo.repository

import com.example.demo.model.Article
import com.example.demo.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// findAll() 这类查询显然应该返回 Flux，而不是 Mono。
// 这也带来经典的同步问题。很多教程／样例回避了这一问题，直接返回 Mono<>，
// 相当于： 数据库异步查询 -> 数据访问层异步接收，并 *等待* 全部返回完毕 -> 打包成一个完整的数据包，传给下一个异步流程
// 这与异步、流、响应式的初衷有悖。
// 本项目中使用 .collectList() 来消费 Flux（变为 Mono），本质上并不是完整的全流程响应式，只是比上述例子更靠前一点。
// 相当于： 数据库异步查询 -> 数据访问层异步接收，并流式传递给控制层 -> 控制层 *等待* 全部返回完毕 -> 打包成一个完整的 HTTP 响应，传给路由层

interface ArticleRepository : ReactiveCrudRepository<Article, Long> {
    fun findBySlug(slug: String): Mono<Article?>
    fun findAllByOrderByPostTimeDesc(): Flux<Article>
}

interface UserRepository : ReactiveCrudRepository<User, Long> {
    fun findByUserName(login: String): Mono<User?>
    fun findAllByOrderByIdAsc(): Flux<User>
}
