package com.mkosc.jobs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.dto.JobOfferDTO;
import com.mkosc.jobs.dto.JobOfferSearchDTO;
import com.mkosc.jobs.exception.JobOfferNotFoundException;
import com.mkosc.jobs.service.OffersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OffersController.class)
public class OffersControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OffersService offersService;

    @Test
    public void shouldReturnOfferById() throws Exception {
        long id = 1L;
        JobOffer jobOffer = new JobOffer()
                .withOfferId(id)
                .withTitle("title")
                .withDescription("description")
                .withCompanyName("companyName");

        when(offersService.getOfferById(id)).thenReturn(jobOffer);

        mockMvc.perform(get("/offers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jobOffer)));

        verify(offersService).getOfferById(id);
    }

    @Test
    public void shouldReturnJobOfferNotFoundExceptionWhenOfferDoesNotExist() throws Exception {
        long id = 1L;
        doThrow(new JobOfferNotFoundException()).when(offersService).getOfferById(id);

        mockMvc.perform(get("/offers/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof JobOfferNotFoundException));
    }

    @Test
    public void shouldReturnAllOffers() throws Exception {
        long id1 = 1L;
        long id2 = 2L;
        JobOffer jobOffer1 = new JobOffer().withOfferId(id1).withTitle("title1").withDescription("description1").withCompanyName("companyName1");
        JobOffer jobOffer2 = new JobOffer().withOfferId(id2).withTitle("title2").withDescription("description2").withCompanyName("companyName2");
        List<JobOffer> expectedOffers = Arrays.asList(jobOffer1, jobOffer2);

        when(offersService.getAllOffers(any())).thenReturn(new PageImpl<>(expectedOffers));

        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(expectedOffers))));

        verify(offersService).getAllOffers(any());
    }

    @Test
    public void shouldReturnOffersByTitle() throws Exception {
        String title = "title1";
        JobOfferSearchDTO jobOfferSearchDTO = new JobOfferSearchDTO().withOfferId(1L).withTitle(title);
        List<JobOfferSearchDTO> expectedOffers = Collections.singletonList(jobOfferSearchDTO);

        when(offersService.getOffersByTitle(any(), eq(title))).thenReturn(new PageImpl<>(expectedOffers));

        mockMvc.perform(get("/offers/search").param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(expectedOffers))));

        verify(offersService).getOffersByTitle(any(), eq(title));
    }

    @Test
    public void shouldReturnOffersByCompanyName() throws Exception {
        String companyName = "company1";
        JobOfferSearchDTO jobOfferSearchDTO = new JobOfferSearchDTO().withOfferId(1L).withCompanyName(companyName);
        List<JobOfferSearchDTO> expectedOffers = Collections.singletonList(jobOfferSearchDTO);

        when(offersService.getOffersByCompanyName(any(), eq(companyName))).thenReturn(new PageImpl<>(expectedOffers));

        mockMvc.perform(get("/offers/search").param("companyName", companyName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(expectedOffers))));

        verify(offersService).getOffersByCompanyName(any(), eq(companyName));
    }

    @Test
    public void shouldCreateOffer() throws Exception {
        long id = 1L;
        JobOffer jobOffer = new JobOffer()
                .withOfferId(id)
                .withTitle("title")
                .withDescription("description")
                .withCompanyName("companyName");

        when(offersService.createOffer(jobOffer)).thenReturn(jobOffer);

        mockMvc.perform(post("/offers")
                .content(objectMapper.writeValueAsString(jobOffer)).header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(jobOffer)));

        verify(offersService).createOffer(jobOffer);
    }

    @Test
    public void shouldUpdateOfferIfExist() throws Exception {
        long id = 1L;
        JobOffer jobOffer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("companyName");

        when(offersService.updateOffer(id, jobOffer)).thenReturn(jobOffer);

        mockMvc.perform(put("/offers/{id}", id)
                .content(objectMapper.writeValueAsString(jobOffer)).header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jobOffer)));

        verify(offersService).updateOffer(id, jobOffer);
    }

    @Test
    public void shouldReturnJobOfferNotFoundExceptionOnUpdateWhenOfferDoesNotExist() throws Exception {
        long id = 1L;
        JobOffer jobOffer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("companyName");

        doThrow(new JobOfferNotFoundException()).when(offersService).updateOffer(id, jobOffer);

        mockMvc.perform(put("/offers/{id}", id)
                .content(objectMapper.writeValueAsString(jobOffer)).header("Content-Type", "application/json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof JobOfferNotFoundException));
    }

    @Test
    public void shouldUpdateOfferElementIfExist() throws Exception {
        long id = 1L;
        String element = "title";
        Map<String, Object> elementUpdate = new HashMap<>();
        elementUpdate.put(element, "new title");
        JobOfferDTO jobOfferDTO = new JobOfferDTO().withTitle("title").withDescription("description").withCompanyName("companyName");

        when(offersService.updateOfferElement(id, element, elementUpdate)).thenReturn(jobOfferDTO);

        mockMvc.perform(patch("/offers/{id}", id)
                .content(objectMapper.writeValueAsString(elementUpdate))
                .header("Content-Type", "application/json")
                .param("element", element))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jobOfferDTO)));

        verify(offersService).updateOfferElement(id, element, elementUpdate);
    }

    @Test
    public void shouldReturnJobOfferNotFoundOnUpdateOfferElementWhenOfferDoesNotExist() throws Exception {
        long id = 1L;
        String element = "title";
        Map<String, Object> elementUpdate = new HashMap<>();
        elementUpdate.put(element, "new title");

        doThrow(new JobOfferNotFoundException()).when(offersService).updateOfferElement(id, element, elementUpdate);

        mockMvc.perform(patch("/offers/{id}", id)
                .content(objectMapper.writeValueAsString(elementUpdate))
                .header("Content-Type", "application/json")
                .param("element", element))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof JobOfferNotFoundException));
    }

    @Test
    public void shouldDeleteOfferIfExist() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/offers/{id}", id)).andExpect(status().isNoContent());

        verify(offersService).deleteOfferById(id);
    }

    @Test
    public void shouldReturnJobOfferNotFoundOnDeleteIfOfferDoesNotExist() throws Exception {
        long id = 1L;

        doThrow(new JobOfferNotFoundException()).when(offersService).deleteOfferById(id);

        mockMvc.perform(delete("/offers/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof JobOfferNotFoundException));
    }


}
