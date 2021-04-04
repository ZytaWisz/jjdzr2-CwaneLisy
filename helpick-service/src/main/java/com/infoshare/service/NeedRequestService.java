package com.infoshare.service;

import com.infoshare.database.DB;
import com.infoshare.domain.HelpStatuses;
import com.infoshare.domain.NeedRequest;
import com.infoshare.domain.PersonInNeed;
import com.infoshare.domain.TypeOfHelp;
import com.infoshare.dto.NeedRequestFilterForm;
import com.infoshare.dto.NeedRequestListObject;
import com.infoshare.event.NeedRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NeedRequestService {

    private final DB db;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public NeedRequestService(@Qualifier("SpringDataDb") DB db) { this.db = db; }

    public List<NeedRequestListObject> getRequestFilteredList(NeedRequestFilterForm needRequestFilterForm) {
        List<NeedRequestListObject> needRequestListObjects = db.getNeedRequests().stream()
                .filter(needRequest -> filterHelpStatus(needRequestFilterForm, needRequest))
                .filter(needRequest -> filterTypeOfHelp(needRequestFilterForm, needRequest))
                .filter(needRequest -> filterDates(needRequestFilterForm, needRequest))
                .filter(needRequest -> filterLocation(needRequestFilterForm, needRequest))
                .filter(needRequest -> filterFreeText(needRequestFilterForm, needRequest))
                .map(this::convertToNeedRequestForm)
                .collect(Collectors.toList());

        log.info("For following query params {} found {} records", needRequestFilterForm, needRequestListObjects.size());
        return needRequestListObjects;
    }

    public void updateNeedRequest(String name, String location, String phone, TypeOfHelp typeOfHelp, UUID uuid) {
        Optional<NeedRequest> needRequestOptional = db.getNeedRequests().stream()
                .filter(needRequest1 -> needRequest1.getUuid().equals(uuid))
                .findAny();
        if (needRequestOptional.isPresent()) {
            NeedRequest needRequest = needRequestOptional.get();
            needRequest.getPersonInNeed().setLocation(location);
            needRequest.getPersonInNeed().setName(name);
            needRequest.getPersonInNeed().setPhone(phone);
            needRequest.setTypeOfHelp(typeOfHelp);
            db.saveNeedRequest(needRequest);
            applicationEventPublisher.publishEvent(new NeedRequestEvent(EventType.ADD, needRequest));
        }
    }

    public void createNeedRequest(String name, String location, String phone, TypeOfHelp typeOfHelp) {
        PersonInNeed personInNeed = new PersonInNeed(name, location, phone);
        NeedRequest needRequest = NeedRequest.create(typeOfHelp, personInNeed);
        db.saveNeedRequest(needRequest);
        applicationEventPublisher.publishEvent(new NeedRequestEvent(EventType.ADD, needRequest));
    }

    public void changeRequestStatus(List<NeedRequest> filteredList, int choice) {
        NeedRequest changedRequest = filteredList.get(choice - 1);
        changedRequest.setHelpStatus(HelpStatuses.INPROGRESS);
        changedRequest.setStatusChange(new Date());
        db.updateNeedRequest(changedRequest);
    }

    public void restoreStatusForExpiredRequests() {
        List<NeedRequest> activeNeedRequests = db.getNeedRequests();
        for (NeedRequest request : activeNeedRequests) {
            Date time1 = request.getStatusChange();
            Date actualTime = new Date();
            long diff = actualTime.getTime() - time1.getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (request.getHelpStatus().equals(HelpStatuses.INPROGRESS) &&
                    minutes > 1440) {
                log.info("Found difference > 24h in request " + request.getUuid() + ", changing status...");
                request.setHelpStatus(HelpStatuses.NEW);
                request.setStatusChange(new Date());
                log.info("Status of request ID " + request.getUuid() + " restored to NEW");
                db.updateNeedRequest(request);
            }
        }
    }

    public List<NeedRequest> getNeedRequestFilteredList(String city, TypeOfHelp typeOfHelp) {
        return db.getNeedRequests().stream()
                .filter(n -> n.getHelpStatus().equals(HelpStatuses.NEW))
                .filter(n -> n.getTypeOfHelp().equals(typeOfHelp))
                .filter(n -> n.getPersonInNeed().getLocation().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public List<TypeOfHelp> getTypesOfHelp() {
        return Arrays.asList(TypeOfHelp.values());
    }

    public List<HelpStatuses> getHelpStatuses() {
        return Arrays.asList(HelpStatuses.values());
    }

    private NeedRequestListObject convertToNeedRequestForm(NeedRequest needRequest) {
        return NeedRequestListObject.NeedRequestListObjectBuilder.aNeedRequestListObject()
                .withUuid(needRequest.getUuid())
                .withName(needRequest.getPersonInNeed().getName())
                .withPhone(needRequest.getPersonInNeed().getPhone())
                .withLocation(needRequest.getPersonInNeed().getLocation())
                .withTypeOfHelp(needRequest.getTypeOfHelp())
                .withStatusChange(needRequest.getStatusChange())
                .withHelpStatus(needRequest.getHelpStatus())
                .build();
    }

    private boolean filterHelpStatus(NeedRequestFilterForm needRequestFilterForm, NeedRequest needRequest) {
        return (needRequestFilterForm.getHelpStatuses() == null ||
                needRequestFilterForm.getHelpStatuses().isEmpty()) || needRequestFilterForm.getHelpStatuses().stream()
                .anyMatch(helpStatuses -> needRequest.getHelpStatus().equals(helpStatuses));
    }

    private boolean filterTypeOfHelp(NeedRequestFilterForm needRequestFilterForm, NeedRequest needRequest) {
        return (needRequestFilterForm.getTypeOfHelps() == null || needRequestFilterForm.getTypeOfHelps().isEmpty()) || needRequestFilterForm.getTypeOfHelps().stream()
                .anyMatch(typeOfHelp -> needRequest.getTypeOfHelp().equals(typeOfHelp));
    }

    private boolean filterDates(NeedRequestFilterForm needRequestFilterForm, NeedRequest needRequest) {
        boolean isFilterStartDateBeforeNeedRequestDate = (needRequestFilterForm.getStartDate() == null) ||
                needRequestFilterForm.getStartDate().isBefore(convertToLocalDateViaInstant(needRequest.getStatusChange())) ||
                needRequestFilterForm.getStartDate().isEqual(convertToLocalDateViaInstant(needRequest.getStatusChange()));
        boolean isFilterEndDateAfterNeedRequestDate = (needRequestFilterForm.getEndDate() == null) ||
                needRequestFilterForm.getEndDate().isAfter(convertToLocalDateViaInstant(needRequest.getStatusChange())) ||
                needRequestFilterForm.getEndDate().isEqual(convertToLocalDateViaInstant(needRequest.getStatusChange()));
        return isFilterStartDateBeforeNeedRequestDate && isFilterEndDateAfterNeedRequestDate;
    }

    private boolean filterLocation(NeedRequestFilterForm needRequestFilterForm, NeedRequest needRequest) {
        return (needRequestFilterForm.getLocation() == null || needRequestFilterForm.getLocation().isEmpty()) || needRequest.getPersonInNeed().getLocation().equalsIgnoreCase(needRequestFilterForm.getLocation());
    }

    private boolean filterFreeText(NeedRequestFilterForm needRequestFilterForm, NeedRequest needRequest) {
        return (needRequestFilterForm.getFreeText() == null || needRequestFilterForm.getFreeText().isEmpty()) ||
                needRequest.getPersonInNeed().getName().toLowerCase().contains(needRequestFilterForm.getFreeText().toLowerCase()) ||
                needRequest.getPersonInNeed().getLocation().toLowerCase().contains(needRequestFilterForm.getFreeText().toLowerCase()) ||
                needRequest.getPersonInNeed().getPhone().contains(needRequestFilterForm.getFreeText());
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
