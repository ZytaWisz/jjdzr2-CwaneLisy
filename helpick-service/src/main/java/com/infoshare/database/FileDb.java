package com.infoshare.database;

import com.infoshare.domain.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier ("FileDb")
public class FileDb implements DB {

    private static final String VOLUNTEER_DB_FILE_NAME = "Volunteer.csv";
    private static final String REQUEST_DB_FILE = "NeedRequest.csv";
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FileDb() {
        if (!Files.exists(Paths.get(VOLUNTEER_DB_FILE_NAME))) {
            try {
                Files.createFile(Paths.get(VOLUNTEER_DB_FILE_NAME));
            } catch (IOException e) {
                System.out.println("Plik" + VOLUNTEER_DB_FILE_NAME + " nie moze zostac stworzony");
            }
        }
        if (!Files.exists(Paths.get(REQUEST_DB_FILE))) {
            try {
                Files.createFile(Paths.get(REQUEST_DB_FILE));
            } catch (IOException e) {
                System.out.println("Plik" + REQUEST_DB_FILE + " nie moze zostac stworzony");
            }
        }
    }

    @Override
    public void saveVolunteer(Volunteer volunteer) {
        if(volunteer.getUuid() == null){
            volunteer.setUuid(UUID.randomUUID());
        }
        List<Volunteer> allVolunteers = getVolunteers();
        boolean isNewVolunteer = true;
        for (int i = 0; i < allVolunteers.size(); i++) {
            Volunteer volunteerFromDb = allVolunteers.get(i);
            if (volunteerFromDb.getUuid().equals(volunteer.getUuid())) {
                isNewVolunteer = false;
                allVolunteers.set(i, volunteer);
                break;
            }
        }
        if (isNewVolunteer) {
            allVolunteers.add(volunteer);
        }
        saveVolunteers(allVolunteers);
    }

    @Override
    public void saveNeedRequest(NeedRequest needRequest) {
        if(needRequest.getUuid() == null){
            needRequest.setUuid(UUID.randomUUID());
        }
        List<NeedRequest> needRequests = getNeedRequests();
        Optional<Integer> optionalIndex = needRequests.stream()
                .filter(needRequest1 -> needRequest1.getUuid().equals(needRequest.getUuid()))
                .map(needRequests::indexOf)
                .findAny();
        if (optionalIndex.isPresent()) {
            needRequests.set(optionalIndex.get(), needRequest);
        } else {
            needRequests.add(needRequest);
        }
        saveNeedRequestList(needRequests);
    }

    @Override
    public List<NeedRequest> getNeedRequests() {

        List<NeedRequest> result = new ArrayList<>();
        try {
            try (Scanner scanner = new Scanner(new File(REQUEST_DB_FILE))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] splited = line.split(",");
                    PersonInNeed person = new PersonInNeed(splited[3], splited[4], splited[5]);
                    NeedRequest needRequest = new NeedRequest(TypeOfHelp.valueOf(splited[0]),
                            HelpStatuses.valueOf(splited[1]), df.parse(splited[2]), person, UUID.fromString(splited[6]));
                    result.add(needRequest);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void updateNeedRequest(NeedRequest needRequest){
        saveNeedRequest(needRequest);
    };

    @Override
    public List<Volunteer> getVolunteers() {
        List<Volunteer> result = new ArrayList<>();
        try {
            try (Scanner scanner = new Scanner(new File(VOLUNTEER_DB_FILE_NAME))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] volunteerAtributes = line.split(",");
                    result.add(new Volunteer(volunteerAtributes[0], volunteerAtributes[1], volunteerAtributes[2],
                            volunteerAtributes[3], TypeOfHelp.valueOf(volunteerAtributes[4]),
                            Boolean.parseBoolean(volunteerAtributes[5]), UUID.fromString(volunteerAtributes[6])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<Volunteer> getVolunteer(String email) {

        try {
            try (Scanner scanner = new Scanner(new File(VOLUNTEER_DB_FILE_NAME))) {

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] volunteerAtributes = line.split(",");
                    if (volunteerAtributes.length >= 6 && volunteerAtributes[2].equalsIgnoreCase(email)) {
                        return Optional.of(new Volunteer(volunteerAtributes[0], volunteerAtributes[1], volunteerAtributes[2],
                                volunteerAtributes[3], TypeOfHelp.valueOf(volunteerAtributes[4]),
                                Boolean.parseBoolean(volunteerAtributes[5]), UUID.fromString(volunteerAtributes[6]))) ;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Volunteer> getVolunteer(UUID uuid) {
        return getVolunteers().stream()
                .filter(v -> v.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public void updateVolunteer(Volunteer volunteer) {
        saveVolunteer(volunteer);
    }

    private void saveVolunteers(List<Volunteer> volunteers)
    {
        try {
            try (FileWriter writer = new FileWriter(VOLUNTEER_DB_FILE_NAME, false)) {
                for (Volunteer v : volunteers) {
                    writer.write(v.getName() + "," +
                            v.getLocation() + "," +
                            v.getEmail() + "," +
                            v.getPhone() + "," +
                            v.getTypeOfHelp() + "," +
                            v.isAvailable() + "," +
                            v.getUuid() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNeedRequestList(List<NeedRequest> needRequests) {
        try {
            try (FileWriter fileWriter = new FileWriter(REQUEST_DB_FILE, false)) {
                for (NeedRequest needRequest : needRequests) {
                    PersonInNeed person = needRequest.getPersonInNeed();
                    fileWriter.write(needRequest.getTypeOfHelp() + "," + needRequest.getHelpStatus() + "," + df
                            .format(needRequest.getStatusChange()) + ",");
                    fileWriter.write(person.getName() + "," + person.getLocation() + "," + person.getPhone() +
                            "," + needRequest.getUuid() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
