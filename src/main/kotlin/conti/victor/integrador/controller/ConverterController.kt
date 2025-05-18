package conti.victor.integrador.controller

import conti.victor.integrador.domain.service.ConverterService
import conti.victor.integrador.dto.PucharseResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/converter")
class ConverterController(private final val converterService: ConverterService) {

    @PostMapping
    fun convert(@RequestBody originalText: String): List<PucharseResponseDTO> {
        return converterService.convertString(originalText)
    }

    @PostMapping("file")
    fun convert(@RequestParam("file") originalFile: List<MultipartFile>): List<PucharseResponseDTO> {
        return converterService.convertFile(originalFile)
    }

}