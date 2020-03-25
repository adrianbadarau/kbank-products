package com.adrianbadarau.bank.products.web.rest

import com.adrianbadarau.bank.products.domain.ClientAccount
import com.adrianbadarau.bank.products.security.getCurrentUserLogin
import com.adrianbadarau.bank.products.service.ClientAccountService
import com.adrianbadarau.bank.products.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

private const val ENTITY_NAME = "productsClientAccount"
/**
 * REST controller for managing [com.adrianbadarau.bank.products.domain.ClientAccount].
 */
@RestController
@RequestMapping("/api")
class ClientAccountResource(
    private val clientAccountService: ClientAccountService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /client-accounts` : Create a new clientAccount.
     *
     * @param clientAccount the clientAccount to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new clientAccount, or with status `400 (Bad Request)` if the clientAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/client-accounts")
    fun createClientAccount(@Valid @RequestBody clientAccount: ClientAccount): ResponseEntity<ClientAccount> {
        log.debug("REST request to save ClientAccount : {}", clientAccount)
        if (clientAccount.id != null) {
            throw BadRequestAlertException(
                "A new clientAccount cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = clientAccountService.save(clientAccount)
        return ResponseEntity.created(URI("/api/client-accounts/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /client-accounts` : Updates an existing clientAccount.
     *
     * @param clientAccount the clientAccount to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated clientAccount,
     * or with status `400 (Bad Request)` if the clientAccount is not valid,
     * or with status `500 (Internal Server Error)` if the clientAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/client-accounts")
    fun updateClientAccount(@Valid @RequestBody clientAccount: ClientAccount): ResponseEntity<ClientAccount> {
        log.debug("REST request to update ClientAccount : {}", clientAccount)
        if (clientAccount.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = clientAccountService.save(clientAccount)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     clientAccount.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /client-accounts` : get all the clientAccounts.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of clientAccounts in body.
     */
    @GetMapping("/client-accounts")
    fun getAllClientAccounts(
        pageable: Pageable
    ): ResponseEntity<MutableList<ClientAccount>> {
        log.debug("REST request to get a page of ClientAccounts")
        getCurrentUserLogin().ifPresent {
            log.debug(it)
        }
        val page = clientAccountService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /client-accounts/:id` : get the "id" clientAccount.
     *
     * @param id the id of the clientAccount to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientAccount, or with status `404 (Not Found)`.
     */
    @GetMapping("/client-accounts/{id}")
    fun getClientAccount(@PathVariable id: Long): ResponseEntity<ClientAccount> {
        log.debug("REST request to get ClientAccount : {}", id)
        val clientAccount = clientAccountService.findOne(id)
        return ResponseUtil.wrapOrNotFound(clientAccount)
    }
    /**
     *  `DELETE  /client-accounts/:id` : delete the "id" clientAccount.
     *
     * @param id the id of the clientAccount to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/client-accounts/{id}")
    fun deleteClientAccount(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete ClientAccount : {}", id)
        clientAccountService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
