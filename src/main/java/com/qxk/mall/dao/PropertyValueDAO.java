

package com.qxk.mall.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.Property;
import com.qxk.mall.pojo.PropertyValue;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer>{

	List<PropertyValue> findByProductOrderByIdDesc(Product product);
	PropertyValue getByPropertyAndProduct(Property property, Product product);




}


