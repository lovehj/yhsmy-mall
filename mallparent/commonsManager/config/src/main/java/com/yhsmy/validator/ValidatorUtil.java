package com.yhsmy.validator;

import com.yhsmy.exception.ServiceException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * @auth 李正义
 * @date 2019/11/8 9:39
 **/
public class ValidatorUtil {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory ().getValidator ();
    }

    public static void validataEntity (Object obj, Class<?>... groups)
            throws ServiceException {
        Set<ConstraintViolation<Object>> constraintViolations =
                validator.validate (obj, groups);
        if (!constraintViolations.isEmpty ()) {
            ConstraintViolation<Object> validatorResult =
                    constraintViolations.iterator ().next ();
            throw new ServiceException (validatorResult.getMessage ());
        }
    }
}
