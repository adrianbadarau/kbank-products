package com.adrianbadarau.bank.products.transaction_api

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Transaction. @todo maybe generate this from swagger
 */
data class Transaction(
    var id: String? = null,
    var accountId: String? = null,
    var value: BigDecimal? = null,
    var date: Instant? = null,
    var details: String? = null

)
