package com.favor.book.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

/**
 * @author CQ
 * @version 1.0
 * @date 2024/7/12 17:34
 * @description Elastic搜索框架工具类
 */

public class ElasticSearchUtil {
    // ES地址
    final String serverUrl = "http://localhost:9200";
    // RestClient 是 ElasticSearch 提供的低级 REST 客户端
    final RestClient restClient = RestClient
            .builder(HttpHost.create(serverUrl))
            .build();
    // 创建一个传输对象，使用 RestClientTransport，并传入 Jackson JSON 映射器。
    final ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());
    // 使用传输对象创建一个 Elasticsearch API 客户端。
    final ElasticsearchClient esClient = new ElasticsearchClient(transport);

    public static void main(String[] args) throws IOException {

        ElasticSearchUtil es = new ElasticSearchUtil();
        // 执行一个搜索请求，查询索引为 megacorp 的文档，查找 about 字段中包含 "love" 的文档。
        SearchResponse<Object> search = es.esClient.search(s -> s
                        .index("megacorp")
                        .query(q -> q
                                .term(t -> t
                                        .field("about")
                                        .value(v -> v.stringValue("love"))
                                )),
                Object.class);
        System.out.println(search);
    }
}


