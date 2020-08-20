package com.example.bookManager.configuration;


import com.example.bookManager.interceptor.HostInfoInterceptor;
import com.example.bookManager.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器的配置文件, 需要实现WebMvcConfigurer接口
 */
@Component
public class BookWebConfiguration implements WebMvcConfigurer {

  @Autowired
  private LoginInterceptor loginInterceptor; // 登录的拦截器

  @Autowired
  private HostInfoInterceptor hostInfoInterceptor; // 身份认证的拦截器

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      /**
       * 添加拦截器
       */
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hostInfoInterceptor).addPathPatterns("/**"); //添加拦截器, 设置拦截范围
        registry.addInterceptor(loginInterceptor).addPathPatterns("/books/**");
      }
    };
  }

}
