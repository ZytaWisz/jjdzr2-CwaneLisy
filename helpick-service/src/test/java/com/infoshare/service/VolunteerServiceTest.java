package com.infoshare.service;

import com.infoshare.database.DB;
import com.infoshare.domain.TypeOfHelp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.infoshare.domain.Volunteer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class VolunteerServiceTest {

    private final DB db = mock(DB.class);
    private final VolunteerService volunteerService = new VolunteerService(db);
    private final ArgumentCaptor<Volunteer> volunteerArgumentCaptor = ArgumentCaptor.forClass(Volunteer.class);

    @Test
    public void ifNewVolunteerRegisteredForValidInput() {
        //Given & when
        boolean isNewVolunteerRegistered = volunteerService.registerNewVolunteer("Anna", "Warszawa", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true);
        //Then
        assertThat(isNewVolunteerRegistered).isTrue();
        }

    @Test
    public void ifNewVolunteerRegisteredWithEmailAlreadyUsedInDb(){
        //Given
        Volunteer volunteerInDb = new Volunteer( "Janko","Gdansk", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, UUID.randomUUID());
        when(db.getVolunteer("janko@gmail.com")).thenReturn(Optional.of(volunteerInDb));
        //When
        boolean isNewVolunteerRegistered = volunteerService.registerNewVolunteer("Anna", "Warszawa", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true);
        //Then
        assertThat(isNewVolunteerRegistered).isFalse();
    }

   @Test
   public void testEditVolunteerDataForUuidInDb(){
       //Given
       UUID testUuid = UUID.randomUUID();
       Volunteer volunteerInDb = new Volunteer( "Janko","Gdansk", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, testUuid);
       when(db.getVolunteer(testUuid)).thenReturn(Optional.of(volunteerInDb));
       //When
       boolean isEditionSuccessful = volunteerService.editVolunteerData("Franko","Gdansk", "franko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, testUuid);
       //Then
       assertThat(isEditionSuccessful).isTrue();
       verify(db,times(1)).getVolunteer(testUuid);
       verify(db, times(1)).updateVolunteer(new Volunteer("Franko","Gdansk", "franko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, testUuid));
   }

   @Test
    public void testEditVolunteerDataForUuidNotSavedInDb(){
        //Given
       UUID uuid = UUID.randomUUID();
       when(db.getVolunteer((UUID) any())).thenReturn(Optional.empty());
       //When
       boolean isEditionSuccessful = volunteerService.editVolunteerData("Franko","Gdansk", "franko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, uuid);
       //Then
       assertThat(isEditionSuccessful).isFalse();
       verify(db, times(1)).getVolunteer(uuid);
       verifyNoMoreInteractions(db);
   }

    @Test
    public void testChangeAvailabilityStatusWhenVolunteerIsNull(){
        //Given
        Volunteer nullVolunteer = null;
        //When
        boolean isVolunteerStatusUpdated = volunteerService.updateAvailability(nullVolunteer);
        //Then
        assertThat(isVolunteerStatusUpdated).isFalse();
    }

    @Test
    public void testChangeAvailabilityStatusWhenVolunteerNotNull(){
        //Given
        Volunteer volunteer = new Volunteer( "Janko","Gdansk", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, UUID.randomUUID());
        //When
        boolean isVolunteerStatusUpdated = volunteerService.updateAvailability(volunteer);
        //Then
        assertThat(isVolunteerStatusUpdated).isTrue();
    }

    @Test
    public void testChangeAvailabilityStatusFromTrueToFalse(){
        //Given
        Volunteer volunteer = new Volunteer( "Janko","Gdansk", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, true, UUID.randomUUID());
        //When
        boolean isVolunteerStatusUpdated = volunteerService.updateAvailability(volunteer);
        //Then
        assertThat(isVolunteerStatusUpdated).isTrue();
        assertThat(volunteer.isAvailable()).isFalse();
        Mockito.verify(db).saveVolunteer(volunteerArgumentCaptor.capture());
        Volunteer volunteerCaptured = volunteerArgumentCaptor.getValue();
        assertThat(volunteerCaptured).isEqualTo(volunteer);
    }

    @Test
    public void testChangeAvailabilityStatusFromFalseToTrue(){
        //Given
        Volunteer volunteer = new Volunteer( "Janko","Gdansk", "janko@gmail.com", "123654789", TypeOfHelp.SHOPPING, false, UUID.randomUUID());
        //When
        boolean isVolunteerStatusUpdated = volunteerService.updateAvailability(volunteer);
        //Then
        assertThat(isVolunteerStatusUpdated).isTrue();
        assertThat(volunteer.isAvailable()).isTrue();
        Mockito.verify(db).saveVolunteer(volunteerArgumentCaptor.capture());
        Volunteer volunteerCaptured = volunteerArgumentCaptor.getValue();
        assertThat(volunteerCaptured).isEqualTo(volunteer);
    }

}
