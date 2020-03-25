package com.adrianbadarau.bank.products.repository

import com.adrianbadarau.bank.products.domain.ClientAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [ClientAccount] entity.
 */
@Suppress("unused")
@Repository
interface ClientAccountRepository : JpaRepository<ClientAccount, String>
