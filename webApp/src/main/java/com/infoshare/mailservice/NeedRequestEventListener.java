package com.infoshare.mailservice;

import com.infoshare.domain.Volunteer;
import com.infoshare.event.NeedRequestEvent;
import com.infoshare.service.EventType;
import com.infoshare.service.VolunteerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class NeedRequestEventListener {

    private static final String EMAIL_SUBJECT = "New person in need registered in your location!";

    private final MailService mailService;
    private final VolunteerService volunteerService;

    public NeedRequestEventListener(MailService mailService, VolunteerService volunteerService) {
        this.mailService = mailService;
        this.volunteerService = volunteerService;
    }

    @EventListener
    public void update(NeedRequestEvent needRequestEvent) {
        if (needRequestEvent.getEventType().equals(EventType.ADD)) {
            log.info("Searching for volunteers to notify with params :"+needRequestEvent.getNeedRequest().getPersonInNeed().getLocation()+ " : "+needRequestEvent.getNeedRequest().getTypeOfHelp().getType());
            List<Volunteer> volunteersToNotify = volunteerService.getVolunteerFilteredList(needRequestEvent.getNeedRequest().getPersonInNeed().getLocation(), needRequestEvent.getNeedRequest().getTypeOfHelp());
            if (!volunteersToNotify.isEmpty()){
                String[] emails = volunteersToNotify.stream()
                    .map(Volunteer::getEmail)
                    .collect(toList())
                    .toArray(String[]::new);
            log.info("Found "+volunteersToNotify.size()+" volunteers");
            mailService.sendMail(EMAIL_SUBJECT, needRequestEvent.getNeedRequest().toString(), emails);
            }
        }
    }
}
