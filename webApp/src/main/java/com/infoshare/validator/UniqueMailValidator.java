package com.infoshare.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.infoshare.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueMailValidator implements ConstraintValidator <IsMailRegistered, String> {

    @Autowired
    private VolunteerService volunteerService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && volunteerService.searchForVolunteer(value).isPresent();
    }
}
