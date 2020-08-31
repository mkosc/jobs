package com.mkosc.jobs.repository;

import com.mkosc.jobs.domain.JobOffer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffersRepository extends CrudRepository<JobOffer, Long> {

    List<JobOffer> findAllByTitle(String title);

    List<JobOffer> findAllByCompanyName(String companyName);
}
