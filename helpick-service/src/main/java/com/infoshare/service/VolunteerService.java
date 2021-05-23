package com.infoshare.service;

import com.infoshare.database.DB;
import com.infoshare.domain.TypeOfHelp;
import com.infoshare.domain.Volunteer;
import com.infoshare.dto.VolunteerFilterForm;
import com.infoshare.dto.VolunteerListObject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VolunteerService {

    DB db;

    @Autowired
    public VolunteerService(@Qualifier ("SpringDataDb") DB db) {
        this.db = db;
    }

    private VolunteerListObject convertToVolunteerForm(Volunteer volunteer) {
        return VolunteerListObject.VolunteerListObjectBuilder.aVolunteerListObject()
                .withUuid(volunteer.getUuid())
                .withName(volunteer.getName())
                .withPhone(volunteer.getPhone())
                .withEmail(volunteer.getEmail())
                .withLocation(volunteer.getLocation())
                .withTypeOfHelp(volunteer.getTypeOfHelp())
                .withIsAvailable(volunteer.isAvailable())
                .build();
    }
    private boolean filterTypeOfHelp(VolunteerFilterForm volunteerFilterForm, Volunteer volunteer) {
        return (volunteerFilterForm.getTypeOfHelps() == null || volunteerFilterForm.getTypeOfHelps().isEmpty()) || volunteerFilterForm.getTypeOfHelps().stream()
                .anyMatch(typeOfHelp -> volunteer.getTypeOfHelp().equals(typeOfHelp));
    }
    private boolean filterLocation(VolunteerFilterForm volunteerFilterForm, Volunteer volunteer) {
        return (volunteerFilterForm.getLocation() == null || volunteerFilterForm.getLocation().isEmpty()) || volunteer.getLocation().equalsIgnoreCase(volunteerFilterForm.getLocation());
    }
    private boolean filterFreeText(VolunteerFilterForm volunteerFilterForm, Volunteer volunteer) {
        return (volunteerFilterForm.getFreeText() == null || volunteerFilterForm.getFreeText().isEmpty()) ||
                volunteer.getName().toLowerCase().contains(volunteerFilterForm.getFreeText().toLowerCase()) ||
                volunteer.getLocation().toLowerCase().contains(volunteerFilterForm.getFreeText().toLowerCase()) ||
                volunteer.getPhone().contains(volunteerFilterForm.getFreeText());
    }
    private boolean filterAvailability(VolunteerFilterForm volunteerFilterForm, Volunteer volunteer) {
        if(!volunteer.isAvailable() && volunteerFilterForm.isAvailable()){
            return false;
        }else{
          return true;
        }
    }

    public List<VolunteerListObject> getVolunteerFilteredList(VolunteerFilterForm volunteerFilterForm) {
        List<VolunteerListObject> volunteerListObjects = db.getVolunteers().stream()
                .filter(volunteer -> filterTypeOfHelp(volunteerFilterForm, volunteer))
                .filter(volunteer -> filterLocation(volunteerFilterForm, volunteer))
                .filter(volunteer -> filterFreeText(volunteerFilterForm, volunteer))
                .filter(volunteer -> filterAvailability(volunteerFilterForm,volunteer))
            .sorted(Comparator.comparing(Volunteer::getCreateDate).reversed())
                .map(this::convertToVolunteerForm)
                .collect(Collectors.toList());

        log.info("For following query params {} found {} records", volunteerFilterForm, volunteerListObjects.size());
        return volunteerListObjects;
    }

    public List<Volunteer> getVolunteerFilteredList(String city, TypeOfHelp typeOfHelp) {
        return db.getVolunteers().stream()
                .filter(Volunteer::isAvailable)
                .filter(v -> v.getLocation().equalsIgnoreCase(city))
                .filter(v -> v.getTypeOfHelp().equals(typeOfHelp))
                .collect(Collectors.toList());
    }

    public Optional<Volunteer> searchForVolunteer(String email) {
        return db.getVolunteer(email);
    }

    public boolean updateAvailability(Volunteer volunteer) {
        if (volunteer != null) {
            volunteer.setAvailable(!volunteer.isAvailable());
            db.saveVolunteer(volunteer);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerNewVolunteer(String name, String location, String email, String phone, TypeOfHelp typeOfHelp,
                                        boolean availability) {
        Volunteer newVolunteer = new Volunteer(name, location, email, phone, typeOfHelp, availability, LocalDateTime.now());
        if (db.getVolunteer(newVolunteer.getEmail()).isEmpty()){
            db.saveVolunteer(newVolunteer);
            return true;
        } else {
            return false;
        }
    }

    public List<TypeOfHelp> getTypesOfHelp() {
        return Arrays.asList(TypeOfHelp.values());
    }

    public boolean editVolunteerData(String name, String location, String email, String phone, TypeOfHelp typeOfHelp,
                                     boolean availability, UUID uuid) {
        Optional<Volunteer> volunteer = db.getVolunteer(uuid);
        if (volunteer.isPresent()) {
            Volunteer volunteerToEdit = volunteer.get();
            volunteerToEdit.setName(name);
            volunteerToEdit.setLocation(location);
            volunteerToEdit.setPhone(phone);
            volunteerToEdit.setTypeOfHelp(typeOfHelp);
            volunteerToEdit.setAvailable(availability);
            volunteerToEdit.setEmail(email);
            db.updateVolunteer(volunteerToEdit);
            return true;
        } else {
            return false;
        }
    }
}
