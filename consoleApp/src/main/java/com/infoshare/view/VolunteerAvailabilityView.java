package com.infoshare.view;

import com.infoshare.domain.Volunteer;
import com.infoshare.service.VolunteerService;
import com.infoshare.util.Util;
import com.infoshare.util.ValidatorEnum;

import java.util.Optional;


public class VolunteerAvailabilityView {

  private static final String AVAILABILITY_HEADER = "Czy chcesz zmienić swoją dostępność? [ Y / N ] ";
  private static final String AVAILABILITY_STATUS = "Twoj obecny status w systemie to: ";
  private static final String AVAILABILITY_STATUS_UPDATE = "Twoj status został zmieniony na: ";
  private static final String GET_EMAIL = "Podaj adres e-mail podany podczas rejestracji";
  private static final String STATUS_AVAILABLE = "DOSTĘPNY";
  private static final String STATUS_UNAVAILABLE = "NIEDOSTĘPNY";
  private static final String EMAIL_UNKNOWN = "Podany e-mail nie jest zarejestrowany w bazie danych";

  VolunteerService volunteerService;

  public VolunteerAvailabilityView(VolunteerService volunteerService) {
    this.volunteerService = volunteerService;
  }

  private void changeAvailability(Volunteer volunteer) {
    System.out.println(AVAILABILITY_STATUS);
    System.out.println(volunteer.isAvailable() ? STATUS_AVAILABLE : STATUS_UNAVAILABLE);
    String data = Util.readDataFromConsole(AVAILABILITY_HEADER, ValidatorEnum.YESNO);
    if (data.equalsIgnoreCase("Y")) {
      if (volunteerService.updateAvailability(volunteer)) {
        System.out.println(AVAILABILITY_STATUS_UPDATE);
        System.out.println(volunteer.isAvailable() ? STATUS_AVAILABLE : STATUS_UNAVAILABLE);
      }
    }
  }

  public void handleVolunteerChangeAvailabilityProcess() {
    Optional<Volunteer> volunteer = volunteerService
        .searchForVolunteer(Util.readDataFromConsole(GET_EMAIL, ValidatorEnum.EMAIL).toLowerCase());
    if (volunteer.isPresent()) {
      changeAvailability(volunteer.get());
    } else {
      System.out.println(EMAIL_UNKNOWN);
    }
  }
}
