package com.infoshare.repository;

import com.infoshare.domain.Volunteer;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class VolunteerDao extends GenericDao<Volunteer> {

    public Volunteer getVolunteer(String email){
        try{
            final Query query = entityManager.createQuery("SELECT u FROM Volunteer u WHERE lower(u.email) = :param1", Volunteer.class);
            query.setParameter("param1", email.toLowerCase());
            return (Volunteer) query.getSingleResult();
        }
        catch (NoResultException noResultException){
            return null;
        }
    }

}
