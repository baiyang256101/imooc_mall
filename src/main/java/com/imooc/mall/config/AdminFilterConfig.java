package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 描述：  Admin 过滤器配置
 */
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter() {
        return new AdminFilter();
    }

    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(adminFilter());

        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.addUrlPatterns("/admin/category/*");
        registrationBean.addUrlPatterns("/admin/product/*");
        registrationBean.addUrlPatterns("/admin/order/*");
        registrationBean.setName("adminFilterConfig");
        return registrationBean;
    }


}
