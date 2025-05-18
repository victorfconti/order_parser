package conti.victor.integrador.domain.mapper

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class ConverterTextToJsonTest {
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
    fun testaConverterSingleLine(received: String, expected: String) {
        val response = ConverterTextToJson(received).parser()

        val jsonCreated = objectMapper.writeValueAsString(response)

        assertThat(jsonCreated).isEqualTo(expected)
    }

//    @Test
//    @DisplayName("Teste de conversão de duas linhas simples")
//    fun testaConverterTwoLines() {
//
//
//
//        val jsonCreated = objectMapper.writeValueAsString(response)
//
//        assertThat(jsonCreated).isEqualTo("")
//
//    }

}