package io.factorialsystems.msscstoreorders.controller;

import io.factorialsystems.msscstoreorders.dto.MessageDTO;
import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import io.factorialsystems.msscstoreorders.dto.PagedDTO;
import io.factorialsystems.msscstoreorders.exception.NotFoundException;
import io.factorialsystems.msscstoreorders.service.OrderService;
import io.factorialsystems.msscstoreorders.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@SecurityRequirement(name = "bearerToken")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))})
    })
    @Operation(summary = "Get All Orders", description = "Get a Pageful of Orders, default values PageNumber = 1, PageSize = 20")
    public PagedDTO<OrderDTO> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        } else {
            pageNumber--;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return orderService.findAllOrders(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))})
    })
    @Operation(summary = "Get Single Orders", description = "Get a Single Order and its OrderItems by Id")
    public OrderDTO findById(@PathVariable("id") UUID id) {
        log.info("Find Order By Id {}", id);
        return orderService.findOrderById(id).orElseThrow(() -> new NotFoundException(String.format("Order %s Not Found", id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    @Operation(summary = "Save Order", description = "Save Orders and Orders Items")
    public OrderDTO saveOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("Saving New Order");
        Optional<OrderDTO> savedOrder = orderService.saveOrder(orderDTO);
        return savedOrder.orElseThrow(() -> new RuntimeException("Error saving Order"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))})
    })
    @Operation(summary = "Update Order", description = "Update Orders and Orders Items")
    public OrderDTO updateOrder(@PathVariable("id") UUID id, @Valid @RequestBody OrderDTO orderDTO) {
        log.info("Update Order {}", id);
        Optional<OrderDTO> updatedOrder = orderService.updateOrderById(id, orderDTO);
        return updatedOrder.orElseThrow(() -> new RuntimeException("Error Updating Order"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "OK", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))})
    })
    @Operation(summary = "Update Order", description = "Update Orders and Orders Items")

    public void deleteOrder(@PathVariable("id") UUID id) {
        log.info("Delete Order {}", id);
        orderService.deleteOrderById(id);
    }
}
