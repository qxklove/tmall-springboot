

package com.qxk.tmall.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.pojo.Property;
import com.qxk.tmall.pojo.PropertyValue;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer>{

	List<PropertyValue> findByProductOrderByIdDesc(Product product);
	PropertyValue getByPropertyAndProduct(Property property, Product product);




}


