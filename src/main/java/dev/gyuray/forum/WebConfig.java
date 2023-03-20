package dev.gyuray.forum;

import dev.gyuray.forum.controller.LoginButtonsInterceptor;
import dev.gyuray.forum.controller.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(0)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/posts", "/users/login", "/users/logout",
                        "/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginButtonsInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

    }
}
