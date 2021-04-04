package com.infoshare.database;

import com.infoshare.domain.NeedRequest;
import com.infoshare.domain.Volunteer;
import com.infoshare.repository.NeedRequestDao;
import com.infoshare.repository.VolunteerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Qualifier("RelationalDb")
public class RelationalDb implements DB {

    private NeedRequestDao needRequestDao;
    private VolunteerDao volunteerDao;

    @Autowired
    public RelationalDb(final NeedRequestDao needRequestDao, VolunteerDao volunteerDao) {
        this.needRequestDao = needRequestDao;
        this.volunteerDao = volunteerDao;
    }

    @Override
    public void saveNeedRequest(NeedRequest needRequest) {
        needRequestDao.save(needRequest);
    }

    @Override
    public List<NeedRequest> getNeedRequests() {
        return (List<NeedRequest>) needRequestDao.findAll();
    }

    @Override
    public void updateNeedRequest(NeedRequest needRequest){
        needRequestDao.update(needRequest);
    }

    @Override
    public void saveVolunteer(Volunteer volunteer) {
        volunteerDao.save(volunteer);
    }

    @Override
    public List<Volunteer> getVolunteers() {
        return (List<Volunteer>) volunteerDao.findAll();
    }

    @Override
    public Optional<Volunteer> getVolunteer(String email) {
        return Optional.ofNullable(volunteerDao.getVolunteer(email));
    }

    @Override
    public Optional<Volunteer> getVolunteer(UUID uuid) {
        return Optional.ofNullable(volunteerDao.find(uuid));
    }

    @Override
    public void updateVolunteer(Volunteer volunteer) {
        volunteerDao.update(volunteer);
    }
}
