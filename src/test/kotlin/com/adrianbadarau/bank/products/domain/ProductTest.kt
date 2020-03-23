package com.adrianbadarau.bank.products.domain

import com.adrianbadarau.bank.products.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProductTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Product::class)
        val product1 = Product()
        product1.id = 1L
        val product2 = Product()
        product2.id = product1.id
        assertThat(product1).isEqualTo(product2)
        product2.id = 2L
        assertThat(product1).isNotEqualTo(product2)
        product1.id = null
        assertThat(product1).isNotEqualTo(product2)
    }
}
