

package com.qxk.mall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qxk.mall.pojo.Order;
import com.qxk.mall.pojo.User;

public interface OrderDAO extends JpaRepository<Order,Integer>{
    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}


