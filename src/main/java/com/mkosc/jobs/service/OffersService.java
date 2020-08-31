package com.mkosc.jobs.service;

import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.repository.OffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OffersService {

    @Autowired
    OffersRepository offersRepository;

    public List<JobOffer> getAllOffers() {
        return (List<JobOffer>) offersRepository.findAll();
    }

    public JobOffer getOfferById(long id) {
        return getOfferByIdIfExist(id);
    }

    public List<JobOffer> getOffersByTitle(String title) {
        return offersRepository.findAllByTitle(title);
    }

    public List<JobOffer> getOffersByCompanyName(String companyName) {
        return offersRepository.findAllByCompanyName(companyName);
    }

    public JobOffer createOffer(JobOffer jobOffer) {
        return offersRepository.save(jobOffer);
    }

    public JobOffer updateOffer(long id, JobOffer jobOffer) {
        getOfferByIdIfExist(id);
        jobOffer.setOfferId(id);
        return offersRepository.save(jobOffer);
    }

    public void deleteOfferById(long id) {
        getOfferByIdIfExist(id);
        offersRepository.deleteById(id);
    }

    public JobOffer getOfferByIdIfExist(long id) {
        return offersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Offer with id: " + id + " not found"));
    }
}
