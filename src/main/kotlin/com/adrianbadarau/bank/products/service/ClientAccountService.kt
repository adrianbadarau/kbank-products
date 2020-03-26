package com.adrianbadarau.bank.products.service

import com.adrianbadarau.bank.products.domain.ClientAccount
import com.adrianbadarau.bank.products.repository.ClientAccountRepository
import com.adrianbadarau.bank.products.client.TransactionsClient
import com.adrianbadarau.bank.products.security.getCurrentUserLogin
import com.adrianbadarau.bank.transactions.domain.Transaction
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import javax.servlet.http.HttpServletRequest

/**
 * Service Implementation for managing [ClientAccount].
 */
@Service
@Transactional
class ClientAccountService(
    private val clientAccountRepository: ClientAccountRepository,
    private val transactionsClient: TransactionsClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a clientAccount.
     * Make sure the balance equals to the initial credit property if it's a new account, and the property exists.
     * @param clientAccount the entity to save.
     * @return the persisted entity.
     */
    fun save(clientAccount: ClientAccount): ClientAccount {
        log.debug("Request to save ClientAccount : {}", clientAccount)
        if (clientAccount.id == null) {
            if (clientAccount.initialCredit != null) {
                clientAccount.ballance = clientAccount.initialCredit!!
            }
            // Here we can ignore the optional and go directly for the value since there is no way a user that is not
            // logged in can get to this point
            clientAccount.user = getCurrentUserLogin().get()
        }
        // we first create the account then we have to add the transaction for it
        val savedAccount = clientAccountRepository.save(clientAccount)
        if (clientAccount.initialCredit != null && clientAccount.initialCredit!! > BigDecimal.ZERO) {
            makeCreateTransactionCall(clientAccount)
        }
        return savedAccount
    }

    /**
     * Get all the clientAccounts for the current logged in user.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<ClientAccount> {
        log.debug("Request to get all ClientAccounts")
        val user = getCurrentUserLogin().get()
        return clientAccountRepository.findAllByUserEquals(user, pageable)
    }

    /**
     * Get one clientAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: String): Optional<ClientAccount> {
        log.debug("Request to get ClientAccount : {}", id)
        return clientAccountRepository.findById(id)
    }

    /**
     * Delete the clientAccount by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete ClientAccount : {}", id)

        clientAccountRepository.deleteById(id)
    }

    private fun makeCreateTransactionCall(account: ClientAccount): Transaction {
        return transactionsClient.createTransaction(
            Transaction(
                accountId = account.customerID,
                value = account.ballance,
                date = Instant.now(),
                details = "Initial credit"
            )
        )
    }
}
