package com.favor.book.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.instrumentation.OpenTelemetryForElasticsearch;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.favor.book.entity.BookDocument;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author CQ
 * @version 1.0
 * @date 2024/7/15 19:33
 * @description Elasticsearch连接配置
 */

@Configuration
public class ElasticsearchConfig {
    @Bean
    public static ElasticsearchClient createClient(){
        // 创建低级客户端
        RestClient restClient=RestClient.builder(
                new HttpHost("localhost",9200,"http")
        ).build();
        // 配置OpenTelemetrySdk的实例
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();
        // OpenTelemetry openTelemetry = OpenTelemetry.noop();
        // try {
        //     // Parse configuration file to configuration model
        //     OpenTelemetryConfiguration configurationModel = FileConfiguration.parse(new File("/app/sdk-config.yaml"));
        //     // Create SDK components from configuration model
        //     openTelemetry = FileConfiguration.create(configurationModel);
        // } catch (Throwable e) {
        //     System.out.println("Error initializing SDK from configuration file");
        // }

        //使用自定义openTelemetry实例创建Instrumentation实例
        //第二个构造函数参数允许启用/禁用搜索正文捕获
        OpenTelemetryForElasticsearch esOtelInstrumentation =
                new OpenTelemetryForElasticsearch(openTelemetry, true);

        // 基于低级客户端创建java api高级客户端
        // 使用自定义Instrumentation实例创建传输
        ElasticsearchTransport transport=new RestClientTransport(
                restClient,new JacksonJsonpMapper(),null,esOtelInstrumentation
        );
        return new ElasticsearchClient(transport);
    }
}
