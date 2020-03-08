

package com.qxk.mall.dao;

import java.util.List;

import com.qxk.mall.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.mall.pojo.Order;
import com.qxk.mall.pojo.OrderItem;
import com.qxk.mall.pojo.User;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer>{
	List<OrderItem> findByOrderOrderByIdDesc(Order order);
	List<OrderItem> findByProduct(Product product);
	List<OrderItem> findByUserAndOrderIsNull(User user);
}


