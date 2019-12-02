package com.custom.gateway.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 */
@Configuration
public class DruidConfiguration {
    private static final String DB_PREFIX = "spring.datasource";

    @ConfigurationProperties(prefix = DB_PREFIX)
    class IDataSourceProperties {
        @NacosValue("${spring.datasource.type}")
        private String type;
        @NacosValue("${spring.datasource.url}")
        private String url;
        @NacosValue("${spring.datasource.driverClassName}")
        private String driverClassName;
        @NacosValue("${spring.datasource.username}")
        private String userName;
        @NacosValue("${spring.datasource.password}")
        private String passwrod;
        @NacosValue("${spring.datasource.initialSize}")
        private Integer initialSize;
        @NacosValue("${spring.datasource.minIdle}")
        private Integer minIdle;
        @NacosValue("${spring.datasource.maxActive}")
        private Integer maxActive;
        @NacosValue("${spring.datasource.timeBetweenEvictionRunsMillis}")
        private Long timeBetweenEvictionRunsMillis;
        @NacosValue("${spring.datasource.minEvictableIdleTimeMillis}")
        private Long minEvictableIdleTimeMillis;

        @Bean
        @Primary
        public DataSource dataSource() {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDbType(type);
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(userName);
            dataSource.setPassword(passwrod);
            dataSource.setInitialSize(initialSize);
            dataSource.setMinIdle(minIdle);
            dataSource.setMaxActive(maxActive);
            dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            dataSource.setTestWhileIdle(false);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(200);
            return dataSource;
        }


    }

}