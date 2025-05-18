package conti.victor.integrador.domain.service

import conti.victor.integrador.domain.mapper.ConverterTextToJson
import conti.victor.integrador.dto.PucharseResponseDTO
import conti.victor.integrador.exception.BadRequestException
import conti.victor.integrador.exception.InternalServerException
import org.apache.commons.lang3.Validate
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ConverterService {
    private final val logger = LoggerFactory.getLogger(ConverterService::class.java)

    fun convertString(originalText: String): List<PucharseResponseDTO> {
        try {
            MDC.put("traceId", UUID.randomUUID().toString())

            logger.info("Iniciando o processamento do texto")
            return ConverterTextToJson(originalText).parser()
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

    fun convertFile(originalFile: List<MultipartFile>): List<PucharseResponseDTO> {
        try {
            MDC.put("traceId", UUID.randomUUID().toString())

            logger.info("Iniciando o processamento do arquivo")
            Validate.isTrue(originalFile.size == 1, "Apenas um arquivo é permitido por vez")
            logger.info("Lendo o arquivo {}", originalFile.first().name)
            MDC.put("fileName", originalFile.first().name)
            val body = String(originalFile.first().bytes)
            logger.info("Finalizando o processamento do arquivo")
            return ConverterTextToJson(body).parser()
        } catch (e: IllegalArgumentException){
            logger.error("Falha ao processar o arquivo: {}", e.message, e)
            throw BadRequestException(e.message)
        } catch (e: Exception) {
            throw InternalServerException(e.message)
        }finally {
            MDC.clear()
        }
    }

}