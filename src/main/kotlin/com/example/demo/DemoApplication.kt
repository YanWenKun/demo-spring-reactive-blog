package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication // Kotlin语法：类在声明时如果是空的，那就不需要花括号

fun main(args: Array<String>) {

    runApplication<DemoApplication>(*args) // 等价于 SpringApplication.run(BlogApplication::class.java, *args)

    val log = org.slf4j.LoggerFactory.getLogger(DemoApplication::class.java)
    log.error("显示ERROR级日志")
    log.warn("显示WARN级日志")
    log.info("显示INFO级日志")
    log.debug("显示DEBUG级日志")
    log.trace("显示TRACE级日志")

}
