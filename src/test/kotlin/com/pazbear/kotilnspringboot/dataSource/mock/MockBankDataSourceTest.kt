package com.pazbear.kotilnspringboot.dataSource.mock

import com.pazbear.kotilnspringboot.model.Bank
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class MockBankDataSourceTest{
    private val mockDataSource: MockBankDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`(){
        //given


        //when
        val banks = mockDataSource.retrieveBanks()

        //then
        assertThat(banks).isNotEmpty
        assertThat(banks.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should provide some mock data`(){
        //given
        
        
        //when
        val banks = mockDataSource.retrieveBanks()
        
        //then
        assertThat(banks).allSatisfy(Consumer { it.accountNumber.isNotBlank() })
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch { it.trust !=0.0 }
        assertThat(banks).anyMatch { it.transactionFee !=0 }
    }
}