package com.venturedive.app.ticketing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService mockDeliveryService;

    @Test
    @WithMockUser(username = "order.manager@ticketing.com", roles = {GlobalConstant.ROLE_ORDER_MANAGER})
    void shouldReturnAllDeliveries() throws Exception {

    List<Delivery> list = fetchAllDeliveries();

    when(mockDeliveryService.getAll()).thenReturn(list);

    String accessToken = getAccessToken();

    final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_DELIVERY + GlobalConstant.API_DELIVERY_GET_ALL )
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEqualTo("[]");
    }

    @Test
    @WithMockUser(username = "order.manager@ticketing.com", roles = {GlobalConstant.ROLE_ORDER_MANAGER})
    void shouldReturnEmptyList() throws Exception {

        when(mockDeliveryService.getAll()).thenReturn(Collections.emptyList());

        String accessToken = getAccessToken();

        final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_DELIVERY + GlobalConstant.API_DELIVERY_GET_ALL)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    @WithMockUser(username = "ticketing.agent@ticketing.com", roles = {GlobalConstant.ROLE_TICKETING_AGENT})
    void shouldNotAuthorizeUnauthorizedUser() throws Exception {

        List<Delivery> list = fetchAllDeliveries();

        when(mockDeliveryService.getAll()).thenReturn(list);

        String accessToken = getAccessToken();

        final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_DELIVERY + GlobalConstant.API_DELIVERY_GET_ALL )
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private List<Delivery> fetchAllDeliveries(){
        Delivery delivery1 = Delivery.builder()
                .id(1)
                .customerType(CustomerType.VIP)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(500)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery2 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.LOYAL)
                .deliveryStatus(DeliveryStatus.ORDER_PREPARING)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(300)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery3 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.NEW)
                .deliveryStatus(DeliveryStatus.ORDER_PICKED_UP)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(400)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        List<Delivery> list = new ArrayList<>();
        list.add(delivery1);
        list.add(delivery2);
        list.add(delivery3);
        return list;
    }

    private String getAccessToken() throws Exception {

        UserDto userDto = UserDto.builder()
                .username("order.manager@ticketing.com")
                .email("order.manager@ticketing.com")
                .password("12345")
                .firstname("first name")
                .lastname("first name").build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userDto);

        ResultActions result
                = mockMvc.perform(post(GlobalConstant.API_PREFIX_AUTH + GlobalConstant.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        AuthDto token = mapper.readValue(resultString, AuthDto.class);
        return token.getAccessTokenResponse().getToken();
    }
}
