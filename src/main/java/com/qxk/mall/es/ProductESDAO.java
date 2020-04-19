

package com.qxk.mall.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.qxk.mall.model.Product;

public interface ProductESDAO extends ElasticsearchRepository<Product,Integer>{

}


