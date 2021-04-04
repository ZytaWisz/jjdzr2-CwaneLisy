package com.infoshare.database;

import com.infoshare.domain.NeedRequest;
import com.infoshare.domain.Volunteer;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DB {

    void saveNeedRequest(NeedRequest needRequest);

    List<NeedRequest> getNeedRequests();

    void updateNeedRequest(NeedRequest needRequest);

    void saveVolunteer(Volunteer volunteer);

    List<Volunteer> getVolunteers();

    Optional<Volunteer> getVolunteer(String email);

    Optional<Volunteer> getVolunteer(UUID uuid);

    void updateVolunteer(Volunteer volunteer);
}
