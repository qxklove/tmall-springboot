package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.OrderMapper;
import com.qxk.mall.mapper.UserMapper;
import com.qxk.mall.model.Order;
import com.qxk.mall.model.OrderItem;
import com.qxk.mall.model.User;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.PageUtil;
import com.qxk.mall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames="orders")
public class OrderService extends ServiceImpl<OrderMapper, Order> {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";

	@Autowired
	OrderItemService orderItemService;
	@Autowired
	UserMapper userMapper;

	public List<Order> listByUserWithoutDelete(User user) {
		OrderService orderService = SpringContextUtil.getBean(OrderService.class);
		List<Order> orders = orderService.listByUserAndNotDeleted(user);
		orderItemService.fill(orders);
		return orders;
	}

	@Cacheable(key="'orders-uid-'+ #p0.id")
	public List<Order> listByUserAndNotDeleted(User user) {
		return this.list(new QueryWrapper<Order>()
				.eq("uid", user.getId())
				.ne("status", OrderService.delete));
	}

	@CacheEvict(allEntries=true)
	public void update(Order bean) {
		this.updateById(bean);
	}

	@Cacheable(key="'orders-page-'+#p0+ '-' + #p1")
	public Page4Navigator<Order> list(int start, int size, int navigatePages) {
		PageUtil<Order> p = new PageUtil<>(start, size);
		Page<Order> pageList = this.page(p, new QueryWrapper<Order>().orderByDesc("id"));
		pageList.getRecords().stream().forEach(order -> order.setUser(userMapper.selectById(order.getUid())));
		return new Page4Navigator<>(pageList, navigatePages);
	}

	@CacheEvict(allEntries=true)
	public void add(Order order) {
		this.save(order);
	}

	@CacheEvict(allEntries=true)
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> ois) {
        float total = 0;
        add(order);
        for (OrderItem oi: ois) {
            oi.setOrder(order);
			oi.setOid(order.getId());
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

	@Cacheable(key="'orders-one-'+ #p0")
	public Order get(int oid) {
		return this.getById(oid);
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


