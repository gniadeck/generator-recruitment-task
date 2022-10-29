package dev.komp15.generatorrecruitmenttask.utils.validation.job;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = JobMinimumLengthValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface JobMinLengthSmallerThanMaxLength {
    String message() default "Minimum length should be smaller than maximum length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
