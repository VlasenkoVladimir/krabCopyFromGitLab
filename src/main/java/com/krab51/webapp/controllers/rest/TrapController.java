package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.rest.base.BaseController;
import com.krab51.webapp.dto.TrapDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Ловушки")
public interface TrapController extends BaseController {

    @Operation(summary = "Сохранение ловушки в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись сохранена")
    })
    @RequestMapping(value = "/trap", method = POST, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<HttpStatus> save(
            @RequestBody TrapDto trapDto
    );

    @Operation(summary = "Удаление ловушки из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена")
    })
    @RequestMapping(value = "/trap/{id}", method = DELETE, consumes = ALL_VALUE)
    ResponseEntity<HttpStatus> deleteById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение ловушки из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrapDto.class)
                    )}),
            @ApiResponse(responseCode = "204", description = "Запись не найдена", content = {@Content()})
    })
    @RequestMapping(value = "/trap/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TrapDto> findById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение постраничного списка ловушек из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TrapController.TrapDtoPage.class)
                            )
                    )}),
            @ApiResponse(responseCode = "204", description = "Записи отсутствуют",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/traps", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TrapDtoPage> findAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество записей на страницу")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    );

    class TrapDtoPage extends PageImpl<TrapDto> {
        public TrapDtoPage(List<TrapDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}