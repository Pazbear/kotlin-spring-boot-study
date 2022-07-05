package com.pazbear.kotilnspringboot.dataSource.mock

import com.pazbear.kotilnspringboot.dataSource.BankDataSource
import com.pazbear.kotilnspringboot.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf(
        Bank("1234", 3.14, 17),
        Bank("bbb", 2.0, 2),
        Bank("ccc", 3.0, 3)
    )
    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber : String): Bank = banks.firstOrNull(){it.accountNumber == accountNumber}
        ?:throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    override fun createBank(bank: Bank): Bank {
        if (banks.any {it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Bank with accountNumber ${bank.accountNumber} already exists")
        }
        banks.add(bank)

        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull {it.accountNumber == bank.accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")

        banks.remove(currentBank)
        banks.add(bank)

        return bank
    }

    override fun deleteBank(accountNumber: String):Unit {
        val deletedBank = banks.firstOrNull {it.accountNumber == accountNumber}
            ?: throw NoSuchElementException("Could not find a bank with account number ${accountNumber}")
        banks.remove(deletedBank)
    }
}