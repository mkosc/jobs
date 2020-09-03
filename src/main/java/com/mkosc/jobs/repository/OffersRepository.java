package com.mkosc.jobs.repository;

import com.mkosc.jobs.domain.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffersRepository extends PagingAndSortingRepository<JobOffer, Long> {

    Page<JobOffer> findAllByTitle(Pageable pageable, String title);

    Page<JobOffer> findAllByCompanyName(Pageable pageable, String companyName);
}
