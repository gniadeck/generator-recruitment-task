package dev.komp15.generatorrecruitmenttask.utils.validation.job;

import dev.komp15.generatorrecruitmenttask.entity.Job;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JobMinimumLengthValidator implements ConstraintValidator<JobMinLengthSmallerThanMaxLength, Job> {
    @Override
    public boolean isValid(Job job, ConstraintValidatorContext constraintValidatorContext) {
        return job.getMinLength() <= job.getMaxLength();
    }
}
