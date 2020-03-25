package com.adrianbadarau.bank.products.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.*
import kotlin.jvm.Transient
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.GenericGenerator

/**
 * A ClientAccount.
 */
@Entity
@Table(name = "client_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
data class ClientAccount(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    var id: String? = null,
    @get: NotNull
    @Column(name = "customer_id", nullable = false)
    var customerID: String? = null,

    @get: NotNull
    @Column(name = "iban", nullable = false)
    var iban: String? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @get: NotNull
    @Column(name = "ballance", precision = 21, scale = 2, nullable = false)
    var ballance: BigDecimal = BigDecimal.ZERO,

    @get: NotNull
    @Column(name = "user", nullable = false)
    var user: String? = null,

    @ManyToOne(optional = false) @NotNull
    @JsonIgnoreProperties("clientAccounts")
    var type: Product? = null,

    @Transient
    var initialCredit: BigDecimal? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClientAccount) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "ClientAccount{" +
        "id=$id" +
        ", customerID='$customerID'" +
        ", iban='$iban'" +
        ", name='$name'" +
        ", ballance=$ballance" +
        ", userId=$user" +
        ", initialCredit=$initialCredit" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
