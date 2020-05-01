package com.example.demo.mock

import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.repository.ArticleRepository
import com.example.demo.repository.UserRepository
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class DatabaseInit {

    private val log: Logger = LoggerFactory.getLogger(DatabaseInit::class.java)

    /**
     * 执行 SQL，以进行数据库的建表操作
     * R2DBC 这套自然是不会像 JPA 一样帮着建表的，所以还得自己来
     * 参考： https://docs.spring.io/spring-data/r2dbc/docs/current-SNAPSHOT/reference/html/#r2dbc.init
     */
    @Bean
    fun databaseInitializer(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("sql/schema.sql")))
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("sql/data.sql")))
        initializer.setDatabasePopulator(populator)
        log.info("开始数据库初始化")
        return initializer
    }

    /**
     * 构造一些不疼不痒的数据作为数据库初始化内容
     * 注意这里没有、也不能指定 id
     * 想要更细化的控制，可以直接写 SQL（利用初始化中用到的 SQL 文件）
     */
    @Bean
    fun databaseFiller(userRepository: UserRepository,
                       articleRepository: ArticleRepository) = ApplicationRunner {

        val userY = userRepository.save(User(
                "user_y", "Y", "Hey! It's always good time!"))
                .block()
        log.debug("存入一个示例用户")

        articleRepository.save(Article(
                title = "硅基生命被发现！",
                digest = "硅基生命被发现！但是不在太阳系",
                content = "也许周围还有黑洞",
                author = userY
        ))
        log.debug("存入一篇示例文章")

        articleRepository.save(Article(
                title = "铁基生命被发现！",
                digest = "铁基生命被发现！聚变性质十分稳定！",
                content = "也许是最熵生物",
                author = userY
        ))
        log.debug("存入另一篇示例文章")

        log.info("数据表填充完成")
    }

}
