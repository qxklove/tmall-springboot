

package com.qxk.tmall.dao;

import java.util.List;

import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer>{
	public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

}


