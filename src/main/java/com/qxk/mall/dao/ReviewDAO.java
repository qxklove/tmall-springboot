

package com.qxk.mall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.Review;

public interface ReviewDAO extends JpaRepository<Review,Integer>{

	List<Review> findByProductOrderByIdDesc(Product product);
	int countByProduct(Product product);

}


