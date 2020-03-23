package com.adrianbadarau.bank.products.repository

import com.adrianbadarau.bank.products.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Product] entity.
 */
@Suppress("unused")
@Repository
interface ProductRepository : JpaRepository<Product, Long> {
}
