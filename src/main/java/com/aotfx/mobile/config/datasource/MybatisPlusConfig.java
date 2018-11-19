package com.aotfx.mobile.config.datasource;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@EnableTransactionManagement
@Configuration
@MapperScan("com.aotfx.mobile.dao.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 打印 sql
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //格式化sql语句
        Properties properties = new Properties();
        properties.setProperty("format", "true");
        performanceInterceptor.setProperties(properties);
        return performanceInterceptor;
    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
////        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
////        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
////
////        sqlSessionFactoryBean.setDataSource(druidDataSource);
////        org.apache.ibatis.session.Configuration cfg = new org.apache.ibatis.session.Configuration();//configuration
////        logger.info("sqlSessionFactoryBean:-->" + sqlSessionFactoryBean.getObject());
////        logger.info("default-statement-timeout:" + dst);
////        sqlSessionFactoryBean.setConfiguration(cfg);
////        return sqlSessionFactoryBean.getObject();
//        return null;
//
//
//    }
}
