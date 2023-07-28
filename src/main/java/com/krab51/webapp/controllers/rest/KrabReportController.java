package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.rest.base.BaseController;
import com.krab51.webapp.dto.KrabReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "Отчёты")
public interface KrabReportController extends BaseController {
    @Operation(summary = "Сохранение отчёта в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись сохранена")
    })
    @RequestMapping(value = "/report", method = POST, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<HttpStatus> save(
            @RequestBody KrabReportDto krabReportDto
    );

    @Operation(summary = "Удаление отчёта из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена")
    })
    @RequestMapping(value = "/report/{id}", method = DELETE, consumes = ALL_VALUE)
    ResponseEntity<HttpStatus> deleteById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение отчёта из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = KrabReportDto.class)
                    )}),
            @ApiResponse(responseCode = "204", description = "Запись не найдена", content = {@Content()})
    })
    @RequestMapping(value = "/report/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<KrabReportDto> findById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение постраничного списка отчётов из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = KrabReportController.ReportDtoPage.class)
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Записи отсутствуют",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/reports", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Page<KrabReportDto>> findAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество записей на страницу")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    );

    class ReportDtoPage extends PageImpl<KrabReportDto> {
        public ReportDtoPage(List<KrabReportDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}