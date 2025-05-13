package io.github.alberes.register.manager.frontend.controllers.dto;

public class AddressDto{
    private String userId;
    private String id;
    private String publicArea;
    private Integer number;
    private String additionalAddress;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private boolean isNewRegister;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicArea() {
        return publicArea;
    }

    public void setPublicArea(String publicArea) {
        this.publicArea = publicArea;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(String additionalAddress) {
        this.additionalAddress = additionalAddress;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isNewRegister() {
        return isNewRegister;
    }

    public void setNewRegister(boolean newRegister) {
        isNewRegister = newRegister;
    }
}
