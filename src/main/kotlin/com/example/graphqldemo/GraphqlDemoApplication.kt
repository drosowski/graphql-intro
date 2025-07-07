package com.example.graphqldemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class GraphqlDemoApplication

fun main(args: Array<String>) {
    runApplication<GraphqlDemoApplication>(*args)
} 