package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.ReviewMapper;
import com.qxk.mall.model.Product;
import com.qxk.mall.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="reviews")
public class ReviewService extends ServiceImpl<ReviewMapper, Review> {

	@Autowired ProductService productService;
    @Autowired
    UserService userService;

	@CacheEvict(allEntries=true)
    public void add(Review review) {
        this.save(review);
    }

	@Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product){
        List<Review> list = this.list(new QueryWrapper<Review>()
                .eq("pid", product.getId())
                .orderByDesc("id"));
        list.stream().forEach(review -> review.setUser(userService.getById(review.getUid())));
        return list;
    }

	@Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product) {
        return this.count(new QueryWrapper<Review>().eq("pid", product.getId()));
    }

}


