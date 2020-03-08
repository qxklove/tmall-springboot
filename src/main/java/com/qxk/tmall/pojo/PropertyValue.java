

package com.qxk.tmall.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "property_value")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class PropertyValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;


    @ManyToOne
    @JoinColumn(name="pid")

	private Product product;
    @ManyToOne

    @JoinColumn(name="ptid")
	private Property property;

    private String value;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "PropertyValue [id=" + id + ", product=" + product + ", property=" + property + ", value=" + value + "]";
	}


}


