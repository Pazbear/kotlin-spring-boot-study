package com.pazbear.kotilnspringboot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pazbear.kotilnspringboot.model.Bank
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc:MockMvc,
    var objectMapper:ObjectMapper
) {

    val baseUrl = "/api/banks"
    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`(){

            //when/then
            mockMvc.get(baseUrl)
                .andDo{print()}
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber"){value("1234")}
                }
        }
    }

    @Nested
    @DisplayName("GET /api/bank/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`(){
            //given
            val accountNumber = 1234

            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust"){value(3.14)}
                    jsonPath("$.transactionFee"){value("17")}
                }

        }

        @Test
        fun `should return Not Found if the account number does not exist`(){
            //given
            val accountNumber = "does_not_exist"

            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
    
    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {

        @Test
        fun `should add the new bank`(){
            //given
            val newBank = Bank("newBank", 1.111, 4)

            //when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            //then
            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect {
                    status { isOk() }
                    content { content { json(objectMapper.writeValueAsString(newBank)) }}
                }
        }
        
        @Test
        fun `should return Not Found if the account number does not exist`(){
            //given
            val invalidBank = Bank("1234", 3.14, 17)
            
            //when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            
            //then
            performPost
                .andDo{ print() }
                .andExpect{
                    status { isBadRequest() }
                }
        }
    }
    
    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an Existing bank`(){
            //given
            val updatedBank = Bank("1234", 1.0, 1)
            
            //when
            val performPatchRequest = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
            
            //then
            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect {
                    status { isOk() }
                    content { content { json(objectMapper.writeValueAsString(updatedBank)) }}
                }
        }


        @Test
        fun `should return Not Found if the account number does not exist`(){
            //given
            val invalidBank = Bank("does_not_exist", 3.14, 17)

            //when
            val performPatchRequest = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            //then
            performPatchRequest
                .andDo{ print() }
                .andExpect{
                    status { isNotFound() }
                }
        }
    }
    
    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {
        @Test
        fun `should delete the bank with the given account number`(){
            //given
            val accountNumber = 1234
            
            //when/then
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo{ print() }
                .andExpect {
                    status { isNoContent() }
                }

            
        }

        @Test
        fun `should return Not Found if the account number does not exist`(){
            //given
            val invalidAccountNumber = "does_not_exist"

            //when
            mockMvc.delete("$baseUrl/$invalidAccountNumber")
            //then
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}