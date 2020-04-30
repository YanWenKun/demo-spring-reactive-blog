package com.example.demo.repository

import com.example.demo.model.Article
import com.example.demo.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

// 注意，如果直接调用 findAll，则返回 Flux，而不是 Mono。
// 这也就是经典的同步问题。目前我找到的所有教程／样例均回避了这一问题，我也就不花时间硬上了。

interface ArticleRepository : ReactiveCrudRepository<Article, Long> {
    fun findBySlug(slug: String): Mono<Article?>
    fun findAllByOrderByPostTimeDesc(): Mono<Iterable<Article>>
}

interface UserRepository : ReactiveCrudRepository<User, Long> {
    fun findByUserName(login: String): Mono<User?>
    fun findAllByOrderByIdAsc() : Mono<Iterable<User>>
}
