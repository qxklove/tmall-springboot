

package com.qxk.mall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qxk.mall.dao.OrderDAO;
import com.qxk.mall.pojo.Order;
import com.qxk.mall.pojo.OrderItem;
import com.qxk.mall.pojo.User;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.SpringContextUtil;

@Service
@CacheConfig(cacheNames="orders")
public class OrderService {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";

	@Autowired OrderDAO orderDAO;

	@Autowired OrderItemService orderItemService;



	public List<Order> listByUserWithoutDelete(User user) {
		OrderService orderService = SpringContextUtil.getBean(OrderService.class);
		List<Order> orders = orderService.listByUserAndNotDeleted(user);
		orderItemService.fill(orders);
		return orders;
	}


	@Cacheable(key="'orders-uid-'+ #p0.id")
	public List<Order> listByUserAndNotDeleted(User user) {
		return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
	}



	@CacheEvict(allEntries=true)
	public void update(Order bean) {
		orderDAO.save(bean);
	}

	@Cacheable(key="'orders-page-'+#p0+ '-' + #p1")
	public Page4Navigator<Order> list(int start, int size, int navigatePages) {
    	Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = new PageRequest(start, size,sort);
		Page pageFromJPA =orderDAO.findAll(pageable);
		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}

	@CacheEvict(allEntries=true)
	public void add(Order order) {
		orderDAO.save(order);
	}

	@CacheEvict(allEntries=true)
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> ois) {
        float total = 0;
        add(order);

        if(false)
            throw new RuntimeException();

        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

	@Cacheable(key="'orders-one-'+ #p0")
	public Order get(int oid) {
		return orderDAO.findOne(oid);
	}


	public void cacl(Order o) {
		List<OrderItem> orderItems = o.getOrderItems();
		float total = 0;
		for (OrderItem orderItem : orderItems) {
			total+=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
		}
		o.setTotal(total);
	}

	public void removeOrderFromOrderItem(List<Order> orders) {
		for (Order order : orders) {
			removeOrderFromOrderItem(order);
		}
	}

	public void removeOrderFromOrderItem(Order order) {
		List<OrderItem> orderItems= order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrder(null);
		}
	}

}


