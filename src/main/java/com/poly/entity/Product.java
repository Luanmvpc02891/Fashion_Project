package com.poly.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    
    @NotBlank(message = "NotBlank.product.productName")
    @Column(name = "product_name")
    private String productName;
    
    @DecimalMin(value = "0.00",inclusive = false, message = "DecimalMin.product.price")
    @Column(name = "price")
    private BigDecimal price;
    
   @NotNull(message = "NotNull.product.quantity")
    @Column(name = "quantity")
    private Integer  quantity;
    
   @DecimalMin(value = "0.00",inclusive = false, message = "DecimalMin.product.discout")
    @Column(name = "discount")
    private BigDecimal discount;
    
   @NotBlank(message= "NotBlank.product.image")
    @NotNull(message = "lỗi")
    @Column(name = "image")
    private String image;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;
    
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "product")
    private List<CartProduct> cartProducts;
    
    @OneToOne(mappedBy = "product")
    private Inventory inventory;
    
  

    // Constructors, getters, setters, and other properties
}