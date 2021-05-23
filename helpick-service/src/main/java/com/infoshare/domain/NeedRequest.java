package com.infoshare.domain;

import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
@Entity
public class NeedRequest {

    @Enumerated(EnumType.STRING)
    private TypeOfHelp typeOfHelp;
    @Enumerated(EnumType.STRING)
    private HelpStatuses helpStatus;
    private Date statusChange;
    @OneToOne (cascade = {CascadeType.ALL})
    private PersonInNeed personInNeed;
    private LocalDateTime createDate;
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "UUID", strategy = "uuid4")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "CHAR(36)")
    private UUID uuid;

    public NeedRequest(TypeOfHelp typeOfHelp, HelpStatuses helpStatus, Date statusChange, PersonInNeed personInNeed, UUID uuid){
        this(typeOfHelp, helpStatus, statusChange,personInNeed, LocalDateTime.now());
        this.uuid = uuid;
    }

    public NeedRequest(TypeOfHelp typeOfHelp, HelpStatuses helpStatus, Date statusChange, PersonInNeed personInNeed,
                       LocalDateTime createDate) {
        this.typeOfHelp = typeOfHelp;
        this.helpStatus = helpStatus;
        this.statusChange = statusChange;
        this.personInNeed = personInNeed;
        this.createDate=createDate;
    }

    public NeedRequest() {

    }

    public static NeedRequest create(TypeOfHelp typeOfHelp, PersonInNeed personInNeed) {
        return new NeedRequest(typeOfHelp, HelpStatuses.NEW, new Date(), personInNeed, LocalDateTime.now());
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public TypeOfHelp getTypeOfHelp() {
        return typeOfHelp;
    }

    public void setTypeOfHelp(TypeOfHelp typeOfHelp) {
        this.typeOfHelp = typeOfHelp;
    }

    public HelpStatuses getHelpStatus() {
        return helpStatus;
    }

    public void setHelpStatus(HelpStatuses helpStatus) {
        this.helpStatus = helpStatus;
    }

    public Date getStatusChange() {
        return statusChange;
    }

    public void setStatusChange(Date statusChange) {
        this.statusChange = statusChange;
    }

    public PersonInNeed getPersonInNeed() {
        return personInNeed;
    }

    public void setPersonInNeed(PersonInNeed personInNeed) {
        this.personInNeed = personInNeed;
    }

    @Override
    public String toString() {
        return "NeedRequest{" + "typeOfHelp=" + typeOfHelp + ", helpStatus=" + helpStatus + ", statusChange="
                + statusChange + ", personInNeed=" + personInNeed + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NeedRequest that = (NeedRequest) o;
        return typeOfHelp == that.typeOfHelp
                && helpStatus == that.helpStatus
                && statusChange.equals(that.statusChange)
                && personInNeed.equals(that.personInNeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfHelp, helpStatus, statusChange, personInNeed);
    }

    public void printDescription() {
        System.out
                .println("Imię osoby potrzebującej: " + personInNeed.getName() + ", miasto: " + personInNeed.getLocation()
                        + ", telefon: " + personInNeed.getPhone() + ", rodzaj potrzebnej pomocy: " + typeOfHelp.getType());
    }
}
