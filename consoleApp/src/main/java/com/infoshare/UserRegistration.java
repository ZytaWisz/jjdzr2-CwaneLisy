package com.infoshare;

import com.infoshare.domain.TypeOfHelp;
import com.infoshare.domain.Volunteer;

import java.util.Scanner;

public class UserRegistration {
    private static final String REGISTRATION_HEADER = "[Witaj w programie do rejestracji]";
    private static final String REGISTRATION_NAME = "Podaj imie/nickname (min 3 znaki): ";
    private static final String REGISTRATION_LOCATION = "Podaj swoją lokalizację (bez polskich znaków): ";
    private static final String REGISTRATION_PHONE_NUMBER = "Podaj numer telefonu: ";
    private static final String REGISTRATION_EMAIL = "Podaj e-mail";
    private static final String REGISTRATION_HELP_TYPE = "W czym możesz pomóc innym, wybierz z listy: ";
    private static final String REGISTRATION_ERROR = "***BŁĄD***";

    private String getVolunteerName(){
        String username = "";
        boolean isGood = false;
        do{
            System.out.println(REGISTRATION_NAME);
            Scanner sc = new Scanner(System.in);
            username = sc.nextLine();
            if(username.length() > 2){
                isGood = true;
            }else{
                System.out.println(REGISTRATION_ERROR);
            }
        }while (isGood == false);

        return username;
    }

    private String getLocation(){
        String location = "";
        boolean isGood = false;
        do{
            System.out.println(REGISTRATION_LOCATION);
            Scanner sc = new Scanner(System.in);
            location = sc.nextLine();
            if(isAlpha(location) == true){
                isGood = true;
            }else {
                System.out.println(REGISTRATION_ERROR);
            }
        }while (isGood == false);
        return location;
    }

    private String getPhoneNumber(){
        String phone = "";
        boolean isGood = false;
        do{
            System.out.println(REGISTRATION_PHONE_NUMBER);
            Scanner sc = new Scanner(System.in);
            phone = sc.nextLine();
            if(isNumber(phone) == true){
                isGood = true;
            }else {
                System.out.println(REGISTRATION_ERROR);
            }
        }while (isGood == false);
        return phone;

    }

    private String getEmail(){
        String email = "";
        boolean isGood = false;
        do{
            System.out.println(REGISTRATION_EMAIL);
            Scanner sc = new Scanner(System.in);
            email = sc.nextLine();
            if(isEmailValid(email) == true ){
                isGood = true;
            }else {
                System.out.println(REGISTRATION_ERROR);
            }
        }while (isGood == false);
        return email;
    }

    private String getHelpType(){
        int helpType = 0;
        boolean isGood = false;
        TypeOfHelp[] typeOfHelpArray = TypeOfHelp.values();
        do {
            System.out.println(REGISTRATION_HELP_TYPE);
            for (int i = 0; i < typeOfHelpArray.length; i++) {
                System.out.println((i + 1) + " - " + typeOfHelpArray[i].getTypeOfHelp());
            }
            Scanner sc = new Scanner(System.in);
            helpType = sc.nextInt();

            if (helpType > 0 && helpType < typeOfHelpArray.length + 1) {
                isGood = true;
                helpType = helpType - 1;
            } else {
                System.out.println(REGISTRATION_ERROR);
            }
        }while (isGood == false);

        return typeOfHelpArray[helpType].getTypeOfHelp();
    }
// [a-zA-Z]+[@][a-zA-Z]+[.][a-zA-Z]+
    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean isNumber(String number){
        return number.matches("[0-9]+");
    }

    public boolean isEmailValid(String email) { return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"); }

    public void register(){
        System.out.println(REGISTRATION_HEADER);

        Volunteer newVolunteer = new Volunteer();

        newVolunteer.setName(getVolunteerName());
        newVolunteer.setLocation(getLocation());
        newVolunteer.setPhone(getPhoneNumber());
        newVolunteer.setEmail(getEmail());
        newVolunteer.setTypeOfHelp(getHelpType());

        SavingUtil.saveToFile("../registeredVolunteer", newVolunteer);


    }


}
