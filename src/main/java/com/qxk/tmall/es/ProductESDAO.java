

package com.qxk.tmall.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.qxk.tmall.pojo.Product;

public interface ProductESDAO extends ElasticsearchRepository<Product,Integer>{

}


