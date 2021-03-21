package com.infoshare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infoshare.domain.HelpStatuses;
import com.infoshare.domain.TypeOfHelp;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class NeedRequestListObject {

    private String name;
    private String location;
    private String phone;
    private TypeOfHelp typeOfHelp;
    private UUID uuid;
    private HelpStatuses helpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statusChange;

    @Override
    public String toString() {
        return "NeedRequestListObject{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", typeOfHelp=" + typeOfHelp +
                ", helpStatus=" + helpStatus +
                ", statusChange=" + statusChange +
                '}';
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TypeOfHelp getTypeOfHelp() {
        return typeOfHelp;
    }

    public void setTypeOfHelp(TypeOfHelp typeOfHelp) {
        this.typeOfHelp = typeOfHelp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeedRequestListObject that = (NeedRequestListObject) o;
        return Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(phone, that.phone) && typeOfHelp == that.typeOfHelp && Objects.equals(uuid, that.uuid) && helpStatus == that.helpStatus && Objects.equals(statusChange, that.statusChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, phone, typeOfHelp, uuid, helpStatus, statusChange);
    }

    public static final class NeedRequestListObjectBuilder {
        private String name;
        private String location;
        private String phone;
        private TypeOfHelp typeOfHelp;
        private UUID uuid;
        private HelpStatuses helpStatus;
        private Date statusChange;

        private NeedRequestListObjectBuilder() {
        }

        public static NeedRequestListObjectBuilder aNeedRequestListObject() {
            return new NeedRequestListObjectBuilder();
        }

        public NeedRequestListObjectBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public NeedRequestListObjectBuilder withLocation(String location) {
            this.location = location;
            return this;
        }

        public NeedRequestListObjectBuilder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public NeedRequestListObjectBuilder withTypeOfHelp(TypeOfHelp typeOfHelp) {
            this.typeOfHelp = typeOfHelp;
            return this;
        }

        public NeedRequestListObjectBuilder withUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public NeedRequestListObjectBuilder withHelpStatus(HelpStatuses helpStatus) {
            this.helpStatus = helpStatus;
            return this;
        }

        public NeedRequestListObjectBuilder withStatusChange(Date statusChange) {
            this.statusChange = statusChange;
            return this;
        }

        public NeedRequestListObject build() {
            NeedRequestListObject needRequestListObject = new NeedRequestListObject();
            needRequestListObject.setName(name);
            needRequestListObject.setLocation(location);
            needRequestListObject.setPhone(phone);
            needRequestListObject.setTypeOfHelp(typeOfHelp);
            needRequestListObject.setUuid(uuid);
            needRequestListObject.setHelpStatus(helpStatus);
            needRequestListObject.setStatusChange(statusChange);
            return needRequestListObject;
        }
    }
}
