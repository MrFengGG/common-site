package com.feng.home.common.validate;

import com.feng.home.common.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

/**
 * 校验工具类
 */
public class ValidationUtil {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> validateSet = validatorFactory.getValidator().validate(t);
        Optional<ConstraintViolation<T>> constraintViolation = validateSet.stream().findFirst();
        constraintViolation.ifPresent(violation -> {
            throw new BusinessException(violation.getMessage());
        });
    }
}
