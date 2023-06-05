//package com.poly.dao;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.poly.entity.Product;
//import com.poly.entity.cartDto;
//import com.poly.repository.ProductRepository;
//
//import jakarta.servlet.http.HttpSession;
//
//public class cartDao {
//	@Autowired
//	ProductRepository dao;
//
//	
//	public Product findProductID(long id){
//		StringBuffer sqp=qlString();
//		return null;
//		
//	}
//	
//
//	public HashMap<Long, cartDto> addCart(long id, HashMap<Long, cartDto> cart, Model model) {
//		cartDto itemCart = new cartDto();
//		Product item = dao.findById(productId).get();
//		if (item != null) {
//			itemCart.setdProduct(item);
//			itemCart.setQuantity(1);
//			BigDecimal totalPrice = item.getPrice();
//			itemCart.setTotalPrice(totalPrice);
//		}
//		return cart;
//	}
//}
