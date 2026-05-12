package com.chis.trugarden.domain.user;

public class Address {
    private final Long id;
    private final String principalStreet;
    private final String secondaryStreet;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String houseNumber;
    private final String zipCode;
    private final String sector;
    private final String city;
    private final Long userId;
    private final String sessionId;

    public Address(
            Long id,
            String principalStreet,
            String secondaryStreet,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String houseNumber,
            String zipCode,
            String sector,
            String city,
            Long userId,
            String sessionId
    ) {
        this.id = id;
        this.principalStreet = principalStreet;
        this.secondaryStreet = secondaryStreet;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.sector = sector;
        this.city = city;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public static Address of(
            Long id,
            String principalStreet,
            String secondaryStreet,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String houseNumber,
            String zipCode,
            String sector,
            String city,
            Long userId,
            String sessionId
    ) {
        return new Address(id, principalStreet, secondaryStreet, firstName, lastName, email, phoneNumber, houseNumber, zipCode, sector, city, userId, sessionId);
    }

    public static Address ofNew(
            String principalStreet,
            String secondaryStreet,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String houseNumber,
            String zipCode,
            String sector,
            String city,
            Long userId,
            String sessionId
    ) {
        return new Address(null, principalStreet, secondaryStreet, firstName, lastName, email, phoneNumber, houseNumber, zipCode, sector, city, userId, sessionId);
    }

    public Long getId() {
        return id;
    }

    public String getPrincipalStreet() {
        return principalStreet;
    }

    public String getSecondaryStreet() {
        return secondaryStreet;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getSector() {
        return sector;
    }

    public String getCity() {
        return city;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", principalStreet='" + principalStreet + '\'' +
                ", secondaryStreet='" + secondaryStreet + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", sector='" + sector + '\'' +
                ", city='" + city + '\'' +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
