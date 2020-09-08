package com.mkosc.jobs.controller;

import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.dto.JobOfferDTO;
import com.mkosc.jobs.exception.ElementNotUpdatableException;
import com.mkosc.jobs.exception.JobOfferNotFoundException;
import com.mkosc.jobs.service.OffersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    OffersService offersService;

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getOfferById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(offersService.getOfferById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Offer with id {} not found", id);
            throw new JobOfferNotFoundException();
        }
    }

    @GetMapping
    public ResponseEntity<Page<JobOffer>> getAllOffers(Pageable pageable) {
        return new ResponseEntity<>(offersService.getAllOffers(pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getOffersByTitleOrCompanyName(Pageable pageable,
                                              @RequestParam(name = "title", required = false) String title,
                                              @RequestParam(name = "companyName", required = false) String companyName) {
        if (title != null) {
            return new ResponseEntity<>(offersService.getOffersByTitle(pageable, title), HttpStatus.OK);
        } else if (companyName != null) {
            return new ResponseEntity<>(offersService.getOffersByCompanyName(pageable, companyName), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<JobOffer> createOffer(@RequestBody @Valid JobOffer jobOffer) {
        return new ResponseEntity<>(offersService.createOffer(jobOffer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOffer> updateOffer(@PathVariable long id, @RequestBody @Valid JobOffer jobOffer) {
        try {
            return new ResponseEntity<>(offersService.updateOffer(id, jobOffer), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Offer with id {} not found", id);
            throw new JobOfferNotFoundException();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobOfferDTO> updateOfferElement(@PathVariable long id, @RequestParam(name="element") String element, @RequestBody @Valid JobOfferDTO jobOfferDTO) {
        try {
            return new ResponseEntity<>(offersService.updateOfferElement(id, element, jobOfferDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Offer with id {} not found", id);
            throw new JobOfferNotFoundException();
        } catch (NotReadablePropertyException e) {
            log.error("Element {} is not updatable", element);
            throw new ElementNotUpdatableException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOfferById(@PathVariable long id) {
        try {
            offersService.deleteOfferById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error("Offer with id {} not found", id);
            throw new JobOfferNotFoundException();
        }
    }
}
