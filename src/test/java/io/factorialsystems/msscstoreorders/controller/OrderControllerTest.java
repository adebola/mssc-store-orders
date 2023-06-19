package io.factorialsystems.msscstoreorders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import io.factorialsystems.msscstoreorders.dto.OrderItemDTO;
import io.factorialsystems.msscstoreorders.dto.PagedDTO;
import io.factorialsystems.msscstoreorders.entity.OrderStatus;
import io.factorialsystems.msscstoreorders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    @WithMockUser(username="spring")
    void testGetOrders() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(UUID.randomUUID().toString());

        PagedDTO<OrderDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(order));

//        given(orderService.findAllOrders(any(Integer.class), any(Integer.class))).willReturn(pagedDTO);
        given(orderService.findAllOrders(anyInt(), anyInt())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 0;

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/order")
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andReturn();

        ArgumentCaptor<Integer> pageNumberArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(orderService).findAllOrders(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
    }

    @Test
    @WithMockUser(username="spring")
    void testUpdateUser() throws Exception {
        final UUID id = UUID.randomUUID();
        OrderDTO order = new OrderDTO();
//        order.setUserId("userId");

        given(orderService.updateOrderById(any(UUID.class), any(OrderDTO.class))).willReturn(Optional.of(order));

        mockMvc.perform(put("/api/v1/order/" + id.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<OrderDTO> orderArgumentCaptor = ArgumentCaptor.forClass(OrderDTO.class);

        verify(orderService).updateOrderById(uuidArgumentCaptor.capture(), orderArgumentCaptor.capture());
    }

    @Test
    @WithMockUser(username = "spring")
    void testFindById() throws Exception {
        final UUID id = UUID.randomUUID();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id.toString());
//        orderDTO.setUserId("adebola");

        given(orderService.findOrderById(id)).willReturn(Optional.of(orderDTO));

        mockMvc.perform(get("/api/v1/order/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)));

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(orderService).findOrderById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    @WithMockUser(username = "spring")
    void testCreateUser() throws Exception {
        final UUID id = UUID.randomUUID();
        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setUserId("userId");
        orderDTO.setOrderStatus(OrderStatus.ORDER_NEW);

        OrderItemDTO item1 = new OrderItemDTO(null, "product", "description", 1, new BigDecimal("1200"), new BigDecimal("1200"), BigDecimal.ZERO);

        Set<OrderItemDTO> items = new HashSet<>();
        items.add(item1);

        orderDTO.setItems(items);

        given(orderService.saveOrder(any(OrderDTO.class))).willReturn(Optional.of(orderDTO));

        mockMvc.perform(post("/api/v1/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ArgumentCaptor<OrderDTO> orderArgumentCaptor = ArgumentCaptor.forClass(OrderDTO.class);
        verify(orderService).saveOrder(orderArgumentCaptor.capture());
        assertThat(orderDTO.getId()).isEqualTo(orderArgumentCaptor.getValue().getId());
    }

    @Test
    @WithMockUser(username = "spring")
    void testDeleteById() throws Exception {
        UUID id = UUID.randomUUID();

        given(orderService.deleteOrderById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete("/api/v1/order/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(orderService).deleteOrderById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(id);
    }
}