package com.imooc.webflux.swagger;

import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 开启swagger文档自动生成功能

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import(SwaggerConfiguration.class)//组合注解
//@EnableSwagger2//组合注解
public @interface EnableMySwagger {
}


 */