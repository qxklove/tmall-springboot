

package com.qxk.tmall.dao;

import java.util.List;

import com.qxk.tmall.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.tmall.pojo.Order;
import com.qxk.tmall.pojo.OrderItem;
import com.qxk.tmall.pojo.User;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer>{
	List<OrderItem> findByOrderOrderByIdDesc(Order order);
	List<OrderItem> findByProduct(Product product);
	List<OrderItem> findByUserAndOrderIsNull(User user);
}


