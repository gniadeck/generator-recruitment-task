package dev.komp15.generatorrecruitmenttask.utils.validation.job;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = JobSizeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidJobSize {
    String message() default "Requested job size is not achievable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
