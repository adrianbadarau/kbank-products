package com.adrianbadarau.bank.products.web.rest

import com.adrianbadarau.bank.products.ProductsApp
import com.adrianbadarau.bank.products.domain.ClientAccount
import com.adrianbadarau.bank.products.repository.ClientAccountRepository
import com.adrianbadarau.bank.products.service.ClientAccountService
import com.adrianbadarau.bank.products.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.math.BigDecimal

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Integration tests for the [ClientAccountResource] REST controller.
 *
 * @see ClientAccountResource
 */
@SpringBootTest(classes = [ProductsApp::class])
class ClientAccountResourceIT {

    @Autowired
    private lateinit var clientAccountRepository: ClientAccountRepository

    @Autowired
    private lateinit var clientAccountService: ClientAccountService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restClientAccountMockMvc: MockMvc

    private lateinit var clientAccount: ClientAccount

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val clientAccountResource = ClientAccountResource(clientAccountService)
        this.restClientAccountMockMvc = MockMvcBuilders.standaloneSetup(clientAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        clientAccount = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createClientAccount() {
        val databaseSizeBeforeCreate = clientAccountRepository.findAll().size

        // Create the ClientAccount
        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isCreated)

        // Validate the ClientAccount in the database
        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeCreate + 1)
        val testClientAccount = clientAccountList[clientAccountList.size - 1]
        assertThat(testClientAccount.customerID).isEqualTo(DEFAULT_CUSTOMER_ID)
        assertThat(testClientAccount.iban).isEqualTo(DEFAULT_IBAN)
        assertThat(testClientAccount.name).isEqualTo(DEFAULT_NAME)
        assertThat(testClientAccount.ballance).isEqualTo(DEFAULT_BALLANCE)
        assertThat(testClientAccount.userId).isEqualTo(DEFAULT_USER_ID)
    }

    @Test
    @Transactional
    fun createClientAccountWithExistingId() {
        val databaseSizeBeforeCreate = clientAccountRepository.findAll().size

        // Create the ClientAccount with an existing ID
        clientAccount.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        // Validate the ClientAccount in the database
        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkCustomerIDIsRequired() {
        val databaseSizeBeforeTest = clientAccountRepository.findAll().size
        // set the field null
        clientAccount.customerID = null

        // Create the ClientAccount, which fails.

        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkIbanIsRequired() {
        val databaseSizeBeforeTest = clientAccountRepository.findAll().size
        // set the field null
        clientAccount.iban = null

        // Create the ClientAccount, which fails.

        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = clientAccountRepository.findAll().size
        // set the field null
        clientAccount.name = null

        // Create the ClientAccount, which fails.

        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkBallanceIsRequired() {
        val databaseSizeBeforeTest = clientAccountRepository.findAll().size
        // set the field null
        clientAccount.ballance = null

        // Create the ClientAccount, which fails.

        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkUserIdIsRequired() {
        val databaseSizeBeforeTest = clientAccountRepository.findAll().size
        // set the field null
        clientAccount.userId = null

        // Create the ClientAccount, which fails.

        restClientAccountMockMvc.perform(
            post("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllClientAccounts() {
        // Initialize the database
        clientAccountRepository.saveAndFlush(clientAccount)

        // Get all the clientAccountList
        restClientAccountMockMvc.perform(get("/api/client-accounts?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAccount.id?.toInt())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ballance").value(hasItem(DEFAULT_BALLANCE.toInt())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
    }
    
    @Test
    @Transactional
    fun getClientAccount() {
        // Initialize the database
        clientAccountRepository.saveAndFlush(clientAccount)

        val id = clientAccount.id
        assertNotNull(id)

        // Get the clientAccount
        restClientAccountMockMvc.perform(get("/api/client-accounts/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ballance").value(DEFAULT_BALLANCE.toInt()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
    }

    @Test
    @Transactional
    fun getNonExistingClientAccount() {
        // Get the clientAccount
        restClientAccountMockMvc.perform(get("/api/client-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateClientAccount() {
        // Initialize the database
        clientAccountService.save(clientAccount)

        val databaseSizeBeforeUpdate = clientAccountRepository.findAll().size

        // Update the clientAccount
        val id = clientAccount.id
        assertNotNull(id)
        val updatedClientAccount = clientAccountRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedClientAccount are not directly saved in db
        em.detach(updatedClientAccount)
        updatedClientAccount.customerID = UPDATED_CUSTOMER_ID
        updatedClientAccount.iban = UPDATED_IBAN
        updatedClientAccount.name = UPDATED_NAME
        updatedClientAccount.ballance = UPDATED_BALLANCE
        updatedClientAccount.userId = UPDATED_USER_ID

        restClientAccountMockMvc.perform(
            put("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedClientAccount))
        ).andExpect(status().isOk)

        // Validate the ClientAccount in the database
        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate)
        val testClientAccount = clientAccountList[clientAccountList.size - 1]
        assertThat(testClientAccount.customerID).isEqualTo(UPDATED_CUSTOMER_ID)
        assertThat(testClientAccount.iban).isEqualTo(UPDATED_IBAN)
        assertThat(testClientAccount.name).isEqualTo(UPDATED_NAME)
        assertThat(testClientAccount.ballance).isEqualTo(UPDATED_BALLANCE)
        assertThat(testClientAccount.userId).isEqualTo(UPDATED_USER_ID)
    }

    @Test
    @Transactional
    fun updateNonExistingClientAccount() {
        val databaseSizeBeforeUpdate = clientAccountRepository.findAll().size

        // Create the ClientAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientAccountMockMvc.perform(
            put("/api/client-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(clientAccount))
        ).andExpect(status().isBadRequest)

        // Validate the ClientAccount in the database
        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteClientAccount() {
        // Initialize the database
        clientAccountService.save(clientAccount)

        val databaseSizeBeforeDelete = clientAccountRepository.findAll().size

        val id = clientAccount.id
        assertNotNull(id)

        // Delete the clientAccount
        restClientAccountMockMvc.perform(
            delete("/api/client-accounts/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val clientAccountList = clientAccountRepository.findAll()
        assertThat(clientAccountList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_CUSTOMER_ID = "AAAAAAAAAA"
        private const val UPDATED_CUSTOMER_ID = "BBBBBBBBBB"

        private const val DEFAULT_IBAN = "AAAAAAAAAA"
        private const val UPDATED_IBAN = "BBBBBBBBBB"

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private val DEFAULT_BALLANCE: BigDecimal = BigDecimal(1)
        private val UPDATED_BALLANCE: BigDecimal = BigDecimal(2)

        private const val DEFAULT_USER_ID: Int = 1
        private const val UPDATED_USER_ID: Int = 2

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): ClientAccount {
            val clientAccount = ClientAccount(
                customerID = DEFAULT_CUSTOMER_ID,
                iban = DEFAULT_IBAN,
                name = DEFAULT_NAME,
                ballance = DEFAULT_BALLANCE,
                userId = DEFAULT_USER_ID
            )

            return clientAccount
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): ClientAccount {
            val clientAccount = ClientAccount(
                customerID = UPDATED_CUSTOMER_ID,
                iban = UPDATED_IBAN,
                name = UPDATED_NAME,
                ballance = UPDATED_BALLANCE,
                userId = UPDATED_USER_ID
            )

            return clientAccount
        }
    }
}
