package com.adrianbadarau.bank.products.domain

import com.adrianbadarau.bank.products.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClientAccountTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(ClientAccount::class)
        val clientAccount1 = ClientAccount()
        clientAccount1.id = "feb28591-b10f-4506-b8d0-ed6cddf74c4f"
        val clientAccount2 = ClientAccount()
        clientAccount2.id = clientAccount1.id
        assertThat(clientAccount1).isEqualTo(clientAccount2)
        clientAccount2.id = "b204d55b-b3cb-408a-a6c4-0ddb0b685848"
        assertThat(clientAccount1).isNotEqualTo(clientAccount2)
        clientAccount1.id = null
        assertThat(clientAccount1).isNotEqualTo(clientAccount2)
    }
}
