package com.pazbear.kotilnspringboot.service

import com.pazbear.kotilnspringboot.dataSource.BankDataSource
import com.pazbear.kotilnspringboot.model.Bank
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource:BankDataSource) {
    fun getBanks():Collection<Bank>{
        return dataSource.retrieveBanks()
    }

    fun getBank(accountNumber:String):Bank = dataSource.retrieveBank(accountNumber)

    fun addBank(bank:Bank):Bank = dataSource.createBank(bank)

    fun updateBank(bank:Bank):Bank = dataSource.updateBank(bank)

    fun deleteBank(accountNumber : String):Unit = dataSource.deleteBank(accountNumber)
}