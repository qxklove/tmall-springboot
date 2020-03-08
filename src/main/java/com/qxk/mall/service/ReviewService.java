

package com.qxk.mall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.qxk.mall.dao.ReviewDAO;
import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.Review;

@Service
@CacheConfig(cacheNames="reviews")
public class ReviewService {

	@Autowired ReviewDAO reviewDAO;
	@Autowired ProductService productService;

	@CacheEvict(allEntries=true)
    public void add(Review review) {
    	reviewDAO.save(review);
    }

	@Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product){
        List<Review> result =  reviewDAO.findByProductOrderByIdDesc(product);
        return result;
    }

	@Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product) {
    	return reviewDAO.countByProduct(product);
    }

}


