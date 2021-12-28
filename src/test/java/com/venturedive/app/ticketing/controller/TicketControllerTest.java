package com.venturedive.app.ticketing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.common.helper.TicketHelper;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService mockTicketService;

    @Test
    @WithMockUser(username = "ticketing.agent@ticketing.com", roles = {GlobalConstant.ROLE_TICKETING_AGENT})
    void shouldReturnAllTicketsSortedByPriority() throws Exception {

    List<Ticket> list = fetchSortedTicketList();
    List<TicketDto> sortedList = TicketHelper.getTransformedList(fetchSortedTicketList());

    when(mockTicketService.getAllSortedByPriority()).thenReturn(list);

    String accessToken = getAccessToken();

    final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_TICKET + GlobalConstant.API_TICKET_GET_ALL )
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        List<TicketDto> sortedListResponse = mapper.readValue(response.getContentAsString(), new TypeReference<List<TicketDto>>(){});

        assertThat(sortedListResponse).isEqualTo(sortedList);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(username = "ticketing.agent@ticketing.com", roles = {GlobalConstant.ROLE_TICKETING_AGENT})
    void shouldNotReturnAllTicketsUnsortedByPriority() throws Exception {

        List<Ticket> list = fetchSortedTicketList();
        List<TicketDto> unsortedList = TicketHelper.getTransformedList(fetchUnsortedTicketList());

        when(mockTicketService.getAllSortedByPriority()).thenReturn(list);

        String accessToken = getAccessToken();

        final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_TICKET + GlobalConstant.API_TICKET_GET_ALL )
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        List<TicketDto> sortedListResponse = mapper.readValue(response.getContentAsString(), new TypeReference<List<TicketDto>>(){});

        assertThat(sortedListResponse).isNotEqualTo(unsortedList);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(username = "ticketing.agent@ticketing.com", roles = {GlobalConstant.ROLE_TICKETING_AGENT})
    void shouldReturnEmptyList() throws Exception {

        when(mockTicketService.getAllSortedByPriority()).thenReturn(Collections.emptyList());

        String accessToken = getAccessToken();

        final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_TICKET + GlobalConstant.API_TICKET_GET_ALL)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    @WithMockUser(username = "order.manager@ticketing.com", roles = {GlobalConstant.ROLE_ORDER_MANAGER})
    void shouldNotAuthorizeUnauthorizedUser() throws Exception {

        List<Ticket> list = fetchSortedTicketList();

        when(mockTicketService.getAllSortedByPriority()).thenReturn(list);

        String accessToken = getAccessToken();

        final MockHttpServletResponse response = mockMvc.perform(get(GlobalConstant.API_PREFIX_TICKET + GlobalConstant.API_TICKET_GET_ALL )
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    private List<Ticket> fetchSortedTicketList(){
        Ticket ticket1 = Ticket.builder()
                .id(1)
                .priority(Priority.CRITICAL)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2)
                .priority(Priority.HIGHEST)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket3 = Ticket.builder()
                .id(3)
                .priority(Priority.HIGH)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        List<Ticket> list = new ArrayList<>();
        list.add(ticket1);
        list.add(ticket2);
        list.add(ticket3);
        return list;
    }

    private List<Ticket> fetchUnsortedTicketList(){
        Ticket ticket1 = Ticket.builder()
                .id(1)
                .priority(Priority.HIGH)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2)
                .priority(Priority.HIGHEST)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket3 = Ticket.builder()
                .id(3)
                .priority(Priority.CRITICAL)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        List<Ticket> list = new ArrayList<>();
        list.add(ticket1);
        list.add(ticket3);
        list.add(ticket2);
        return list;
    }

    private String getAccessToken() throws Exception {

        UserDto usetDto = UserDto.builder()
                .username("ticketing.agent@ticketing.com")
                .email("ticketing.agent@ticketing.com")
                .password("12345")
                .firstname("first name")
                .lastname("first name").build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(usetDto);

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
