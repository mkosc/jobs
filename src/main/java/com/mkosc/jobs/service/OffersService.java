package com.mkosc.jobs.service;

import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.dto.JobOfferDTO;
import com.mkosc.jobs.dto.JobOfferSearchDTO;
import com.mkosc.jobs.repository.OffersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class OffersService {

    @Autowired
    OffersRepository offersRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<JobOffer> getAllOffers(Pageable pageable) {
        return offersRepository.findAll(pageable);
    }

    public JobOffer getOfferById(long id) {
        return getOfferByIdIfExist(id);
    }

    public Page<JobOfferSearchDTO> getOffersByTitle(Pageable pageable, String title) {
        return offersRepository.findAllByTitle(pageable, title)
                .map(offer -> modelMapper.map(offer, JobOfferSearchDTO.class));
    }

    public Page<JobOfferSearchDTO> getOffersByCompanyName(Pageable pageable, String companyName) {
        return offersRepository.findAllByCompanyName(pageable, companyName)
                .map(offer -> modelMapper.map(offer, JobOfferSearchDTO.class));
    }

    public JobOffer createOffer(JobOffer jobOffer) {
        return offersRepository.save(jobOffer);
    }

    public JobOffer updateOffer(long id, JobOffer jobOffer) {
        getOfferByIdIfExist(id);
        jobOffer.setOfferId(id);
        return offersRepository.save(jobOffer);
    }

    public JobOfferDTO updateOfferElement(long id, String element, JobOfferDTO jobOfferDTO) {
        JobOffer offerToUpdate = getOfferByIdIfExist(id);
        PropertyAccessor sourceObjectAccessor = PropertyAccessorFactory.forBeanPropertyAccess(jobOfferDTO);
        PropertyAccessor destinationObjectAccessor = PropertyAccessorFactory.forBeanPropertyAccess(offerToUpdate);
        destinationObjectAccessor.setPropertyValue(element, sourceObjectAccessor.getPropertyValue(element));
        return modelMapper.map(offersRepository.save(offerToUpdate), JobOfferDTO.class);
    }

    public void deleteOfferById(long id) {
        getOfferByIdIfExist(id);
        offersRepository.deleteById(id);
    }

    public JobOffer getOfferByIdIfExist(long id) {
        return offersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Offer with id " + id + " not found"));
    }
}
