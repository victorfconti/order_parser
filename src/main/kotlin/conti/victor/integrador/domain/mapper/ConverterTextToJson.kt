package conti.victor.integrador.domain.mapper

import conti.victor.integrador.dto.OrderDTO
import conti.victor.integrador.dto.ProductDTO
import conti.victor.integrador.dto.PucharseResponseDTO
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ConverterTextToJson(val originalText: String) {
    private val userIdRange = 0..9
    private val nameRange = 10 .. 54
    private val orderIdRange = 55..64
    private val productIdRange = 65..74
    private val productValueRange = 75..86
    private val orderDateRange = 87..94

    fun parser(): List<PucharseResponseDTO>{
        val lines = originalText.split("\n")
        var pucharses = mutableListOf<PucharseResponseDTO>()

        var line = 0

        lines.forEach {
            if(StringUtils.isBlank(it)) return@forEach
            println("Linha $line lida: $it")
            pucharses.add(PucharseResponseDTO(
                userId = obtainId(it),
                name = obtainName(it),
                orders = obtainOrders(it)
                )
            )
            line++
            println("Linha $line lida com sucesso!")
        }

        return pucharses.toList()
    }

    private fun obtainOrders(it: String): List<OrderDTO> {
        return listOf(
            OrderDTO(
            orderId = obtainOrderId(it),
            date = obtainOrderDate(it),
            products = obtainProducts(it)
        ))

    }

    private fun obtainProducts(it: String): List<ProductDTO> {
        return listOf(
            ProductDTO(
            productId = obtainProductId(it),
            value = obtainProductValue(it)
        )
        )
    }

    private fun obtainProductId(line: String): Long {
        return line.substring(productIdRange).trim().toLong()
    }

    private fun obtainProductValue(line: String): BigDecimal {
        return line.substring(productValueRange).trim().toBigDecimal()
    }

    private fun obtainOrderDate(line: String): LocalDate {
        val dateText = line.substring(orderDateRange)
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.parse(dateText, formatter)
    }

    private fun obtainOrderId(line: String): Long {
        return line.substring(orderIdRange).trim().toLong()
    }

    private fun obtainId(line: String): Long {
        return line.substring(userIdRange).trim().toLong()
    }

    private fun obtainName(line: String): String {
        return line.substring(nameRange).trim()
    }


}