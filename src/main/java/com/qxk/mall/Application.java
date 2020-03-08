

package com.qxk.mall;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.qxk.mall.util.PortUtil;
@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.qxk.mall.es")
@EnableJpaRepositories(basePackages = {"com.qxk.mall.dao", "com.qxk.mall.pojo"})
public class Application {
    static {
        //PortUtil.checkPort(6379,"Redis 服务端",true);
        //PortUtil.checkPort(9200,"ElasticSearch 服务端",true);
        //PortUtil.checkPort(5601,"Kibana 工具", true);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


