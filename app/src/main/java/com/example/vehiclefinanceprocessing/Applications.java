package com.example.vehiclefinanceprocessing;

public class Applications {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    String Id;
    String UserId;
    String UserName;
    String DealerId;
    String DealerName;
    String VehicleId;
    String VehicleName;
    String AmountOfFinance;
    String CardNumber;
    String CardHolderName;
    String CVV;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDealerId() {
        return DealerId;
    }

    public void setDealerId(String dealerId) {
        DealerId = dealerId;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getAmountOfFinance() {
        return AmountOfFinance;
    }

    public void setAmountOfFinance(String amountOfFinance) {
        AmountOfFinance = amountOfFinance;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Applications() {
    }

    public Applications(String id,String userId, String userName, String dealerId, String dealerName, String vehicleId, String vehicleName, String amountOfFinance, String cardNumber, String cardHolderName, String CVV, String status) {
        Id=id;
        UserId = userId;
        UserName = userName;
        DealerId = dealerId;
        DealerName = dealerName;
        VehicleId = vehicleId;
        VehicleName = vehicleName;
        AmountOfFinance = amountOfFinance;
        CardNumber = cardNumber;
        CardHolderName = cardHolderName;
        this.CVV = CVV;
        Status = status;
    }

    String Status;
}
