package com.poly.repository;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi")
	BigDecimal getTotalRevenue();

	@Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE YEAR(oi.order.orderDate) = :year AND MONTH(oi.order.orderDate) = :month")
	BigDecimal getRevenueByMonth(int year, int month);

	@Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE YEAR(oi.order.orderDate) = :year")
	BigDecimal getRevenueByYear(int year);

	@Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate")
	BigDecimal getRevenueByDateRange(Date startDate, Date endDate);
}
