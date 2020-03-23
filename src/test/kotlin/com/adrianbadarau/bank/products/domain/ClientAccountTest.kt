package com.adrianbadarau.bank.products.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.adrianbadarau.bank.products.web.rest.equalsVerifier

class ClientAccountTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(ClientAccount::class)
        val clientAccount1 = ClientAccount()
        clientAccount1.id = 1L
        val clientAccount2 = ClientAccount()
        clientAccount2.id = clientAccount1.id
        assertThat(clientAccount1).isEqualTo(clientAccount2)
        clientAccount2.id = 2L
        assertThat(clientAccount1).isNotEqualTo(clientAccount2)
        clientAccount1.id = null
        assertThat(clientAccount1).isNotEqualTo(clientAccount2)
    }
}
