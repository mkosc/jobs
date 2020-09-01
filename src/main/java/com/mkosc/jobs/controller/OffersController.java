package com.mkosc.jobs.controller;

import com.mkosc.jobs.domain.JobOffer;
import com.mkosc.jobs.dto.JobOfferSearchDTO;
import com.mkosc.jobs.service.OffersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    OffersService offersService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getOfferById(@PathVariable long id) {
        return new ResponseEntity<>(offersService.getOfferById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<JobOffer>> getAllOffers() {
        return new ResponseEntity<>(offersService.getAllOffers(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getOffersByTitle(@RequestParam(name = "title", required = false) String title,
                                              @RequestParam(name = "companyName", required = false) String companyName) {
        if (title != null) {
            List<JobOfferSearchDTO> jobOfferSearchDTOs = offersService.getOffersByTitle(title)
                    .stream()
                    .map(offer -> modelMapper.map(offer, JobOfferSearchDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(jobOfferSearchDTOs, HttpStatus.OK);
        } else if (companyName != null) {
            List<JobOfferSearchDTO> jobOfferSearchDTOs = offersService.getOffersByCompanyName(companyName)
                    .stream()
                    .map(offer -> modelMapper.map(offer, JobOfferSearchDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(jobOfferSearchDTOs, HttpStatus.OK);
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
        return new ResponseEntity<>(offersService.updateOffer(id, jobOffer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOfferById(@PathVariable long id) {
        offersService.deleteOfferById(id);
        return ResponseEntity.ok("{}");
    }
}
