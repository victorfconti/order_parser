package conti.victor.integrador.domain.mapper

import conti.victor.integrador.dto.OrderDTO
import conti.victor.integrador.dto.ProductDTO
import conti.victor.integrador.dto.PucharseResponseDTO
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
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
        val pucharses = mutableListOf<PucharseResponseDTO>()



        lines.forEach { line ->
            if(StringUtils.isBlank(line)) return@forEach
            Validate.isTrue(line.length == 95, "A linha deve ter 95 caracteres")

            pucharses.add(PucharseResponseDTO(
                userId = obtainId(line),
                name = obtainName(line),
                orders = obtainOrders(line)
                )
            )
        }

        return pucharses.toList()
    }

    private fun obtainOrders(line: String): List<OrderDTO> {
        return listOf(
            OrderDTO(
            orderId = obtainOrderId(line),
            date = obtainOrderDate(line),
            products = obtainProducts(line)
        ))

    }

    private fun obtainProducts(line: String): List<ProductDTO> {
        return listOf(
            ProductDTO(
            productId = obtainProductId(line),
            value = obtainProductValue(line)
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
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            return LocalDate.parse(dateText, formatter)

        } catch (exception: RuntimeException) {
            throw IllegalArgumentException("A data $dateText é inválida para a máscara yyyyMMdd")
        }
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