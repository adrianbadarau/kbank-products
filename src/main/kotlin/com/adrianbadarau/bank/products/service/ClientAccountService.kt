package com.adrianbadarau.bank.products.service
import com.adrianbadarau.bank.products.domain.ClientAccount
import com.adrianbadarau.bank.products.repository.ClientAccountRepository
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [ClientAccount].
 */
@Service
@Transactional
class ClientAccountService(
    private val clientAccountRepository: ClientAccountRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a clientAccount.
     *
     * @param clientAccount the entity to save.
     * @return the persisted entity.
     */
    fun save(clientAccount: ClientAccount): ClientAccount {
        log.debug("Request to save ClientAccount : {}", clientAccount)
        return clientAccountRepository.save(clientAccount)
    }

    /**
     * Get all the clientAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<ClientAccount> {
        log.debug("Request to get all ClientAccounts")
        return clientAccountRepository.findAll(pageable)
    }

    /**
     * Get one clientAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<ClientAccount> {
        log.debug("Request to get ClientAccount : {}", id)
        return clientAccountRepository.findById(id)
    }

    /**
     * Delete the clientAccount by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete ClientAccount : {}", id)

        clientAccountRepository.deleteById(id)
    }
}
