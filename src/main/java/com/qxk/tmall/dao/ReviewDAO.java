

package com.qxk.tmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.pojo.Review;

public interface ReviewDAO extends JpaRepository<Review,Integer>{

	List<Review> findByProductOrderByIdDesc(Product product);
	int countByProduct(Product product);

}


