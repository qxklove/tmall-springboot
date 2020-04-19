

package com.qxk.mall.comparator;



import java.util.Comparator;

import com.qxk.mall.model.Product;


public class ProductDateComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		return p1.getCreateDate().compareTo(p2.getCreateDate());
	}

}


