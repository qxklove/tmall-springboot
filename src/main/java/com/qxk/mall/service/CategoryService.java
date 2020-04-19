package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.CategoryMapper;
import com.qxk.mall.model.Category;
import com.qxk.mall.model.Product;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="categories")
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

	@CacheEvict(allEntries=true)
//	@CachePut(key="'category-one-'+ #p0")
	public void add(Category bean) {
        this.save(bean);
	}

	@CacheEvict(allEntries=true)
//	@CacheEvict(key="'category-one-'+ #p0")
	public void delete(int id) {
        this.removeById(id);
	}

	@Cacheable(key="'categories-one-'+ #p0")
	public Category get(int id) {
        Category c = this.getById(id);
		return c;
	}

	@CacheEvict(allEntries=true)
//	@CachePut(key="'category-one-'+ #p0")
	public void update(Category bean) {
        this.updateById(bean);
	}

	@Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
	public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        PageUtil<Category> p = new PageUtil<>(start, size);
        Page<Category> pageList = this.page(p, new QueryWrapper<Category>().orderByDesc("id"));
        return new Page4Navigator<>(pageList, navigatePages);
    }

    @Override
    @Cacheable(key = "'categories-all'")
    public List<Category> list() {
        return this.list(new QueryWrapper<Category>().orderByDesc("id"));
    }

    //这个方法的用处是删除Product对象上的分类。 为什么要删除呢？ 因为在对分类做序列化转换为 json 的时候，会遍历里面的 products, 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了，而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系
	public void removeCategoryFromProduct(List<Category> cs) {
		for (Category category : cs) {
			removeCategoryFromProduct(category);
		}
	}

	public void removeCategoryFromProduct(Category category) {
		List<Product> products =category.getProducts();
		if(null!=products) {
			for (Product product : products) {
				product.setCategory(null);
			}
		}

		List<List<Product>> productsByRow =category.getProductsByRow();
		if(null!=productsByRow) {
			for (List<Product> ps : productsByRow) {
				for (Product p: ps) {
					p.setCategory(null);
				}
			}
		}
	}
}


