package com.mkosc.jobs.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class JobOffer {

    @Id
    @GeneratedValue
    private long offerId;
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String companyName;
    private String companySize;
    private int b2bMinSalary;
    private int b2bMaxSalary;
    private int EmploymentContractMinSalary;
    private int EmploymentContractMaxSalary;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    private String location;
    private boolean isRemote;

}
