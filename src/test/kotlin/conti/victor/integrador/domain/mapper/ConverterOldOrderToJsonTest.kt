package conti.victor.integrador.domain.mapper

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class ConverterOldOrderToJsonTest {
    private val objectMapper: JsonMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()


    @ParameterizedTest()
    @DisplayName("Teste de conversão de uma linha simples")
    @CsvSource(
        value = [
            "0000000002                                     Medeiros00000123450000000111      256.2420201201;[{\"user_id\":2,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"256.24\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"}]}]}]",
            "0000000001                                      Zarelli00000001230000000122      512.2420211201;[{\"user_id\":1,\"name\":\"Zarelli\",\"orders\":[{\"order_id\":123,\"total\":\"512.24\",\"date\":\"2021-12-01\",\"products\":[{\"product_id\":122,\"value\":\"512.24\"}]}]}]"
            ],
        delimiter = ';'
    )
    fun testConverterSingleLine(received: String, expected: String) {
        val response = ConverterOldOrderToJson(received).parser()

        val jsonCreated = objectMapper.writeValueAsString(response)

        assertThat(jsonCreated).isEqualTo(expected)
    }

    @Test
    @DisplayName("Teste com data inválida")
    fun testConverterSingleLineInvalidData() {
        val invalidDateLine = "0000000002                                     Medeiros00000123450000000111      256.2420201a01"

        assertThatThrownBy { ConverterOldOrderToJson(invalidDateLine).parser() }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Teste com linha de tamanho inválida")
    fun testConverterSingleLineInvalidSize() {
        val invalidDateLine = "0000000002                                     Medeiros00000123450000000111      256.242020101"

        assertThatThrownBy { ConverterOldOrderToJson(invalidDateLine).parser() }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Teste de conversão de duas linhas simples")
    fun testConverterTwoLines() {
        val jsonEsperado = "[{\"user_id\":1,\"name\":\"Zarelli\",\"orders\":[{\"order_id\":123,\"total\":\"512.24\",\"date\":\"2021-12-01\",\"products\":[{\"product_id\":122,\"value\":\"512.24\"}]}]},{\"user_id\":2,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"256.24\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"}]}]}]"

        val fileContent = object {}.javaClass.getResourceAsStream("/data_two_lines.txt")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found")

        val response = ConverterOldOrderToJson(fileContent).parser()
        val jsonCreated = objectMapper.writeValueAsString(response)

        assertThat(jsonCreated).isEqualTo(jsonEsperado)

    }

    @Test
    @DisplayName("Teste de conversão de 4 linhas, cada uma com mais de um produto")
    fun testConverterFourLinesMultiplesProducts() {
        val jsonEsperado = "[{\"user_id\":2,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"512.48\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"},{\"product_id\":122,\"value\":\"256.24\"}]}]},{\"user_id\":1,\"name\":\"Zarelli\",\"orders\":[{\"order_id\":123,\"total\":\"1024.48\",\"date\":\"2021-12-01\",\"products\":[{\"product_id\":111,\"value\":\"512.24\"},{\"product_id\":122,\"value\":\"512.24\"}]}]}]"

        val fileContent = object {}.javaClass.getResourceAsStream("/data_four_lines_multiples_products.txt")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found")

        val response = ConverterOldOrderToJson(fileContent).parser()
        val jsonCreated = objectMapper.writeValueAsString(response)

        assertThat(jsonCreated).isEqualTo(jsonEsperado)

    }

    @Test
    @DisplayName("Teste de conversão de 4 linhas, cada uma com mais de uma ordem")
    fun testConverterFourLinesMultiplesOrdens() {
        val jsonEsperado = "[{\"user_id\":1,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"512.48\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"},{\"product_id\":122,\"value\":\"256.24\"}]},{\"order_id\":123,\"total\":\"1024.48\",\"date\":\"2021-12-01\",\"products\":[{\"product_id\":111,\"value\":\"512.24\"},{\"product_id\":122,\"value\":\"512.24\"}]}]}]"

        val fileContent = object {}.javaClass.getResourceAsStream("/data_four_lines_multiples_orders_and_products.txt")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found")

        val response = ConverterOldOrderToJson(fileContent).parser()
        val jsonCreated = objectMapper.writeValueAsString(response)

        assertThat(jsonCreated).isEqualTo(jsonEsperado)

    }


}