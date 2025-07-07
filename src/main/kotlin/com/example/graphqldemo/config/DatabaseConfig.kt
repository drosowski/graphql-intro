package com.example.graphqldemo.config

import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource
import org.jooq.SQLDialect

@Configuration
class DatabaseConfig {

    @Bean
    fun connectionProvider(dataSource: DataSource) =
        DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))

    @Bean
    fun dsl(dataSource: DataSource) = DefaultDSLContext(configuration(dataSource))

    private fun configuration(dataSource: DataSource) = DefaultConfiguration().apply {
        set(connectionProvider(dataSource))
        set(SQLDialect.POSTGRES)
    }
} 