package conti.victor.order_parser.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ConverterControllerTest {
    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Test
    @DisplayName("Testa erro ao converter texto vazio")
    fun textRequestWithInvalidData() {

        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter", null, String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.body).isEqualTo("{\"message\":\"O texto deve ser informado\",\"code\":\"BAD_REQUEST\"}")
    }

    @Test
    @DisplayName("Testa erro ao converter texto com tamanho inválido")
    fun textRequestWithInvalidSize() {

        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter", "null", String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.body).isEqualTo("{\"message\":\"A linha deve ter 95 caracteres\",\"code\":\"BAD_REQUEST\"}")
    }

    @Test
    @DisplayName("Converte texto com sucesso")
    fun textRequestWithValidText() {

        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter", "0000000002                                     Medeiros00000123450000000111      256.2420201201", String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo("[{\"user_id\":2,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"256.24\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"}]}]}]")
    }

    @Test
    @DisplayName("Tenta enviar arquivo vazio")
    fun fileRequestWithEmptyFile() {
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", object : ByteArrayResource(ByteArray(0)) {
            override fun getFilename(): String {
                return "empty-file.txt"
            }
        })

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity(body, headers)


        // Envia a requisição usando TestRestTemplate
        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter/file", requestEntity, String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.body).isEqualTo("{\"message\":\"O texto deve ser informado\",\"code\":\"BAD_REQUEST\"}")

    }

    @Test
    @DisplayName("Tenta enviar arquivo nulo")
    fun fileRequestWithNullFile() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA


        // Envia a requisição usando TestRestTemplate
        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter/file", null, String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.body).isEqualTo("{\"message\":\"O arquivo deve ser informado\",\"code\":\"BAD_REQUEST\"}")

    }

    @Test
    @DisplayName("Tenta enviar dois arquivos")
    fun fileRequestWithTwoFiles() {
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", object : ByteArrayResource(ByteArray(0)) {
            override fun getFilename(): String {
                return "empty-file.txt"
            }
        })
       body.add("file", object : ByteArrayResource(ByteArray(0)) {
            override fun getFilename(): String {
                return "empty-file2.txt"
            }
        })

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity(body, headers)


        // Envia a requisição usando TestRestTemplate
        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter/file", requestEntity, String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(response.body).isEqualTo("{\"message\":\"Apenas um arquivo é permitido por vez\",\"code\":\"BAD_REQUEST\"}")

    }

    @Test
    @DisplayName("Envia arquivo vazio")
    fun fileRequestWithValidFile() {

        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", object : ByteArrayResource("0000000002                                     Medeiros00000123450000000111      256.2420201201".toByteArray()) {
            override fun getFilename(): String {
                return "empty-file.txt"
            }
        })

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val requestEntity = HttpEntity(body, headers)


        // Envia a requisição usando TestRestTemplate
        val response = restTemplate!!.postForEntity("/api/v1/purchase/converter/file", requestEntity, String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo("[{\"user_id\":2,\"name\":\"Medeiros\",\"orders\":[{\"order_id\":12345,\"total\":\"256.24\",\"date\":\"2020-12-01\",\"products\":[{\"product_id\":111,\"value\":\"256.24\"}]}]}]")

    }
}


