package com.example.demo.mock

import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.repository.ArticleRepository
import com.example.demo.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseInit {

    /**
     * 构造一些不疼不痒的数据作为数据库初始化内容
     */
    @Bean
    fun databaseInitializer(userRepository: UserRepository,
                            articleRepository: ArticleRepository) = ApplicationRunner {

        val userYan = userRepository.save(User(
                "user_yan", "YAN WK", "Hey! It's always good time!", 1)).block()

        articleRepository.save(Article(
                title = "硅基生命被发现！",
                digest = "硅基生命被发现！但是不在太阳系",
                content = "也许周围还有黑洞",
                author = userYan,
                id = 1
        ))

        articleRepository.save(Article(
                title = "铁基生命被发现！",
                digest = "铁基生命被发现！聚变性质十分稳定！",
                content = "也许是最熵生物",
                author = userYan,
                id = 2
        ))

    }

}
