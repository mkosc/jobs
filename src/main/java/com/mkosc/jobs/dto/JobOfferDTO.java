package com.mkosc.jobs.dto;

import com.mkosc.jobs.domain.EmploymentType;
import com.mkosc.jobs.domain.ExperienceLevel;
import lombok.Data;

@Data
public class JobOfferDTO {

    private String title;
    private String description;
    private String companyName;
    private String companySize;
    private int b2bMinSalary;
    private int b2bMaxSalary;
    private int EmploymentContractMinSalary;
    private int EmploymentContractMaxSalary;
    private ExperienceLevel experienceLevel;
    private EmploymentType employmentType;
    private String location;
    private boolean isRemote;

    public JobOfferDTO withTitle(String title) {
        this.title = title;
        return this;
    }

    public JobOfferDTO withDescription(String description) {
        this.description = description;
        return this;
    }

    public JobOfferDTO withCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }
}
