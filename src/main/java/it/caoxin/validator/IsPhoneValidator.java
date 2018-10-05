package it.caoxin.validator;


import it.caoxin.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.ValidationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class IsPhoneValidator implements ConstraintValidator<IsPhone,String> {
    private boolean required = false;

    @Override
    public void initialize(IsPhone constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required){
            return ValidatorUtil.isMobile(value);
        }else {
            if (StringUtils.isEmpty(value)){
                return true;
            }else {
                return  ValidatorUtil.isMobile(value);
            }
        }
    }
}
