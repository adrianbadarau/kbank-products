package com.adrianbadarau.bank.products.transaction_api

import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A Transaction. @todo maybe generate this from swagger
 */
data class Transaction(
    var id: Long? = null,
    var accountId: String? = null,
    var value: BigDecimal? = null,
    var date: Instant? = null,
    var details: String? = null

)
