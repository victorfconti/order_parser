package conti.victor.integrador.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.math.BigDecimal
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PucharseResponseDTO(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("orders")
    val orders: List<OrderDTO>
)

@JsonPropertyOrder(value = ["order_id", "total", "date", "products"])
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderDTO(
    @JsonProperty("order_id")
    val orderId: Long,
    @JsonProperty("date")
    val date: LocalDate,
    @JsonProperty("products")
    val products: List<ProductDTO>
){
    @get:JsonProperty("total")
    @Suppress("unused")
    @get:JsonSerialize(using = ToStringSerializer::class)
    val total: BigDecimal // Aqui eu mantenho o tipo como BigDecimal, para permitir uma melhor precisão de tipo e serializo para String
    get() = products.fold(BigDecimal.ZERO) { acc, p -> acc + p.value }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductDTO(
    @JsonProperty("product_id")
    val productId: Long,
    @JsonProperty("value")
    @field:JsonSerialize(using = ToStringSerializer::class)
    val value: BigDecimal   // Já aqui, também é para facilitar a soma
)