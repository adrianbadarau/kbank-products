package com.adrianbadarau.bank.products.domain


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*
import javax.validation.constraints.*

import java.io.Serializable

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Lob
    @Column(name = "image")
    var image: ByteArray? = null,

    @Column(name = "image_content_type")
    var imageContentType: String? = null,

    @ManyToOne(optional = false)    @NotNull
    @JsonIgnoreProperties("products")
    var type: ClientAccount? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Product{" +
        "id=$id" +
        ", name='$name'" +
        ", description='$description'" +
        ", image='$image'" +
        ", imageContentType='$imageContentType'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
