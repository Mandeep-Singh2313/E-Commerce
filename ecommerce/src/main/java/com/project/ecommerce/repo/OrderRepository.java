package com.project.ecommerce.repo;

import com.project.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT COALESCE(sum(o.totalAmount),0) FROM Order o")
    Double getTotalRevenue();
}
