package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.es.ProductESDAO;
import com.qxk.mall.mapper.ProductMapper;
import com.qxk.mall.model.Category;
import com.qxk.mall.model.Product;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.PageUtil;
import com.qxk.mall.util.SpringContextUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames="products")
public class ProductService extends ServiceImpl<ProductMapper, Product> {

	@Autowired
	ProductESDAO productESDAO;
	@Autowired ProductImageService productImageService;
	@Autowired CategoryService categoryService;
	@Autowired OrderItemService orderItemService;
	@Autowired ReviewService reviewService;

	@CacheEvict(allEntries=true)
	public void add(Product bean) {
		this.save(bean);
		productESDAO.save(bean);
	}

	@CacheEvict(allEntries=true)
	public void delete(int id) {
		this.removeById(id);
		productESDAO.delete(id);
	}

	@Cacheable(key="'products-one-'+ #p0")
	public Product get(int id) {
		Product product = this.getById(id);
		product.setCategory(categoryService.getById(product.getCid()));
		return product;
	}

	@CacheEvict(allEntries=true)
	public void update(Product bean) {
		this.updateById(bean);
		productESDAO.save(bean);
	}

	@Cacheable(key="'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
	public Page4Navigator<Product> list(int cid, int start, int size,int navigatePages) {
		PageUtil<Product> p = new PageUtil<>(start, size);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<Product> pageList = this.page(p, new QueryWrapper<Product>()
				.eq("cid", cid)
				.orderByDesc("id"));
		return new Page4Navigator<>(pageList, navigatePages);
	}

	public void fill(List<Category> categorys) {
		for (Category category : categorys) {
			fill(category);
		}
	}

	@Cacheable(key="'products-cid-'+ #p0.id")
	public List<Product> listByCategory(Category category){
		return this.list(new QueryWrapper<Product>()
				.eq("cid", category.getId())
				.orderByDesc("id"));
	}

	public void fill(Category category) {
		ProductService productService = SpringContextUtil.getBean(ProductService.class);
		List<Product> products = productService.listByCategory(category);
		productImageService.setFirstProdutImages(products);
		category.setProducts(products);
	}


	public void fillByRow(List<Category> categorys) {
        int productNumberEachRow = 8;
        for (Category category : categorys) {
            List<Product> products =  category.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
	}

	public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);


        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);

	}

	public void setSaleAndReviewNumber(List<Product> products) {
		for (Product product : products) {
			setSaleAndReviewNumber(product);
		}
	}

	public List<Product> search(String keyword, int start, int size) {
		initDatabase2ES();
		FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
				.add(QueryBuilders.matchPhraseQuery("name", keyword),
						ScoreFunctionBuilders.weightFactorFunction(100))
				.scoreMode("sum")
				.setMinScore(10);
		Sort sort  = new Sort(Sort.Direction.DESC,"id");
		Pageable pageable = new PageRequest(start, size,sort);
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(pageable)
				.withQuery(functionScoreQueryBuilder).build();

		Page<Product> page = productESDAO.search(searchQuery);
		return page.getContent();
	}

	private void initDatabase2ES() {
		Pageable pageable = new PageRequest(0, 5);
		Page<Product> page =productESDAO.findAll(pageable);
		if(page.getContent().isEmpty()) {
			List<Product> products = this.list();
			for (Product product : products) {
				productESDAO.save(product);
			}
		}
	}

}


