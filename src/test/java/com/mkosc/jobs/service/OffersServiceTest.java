package com.mkosc.jobs.service;

import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.dto.JobOfferSearchDTO;
import com.mkosc.jobs.repository.OffersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OffersServiceTest {

    @Mock
    OffersRepository offersRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    OffersService offersService;

    @Test
    public void shouldReturnAllOffers() {
        JobOffer offer1 = new JobOffer().withOfferId(1L).withTitle("title1").withDescription("description1").withCompanyName("company1");
        JobOffer offer2 = new JobOffer().withOfferId(2L).withTitle("title2").withDescription("description2").withCompanyName("company2");
        JobOffer offer3 = new JobOffer().withOfferId(3L).withTitle("title3").withDescription("description3").withCompanyName("company3");
        List<JobOffer> expectedOffers = Arrays.asList(offer1, offer2, offer3);

        when(offersRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(expectedOffers));

        List<JobOffer> returnedOffers = offersService.getAllOffers(PageRequest.of(0, 20)).getContent();
        for (int i = 0; i < expectedOffers.size(); i++) {
            assertEquals(expectedOffers.get(i).getOfferId(), returnedOffers.get(i).getOfferId());
            assertEquals(expectedOffers.get(i).getTitle(), returnedOffers.get(i).getTitle());
            assertEquals(expectedOffers.get(i).getDescription(), returnedOffers.get(i).getDescription());
            assertEquals(expectedOffers.get(i).getCompanyName(), returnedOffers.get(i).getCompanyName());
        }
    }

    @Test
    public void shouldReturnOffersByTitle() {
        String title = "title1";
        JobOffer offer1 = new JobOffer().withOfferId(1L).withTitle(title);
        List<JobOffer> expectedOffers = Collections.singletonList(offer1);

        when(offersRepository.findAllByTitle(any(), eq(title))).thenReturn(new PageImpl<>(expectedOffers));
        when(modelMapper.map(any(JobOffer.class), any())).thenReturn(new JobOfferSearchDTO()
                .withOfferId(expectedOffers.get(0).getOfferId())
                .withTitle(expectedOffers.get(0).getTitle()));

        List<JobOfferSearchDTO> returnedOfferDTOs = offersService.getOffersByTitle(PageRequest.of(0, 20), title).getContent();
        assertEquals(expectedOffers.get(0).getOfferId(), returnedOfferDTOs.get(0).getOfferId());
        assertEquals(expectedOffers.get(0).getTitle(), returnedOfferDTOs.get(0).getTitle());
    }

    @Test
    public void shouldReturnOffersByCompanyName() {
        String companyName = "company1";
        JobOffer offer1 = new JobOffer().withOfferId(1L).withCompanyName(companyName);
        List<JobOffer> expectedOffers = Collections.singletonList(offer1);

        when(offersRepository.findAllByTitle(any(), eq(companyName))).thenReturn(new PageImpl<>(expectedOffers));
        when(modelMapper.map(any(JobOffer.class), any())).thenReturn(new JobOfferSearchDTO()
                .withOfferId(expectedOffers.get(0).getOfferId())
                .withCompanyName(expectedOffers.get(0).getCompanyName()));

        List<JobOfferSearchDTO> returnedOfferDTOs = offersService.getOffersByTitle(PageRequest.of(0, 20), companyName).getContent();
        assertEquals(expectedOffers.get(0).getOfferId(), returnedOfferDTOs.get(0).getOfferId());
        assertEquals(expectedOffers.get(0).getCompanyName(), returnedOfferDTOs.get(0).getCompanyName());
    }

    @Test
    public void shouldReturnOfferById() {
        long id = 1L;
        JobOffer offer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("company");

        when(offersRepository.findById(id)).thenReturn(Optional.of(offer));

        JobOffer returnedOffer = offersService.getOfferById(id);
        assertEquals(offer.getOfferId(), returnedOffer.getOfferId());
        assertEquals(offer.getTitle(), returnedOffer.getTitle());
        assertEquals(offer.getDescription(), returnedOffer.getDescription());
        assertEquals(offer.getCompanyName(), returnedOffer.getCompanyName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenOfferDoesNotExist() {
        long id = 1L;

        when(offersRepository.findById(id)).thenReturn(Optional.empty());

        Exception returnedException = assertThrows(EntityNotFoundException.class, () -> offersService.getOfferById(id));
        assertEquals("Offer with id " + id + " not found", returnedException.getMessage());
    }

    @Test
    public void shouldCreateOffer() {
        long id = 1L;
        JobOffer offer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("company");

        when(offersRepository.save(offer)).thenReturn(offer);

        JobOffer returnedOffer = offersService.createOffer(offer);
        assertEquals(offer.getOfferId(), returnedOffer.getOfferId());
        assertEquals(offer.getTitle(), returnedOffer.getTitle());
        assertEquals(offer.getDescription(), returnedOffer.getDescription());
        assertEquals(offer.getCompanyName(), returnedOffer.getCompanyName());
    }

    @Test
    public void shouldUpdateOfferIfExists() {
        long id = 1L;
        JobOffer offerUpdateData = new JobOffer().withTitle("new title").withDescription("new description").withCompanyName("new company");
        JobOffer existingOffer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("company");

        when(offersRepository.findById(id)).thenReturn(Optional.of(existingOffer));
        when(offersRepository.save(offerUpdateData.withOfferId(id))).thenReturn(offerUpdateData.withOfferId(id));

        JobOffer returnedOffer = offersService.updateOffer(id, offerUpdateData);
        assertEquals(id, returnedOffer.getOfferId());
        assertEquals(offerUpdateData.getTitle(), returnedOffer.getTitle());
        assertEquals(offerUpdateData.getDescription(), returnedOffer.getDescription());
        assertEquals(offerUpdateData.getCompanyName(), returnedOffer.getCompanyName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUpdatingOfferThatDoesNotExist() {
        long id = 1L;
        JobOffer offerUpdateData = new JobOffer().withTitle("new title").withDescription("new description").withCompanyName("new company");

        when(offersRepository.findById(id)).thenReturn(Optional.empty());

        Exception returnedException = assertThrows(EntityNotFoundException.class, () -> offersService.updateOffer(id, offerUpdateData));
        assertEquals("Offer with id " + id + " not found", returnedException.getMessage());
    }

    @Test
    public void shouldDeleteOfferIfExists() {
        long id = 1L;
        JobOffer existingOffer = new JobOffer().withOfferId(id).withTitle("title").withDescription("description").withCompanyName("company");

        when(offersRepository.findById(id)).thenReturn(Optional.of(existingOffer));

        offersService.deleteOfferById(id);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletingOfferThatDoesNotExist() {
        long id = 1L;

        when(offersRepository.findById(id)).thenReturn(Optional.empty());

        Exception returnedException = assertThrows(EntityNotFoundException.class, () -> offersService.deleteOfferById(id));
        assertEquals("Offer with id " + id + " not found", returnedException.getMessage());
    }
}
