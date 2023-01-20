package com.example.vehiclefinanceprocessing;

public class Users {
    private  String Id;
    private  String Name;
    private  String EmailAddress;
    private  String Password;
    private  String Role;

    public Users(String id, String name, String emailAddress, String password, String role) {
        Id = id;
        Name = name;
        EmailAddress = emailAddress;
        Password = password;
        Role = role;
    }

    public Users() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}

