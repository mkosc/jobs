package com.mkosc.jobs.dto;

import com.mkosc.jobs.domain.ExperienceLevel;
import lombok.Data;

@Data
public class JobOfferSearchDTO {

    private long offerId;
    private String title;
    private String companyName;
    private int b2bMinSalary;
    private int EmploymentContractMinSalary;
    private ExperienceLevel experienceLevel;
    private String location;
    private boolean isRemote;
}
