package com.bloodbanksystem.ferozkhan.bloodbanksystem;

public class Donors {
    private String uuid;
    private String name;
    private String email;
    private String image;
    private String bloodGroup;
    private String locLat,locLong;
    public Donors()
    {
        this.name = null;
        this.email = null;
        this.image = null;
        this.uuid = null;
        this.bloodGroup = null;
        this.locLat = null;
        this.locLong = null;
    }
    public Donors(String uuid)
    {
        this.uuid = uuid;
        this.name = null;
        this.email = null;
        this.image = null;
        this.locLat = null;
        this.locLong = null;
    }
    public Donors(String uuid, String name)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = null;
        this.email = null;
        this.image = null;
        this.locLat = null;
        this.locLong = null;
    }
    public Donors(String uuid, String name,String bloodGroup)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = null;
        this.image = null;
        this.locLat = null;
        this.locLong = null;
    }
    public Donors(String uuid, String name, String bloodGroup, String email)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.image = null;
        this.locLat = null;
        this.locLong = null;
    }
    public Donors(String uuid, String name, String bloodGroup, String email, String image)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.image = image;
        this.locLat = null;
        this.locLong = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocLat(String locLat) {
        this.locLat = locLat;
    }

    public void setLocLong(String locLong) {
        this.locLong = locLong;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getImage() {
        return image;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public String getLocLat() {
        return locLat;
    }

    public String getLocLong() {
        return locLong;
    }
}
