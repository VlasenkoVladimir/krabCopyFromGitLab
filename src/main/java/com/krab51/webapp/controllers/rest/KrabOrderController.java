package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.rest.base.BaseController;
import com.krab51.webapp.dto.KrabOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "Путёвки")
@Validated
public interface KrabOrderController extends BaseController {
    @Operation(summary = "Сохранение путёвки в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись сохранена")
    })
    @RequestMapping(value = "/kraborder", method = POST, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<HttpStatus> save(
            @RequestBody KrabOrderDto krabOrderDto
    );

    @Operation(summary = "Удаление путёвки из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена")
    })
    @RequestMapping(value = "/kraborder/{id}", method = DELETE, consumes = ALL_VALUE)
    ResponseEntity<HttpStatus> deleteById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение путёвки из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = KrabOrderDto.class)
                    )
                    }),
            @ApiResponse(responseCode = "204", description = "Запись не найдена", content = {@Content()})
    })
    @RequestMapping(value = "/kraborder/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<KrabOrderDto> findById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение постраничного списка путёвок из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = KrabOrderController.KrabOrderDtoPage.class)
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Записи отсутствуют",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/kraborders", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Page<KrabOrderDto>> findAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество записей на страницу")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    );

    @Operation(summary = "Получение путёвки для печати в формате PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(mediaType = "application/pdf",
                            array = @ArraySchema(
                                    schema = @Schema()
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Запись отсутствует",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/orderForPrint/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> orderForPrint(
            @Parameter(name = "id", description = "Номер путёвки для печати")
            @PathVariable @Min(1) Long id);

    @Operation(summary = "Получение бирки для печати в формате PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(mediaType = "application/pdf",
                            array = @ArraySchema(
                                    schema = @Schema()
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Запись отсутствует",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/birkaForPrintById/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> birkaForPrintById(
            @Parameter(name = "id", description = "Номер бирки для печати")
            @PathVariable @Min(1) Long id);

    class KrabOrderDtoPage extends PageImpl<KrabOrderDto> {
        public KrabOrderDtoPage(List<KrabOrderDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}