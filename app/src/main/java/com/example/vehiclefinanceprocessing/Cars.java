package com.example.vehiclefinanceprocessing;



public class Cars {
    private  String Id;
    private  String Name;
    private String Image;
    private  String Milage;
    private  String Price;
    private  String FuelType;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Cars(String id, String name, String image, String milage, String fuelType, String description, String status, String price) {
        Id = id;
        Name = name;
        Image = image;
        Milage = milage;
        FuelType = fuelType;
        Description = description;
        Status = status;
        Price = price;
    }

    public Cars() {
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMilage() {
        return Milage;
    }

    public void setMilage(String milage) {
        Milage = milage;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private  String Description;
    private  String Status;
}

