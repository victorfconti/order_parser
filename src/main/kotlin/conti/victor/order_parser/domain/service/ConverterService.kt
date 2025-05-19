package conti.victor.order_parser.domain.service

import conti.victor.order_parser.domain.mapper.ConverterOldOrderToJson
import conti.victor.order_parser.dto.PucharseResponseDTO
import conti.victor.order_parser.exception.BadRequestException
import conti.victor.order_parser.exception.InternalServerException
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ConverterService {
    private final val logger = LoggerFactory.getLogger(ConverterService::class.java)

    fun convertString(originalText: Optional<String>): List<PucharseResponseDTO> {
        try {
            MDC.put("traceId", UUID.randomUUID().toString())
            Validate.isTrue(originalText.isPresent && StringUtils.isNoneBlank(originalText.get()), "O texto deve ser informado")

            logger.info("Iniciando o processamento do texto")
            return ConverterOldOrderToJson(originalText.get()).parser()
        } catch (e: IllegalArgumentException){
            logger.error("Falha ao processar o texto: {}", e.message, e)
            throw BadRequestException(e.message)
        } catch (e: Exception) {
            throw InternalServerException(e.message)
        }finally {
            logger.info("Finalizando o processamento do texto")
            MDC.clear()
        }
    }

    fun convertFile(originalFileRequest: Optional<List<MultipartFile>>): List<PucharseResponseDTO> {
        try {
            MDC.put("traceId", UUID.randomUUID().toString())
            Validate.isTrue(originalFileRequest.isPresent && originalFileRequest.get().isNotEmpty(), "O arquivo deve ser informado")
            val originalFile = originalFileRequest.get()

            logger.info("Iniciando o processamento do arquivo")
            Validate.isTrue(originalFile.size == 1, "Apenas um arquivo é permitido por vez")

            logger.info("Lendo o arquivo {}", originalFile.first().name)
            MDC.put("fileName", originalFile.first().name)

            val body = String(originalFile.first().bytes)
            Validate.isTrue(StringUtils.isNoneBlank(body), "O texto deve ser informado")

            return ConverterOldOrderToJson(body).parser()
        } catch (e: IllegalArgumentException){
            logger.error("Falha ao processar o arquivo: {}", e.message, e)
            throw BadRequestException(e.message)
        } catch (e: Exception) {
            throw InternalServerException(e.message)
        }finally {
            logger.info("Finalizando o processamento do arquivo")
            MDC.clear()
        }
    }

}