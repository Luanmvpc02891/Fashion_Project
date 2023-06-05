package com.poly.entity;

import java.math.BigDecimal;

public class cartDto {
	private int quantity;
	private BigDecimal totalPrice;
	private Product dProduct;

	public cartDto() {

	}

	public cartDto(int quantity, BigDecimal totalPrice, Product dProduct) {
		super();
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.dProduct = dProduct;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice2) {
		this.totalPrice = totalPrice2;
	}

	public Product getdProduct() {
		return dProduct;
	}

	public void setdProduct(Product dProduct) {
		this.dProduct = dProduct;
	}

}
