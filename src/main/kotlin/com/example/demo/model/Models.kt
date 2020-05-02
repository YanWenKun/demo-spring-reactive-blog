package com.example.demo.model

import com.example.demo.util.generateSlug
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

// R2DBC 自然是不能像 JPA 一样直接进行高层抽象的

// Kotlin 中的 data class 类似于 Lombok 中的 @Data
// 会自动补充类的 equals() 、 toString() 等方法
// 并免于编写 getter 、 setter

data class Article(
        var title: String,
        var digest: String, // 文章摘要
        var content: String,
        var author: Long, // JPA 在这里可以直接指向 User? 类，但 R2DBC 自然用不了 @ManyToOne，需要另外处理外键。
        var postTime: LocalDateTime = LocalDateTime.now(),
        var slug: String = generateSlug(postTime, title),
        @Id var id: Long? = null // R2DBC 自然用不了 @GeneratedValue
)

data class User(
        var userName: String,
        var displayName: String,
        var description: String? = null,
        @Id var id: Long? = null // R2DBC 自然用不了 @GeneratedValue
)
