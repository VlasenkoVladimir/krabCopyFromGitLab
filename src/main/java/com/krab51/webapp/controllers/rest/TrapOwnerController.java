package com.krab51.webapp.controllers.rest;

import com.krab51.webapp.controllers.rest.base.BaseController;
import com.krab51.webapp.dto.TrapOwnerDto;
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

@Tag(name = "Владельцы ловушек")
public interface TrapOwnerController extends BaseController {

    @Operation(summary = "Сохранение владельца ловушек в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись сохранена")
    })
    @RequestMapping(value = "/trapowner", method = POST, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<HttpStatus> save(
            @RequestBody TrapOwnerDto trapOwnerDto
    );

    @Operation(summary = "Удаление владельца ловушек из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена")
    })
    @RequestMapping(value = "/trapowner/{id}", method = DELETE, consumes = ALL_VALUE)
    ResponseEntity<HttpStatus> deleteById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение владельца ловушек из БД по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrapOwnerDto.class)
                    )}),
            @ApiResponse(responseCode = "204", description = "Запись не найдена", content = {@Content()})
    })
    @RequestMapping(value = "/trapowner/{id}", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TrapOwnerDto> findById(
            @Parameter(name = "id", description = "Идентификатор")
            @PathVariable Long id
    );

    @Operation(summary = "Получение постраничного списка владельца ловушек из БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrapOwnerDtoPage.class))
                    )}),
            @ApiResponse(responseCode = "204", description = "Записи отсутствуют",
                    content = {@Content(schema = @Schema())})
    })
    @RequestMapping(value = "/trapowners", method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<TrapOwnerDtoPage> findAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество записей на страницу")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    );

    class TrapOwnerDtoPage extends PageImpl<TrapOwnerDto> {
        public TrapOwnerDtoPage(List<TrapOwnerDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}