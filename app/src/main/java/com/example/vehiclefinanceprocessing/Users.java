package com.example.vehiclefinanceprocessing;

public class Users {
    private  String Id;
    private  String Name;
    private  String EmailAddress;
    private  String Password;
    private  String Role;
    private  String Status;
    private String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Users(String id, String name, String emailAddress, String password, String role, String status,String image) {
        Id = id;
        Name = name;
        EmailAddress = emailAddress;
        Password = password;
        Role = role;
        Status = status;
        Image = image;

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

