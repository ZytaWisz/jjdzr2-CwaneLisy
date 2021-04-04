package com.infoshare.validator;

import com.infoshare.domain.Volunteer;
import com.infoshare.formobjects.VolunteerForm;
import com.infoshare.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueMailAndUuidValidator implements ConstraintValidator<UniqueMailAndUuid, VolunteerForm> {

    @Autowired
    private VolunteerService volunteerService;

    @Override
    public boolean isValid(VolunteerForm volunteerForm, ConstraintValidatorContext context) {
        Optional<Volunteer> volunteer = volunteerService.searchForVolunteer(volunteerForm.getEmail());
        if(volunteer.isEmpty()){
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email").addConstraintViolation();
            return volunteer.get().getUuid().equals(volunteerForm.getUuid());
        }
    }
}
