

package com.qxk.tmall.service;

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

import com.qxk.tmall.dao.CategoryDAO;
import com.qxk.tmall.pojo.Category;
import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.util.Page4Navigator;

@Service
@CacheConfig(cacheNames="categories")
public class CategoryService {
	@Autowired CategoryDAO categoryDAO;

	@CacheEvict(allEntries=true)
//	@CachePut(key="'category-one-'+ #p0")
	public void add(Category bean) {
		categoryDAO.save(bean);
	}

	@CacheEvict(allEntries=true)
//	@CacheEvict(key="'category-one-'+ #p0")
	public void delete(int id) {
		categoryDAO.delete(id);
	}


	@Cacheable(key="'categories-one-'+ #p0")
	public Category get(int id) {
		Category c= categoryDAO.findOne(id);
		return c;
	}

	@CacheEvict(allEntries=true)
//	@CachePut(key="'category-one-'+ #p0")
	public void update(Category bean) {
		categoryDAO.save(bean);
	}

	@Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
	public Page4Navigator<Category> list(int start, int size, int navigatePages) {
    	Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = new PageRequest(start, size,sort);
		Page pageFromJPA =categoryDAO.findAll(pageable);

		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}

	@Cacheable(key="'categories-all'")
	public List<Category> list() {
    	Sort sort = new Sort(Sort.Direction.DESC, "id");
		return categoryDAO.findAll(sort);
	}

	//这个方法的用处是删除Product对象上的 分类。 为什么要删除呢？ 因为在对分类做序列还转换为 json 的时候，会遍历里面的 products, 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了，就搞死个人了
	//而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系

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


