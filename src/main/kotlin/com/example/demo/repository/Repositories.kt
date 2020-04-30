package com.example.demo.repository

import com.example.demo.model.Article
import com.example.demo.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ArticleRepository : ReactiveCrudRepository<Article, Long> {
    fun findBySlug(slug: String): Mono<Article?>
    fun findAllByOrderByPostTimeDesc(): Mono<Iterable<Article>>
}

interface UserRepository : ReactiveCrudRepository<User, Long> {
    fun findByUserName(login: String): Mono<User?>
}
