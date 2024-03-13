package com.favor.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.servlet.MultipartConfigElement;

/**
 * 1.SpringBootApplication注解，这个注解表示该类是一个 Spring Boot 应用。
 * 它是一个组合注解，它包含了 @SpringBootConfiguration、@EnableAutoConfiguration 和 @ComponentScan 这三个注解。
 * 其中，@ComponentScan 默认会扫描主应用程序类所在的包及其子包中的组件，并将其注册为 Spring Bean。
 * </p>
 * 2.MapperScan注解，扫描Mapper所在的文件夹
 * 直接运行 BookApplication 类即可启动，启动成功后在控制台输出信息，默认端口是 8080
 * @author Administrator
 */
@SpringBootApplication
// @MapperScan("com.favor.book.dao")
public class BookApplication {
    /**
     * 可以看到，我们只在 pom.xml 中引入了一个 Web 的 Starter，然后创建一个普通的 Java 类，一个 Main 方法就可以启动一个 Web 项目。
     * 与之前的使用方式相比，这种方式简单很多。以前需要配置各种 Spring 相关的包，还需要配置 web.xml 文件，还需要将项目放入 Tomcat 中去执行，搭建项目的过程还特别容易出错，会出现各种 jar 包冲突。有了 Spring Boot 后这些问题都解决了。
     * 我们之所以能够通过一个 Main 方法启动一个 Web 服务，是因为 Sprig Boot 中内嵌了 Tomcat，然后通过内嵌的 Tomcat 来提供服务。当然，我们也可以使用别的容器来替换 Tomcat，比如 Undertow 或 Jetty。
     */
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

    /**
     * 文件上传临时路径:<a href="https://blog.csdn.net/daniel7443/article/details/51620308">...</a>
     * 如果我们没有使用绝对路径的话，transferTo方法会在相对路径前添加一个location路径，即：file = new File(location, fileName);。当然，这也影响了SpringMVC的Multipartfile的使用。
     * 由于我们创建的File在项目路径/tmp/source/，而transferTo方法预期写入的文件路径为/tmp/tomcat.273391201583741210.8080/work/Tomcat/localhost/ROOT/tmp/source/，我们并没有创建该目录，因此会抛出异常。
     */
    @Bean
     MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("F:\\");
        return factory.createMultipartConfig();
    }

    /**
     * 使用QueryDSL的功能时，会依赖使用到JPAQueryFactory，而JPAQueryFactory在这里依赖使用EntityManager，所以在主类中做如下配置，使得Spring自动帮我们注入EntityManager与自动管理JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }

}
