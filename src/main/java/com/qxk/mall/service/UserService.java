package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.UserMapper;
import com.qxk.mall.model.User;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.PageUtil;
import com.qxk.mall.util.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames="users")
public class UserService extends ServiceImpl<UserMapper, User> {

	public boolean isExist(String name) {
		UserService userService = SpringContextUtil.getBean(UserService.class);
		User user = userService.getByName(name);
		return null!=user;
	}

	@Cacheable(key="'users-one-name-'+ #p0")
	public User getByName(String name) {
        return this.getOne(new QueryWrapper<User>().eq("name", name));
	}

	@Cacheable(key="'users-one-name-'+ #p0 +'-password-'+ #p1")
	public User get(String name, String password) {
        return this.getOne(new QueryWrapper<User>()
                .eq("name", name)
                .eq("password", password));
	}

	@Cacheable(key="'users-page-'+#p0+ '-' + #p1")
	public Page4Navigator<User> list(int start, int size, int navigatePages) {
        PageUtil<User> p = new PageUtil<>(start, size);
        Page<User> pageList = this.page(p, new QueryWrapper<User>().orderByDesc("id"));
        return new Page4Navigator<>(pageList, navigatePages);
	}

	@CacheEvict(allEntries=true)
	public void add(User user) {
        this.save(user);
	}

}


