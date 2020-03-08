

package com.qxk.mall.dao;

import java.util.List;

import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer>{
	public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

}


