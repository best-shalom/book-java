package com.favor.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.SpringBootApplication注解，这个注解表示该类是一个 Spring Boot 应用。
 * 2.MapperScan注解，扫描Mapper所在的文件夹
 * 直接运行 BookApplication 类即可启动，启动成功后在控制台输出信息，默认端口是 8080
 */
@SpringBootApplication
@MapperScan("com.favor.book.dao")
public class BookApplication {
    /**
     * 可以看到，我们只在 pom.xml 中引入了一个 Web 的 Starter，然后创建一个普通的 Java 类，一个 Main 方法就可以启动一个 Web 项目。
     * 与之前的使用方式相比，这种方式简单很多。以前需要配置各种 Spring 相关的包，还需要配置 web.xml 文件，还需要将项目放入 Tomcat 中去执行，搭建项目的过程还特别容易出错，会出现各种 jar 包冲突。有了 Spring Boot 后这些问题都解决了。
     * 我们之所以能够通过一个 Main 方法启动一个 Web 服务，是因为 Sprig Boot 中内嵌了 Tomcat，然后通过内嵌的 Tomcat 来提供服务。当然，我们也可以使用别的容器来替换 Tomcat，比如 Undertow 或 Jetty。
     */
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

}
