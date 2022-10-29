package dev.komp15.generatorrecruitmenttask.utils.validation.job;

import dev.komp15.generatorrecruitmenttask.entity.Job;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JobSizeValidator implements ConstraintValidator<ValidJobSize, Job> {
    @Override
    public boolean isValid(Job job, ConstraintValidatorContext constraintValidatorContext) {
        long maxJobSize = factorial(job.getChars().length);
        return maxJobSize >= job.getJobSize();
    }

    private long factorial(long number){
        long result = 1;
        for(int i = 1; i < number; i++){
            result *= i;
            if(result < 0){
                return Long.MAX_VALUE;
            }
        }
        return result;
    }
}
