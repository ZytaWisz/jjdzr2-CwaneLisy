package com.infoshare.database;

import com.infoshare.domain.NeedRequest;
import com.infoshare.domain.Volunteer;
import com.infoshare.repository.NeedRequestRepository;
import com.infoshare.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Qualifier("SpringDataDb")
public class SpringDataDb implements DB{

    private final VolunteerRepository volunteerRepository;
    private final NeedRequestRepository needRequestRepository;

    public SpringDataDb(VolunteerRepository volunteerRepository, NeedRequestRepository needRequestRepository) {
        this.volunteerRepository = volunteerRepository;
        this.needRequestRepository = needRequestRepository;
    }

    @Override
    public void saveNeedRequest(NeedRequest needRequest) {
        needRequestRepository.save(needRequest);
    }

    @Override
    public List<NeedRequest> getNeedRequests() {
        return needRequestRepository.findAll();
    }

    @Override
    public void updateNeedRequest(NeedRequest needRequest) {
        needRequestRepository.save(needRequest);
    }

    @Override
    public void saveVolunteer(Volunteer volunteer) {
        volunteerRepository.save(volunteer);
    }

    @Override
    public List<Volunteer> getVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public Optional<Volunteer> getVolunteer(String email) {
        return volunteerRepository.getVolunteerByEmail(email);
    }

    @Override
    public Optional<Volunteer> getVolunteer(UUID uuid) {
        return volunteerRepository.findById(uuid);
    }

    @Override
    public void updateVolunteer(Volunteer volunteer) {
        volunteerRepository.save(volunteer);
    }
}
