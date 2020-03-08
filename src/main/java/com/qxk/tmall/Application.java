

package com.qxk.tmall;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.qxk.tmall.util.PortUtil;
@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.qxk.tmall.es")
@EnableJpaRepositories(basePackages = {"com.qxk.tmall.dao", "com.qxk.tmall.pojo"})
public class Application {
    static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
        //PortUtil.checkPort(9200,"ElasticSearch 服务端",true);
        //PortUtil.checkPort(5601,"Kibana 工具", true);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


