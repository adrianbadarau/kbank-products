package com.adrianbadarau.bank.products.client

import com.adrianbadarau.bank.products.transaction_api.Transaction
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(serviceId = "transactions")
interface TransactionsClient {

    @RequestMapping(value = ["api/transactions"], method = [RequestMethod.POST])
    fun createTransaction(@RequestBody transaction: Transaction): Transaction
}
