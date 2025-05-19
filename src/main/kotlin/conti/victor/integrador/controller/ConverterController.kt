package conti.victor.integrador.controller

import conti.victor.integrador.domain.service.ConverterService
import conti.victor.integrador.dto.PucharseResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

@RestController
@RequestMapping("/api/v1/purchase/converter")
class ConverterController(private val converterService: ConverterService) {

    @PostMapping
    fun convert(@RequestBody(required = false) originalText: Optional<String>): List<PucharseResponseDTO> {
        return converterService.convertString(originalText)
    }

    @PostMapping("/file")
    fun convertFile(@RequestParam("file", required = false) originalFile: Optional<List<MultipartFile>>): List<PucharseResponseDTO> {
        return converterService.convertFile(originalFile)
    }

}