package com.bloodbanksystem.ferozkhan.bloodbanksystem;

public class Donors {
    private String uuid;
    private String name;
    private String email;
    private String image;
    private String bloodGroup;
    public Donors()
    {
        this.name = null;
        this.email = null;
        this.image = null;
        this.uuid = null;
        this.bloodGroup = null;
    }
    public Donors(String uuid)
    {
        this.uuid = uuid;
        this.name = null;
        this.email = null;
        this.image = null;
    }
    public Donors(String uuid, String name)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = null;
        this.email = null;
        this.image = null;
    }
    public Donors(String uuid, String name,String bloodGroup)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = null;
        this.image = null;
    }
    public Donors(String uuid, String name, String bloodGroup, String email)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.image = null;
    }
    public Donors(String uuid, String name, String bloodGroup, String email, String image)
    {
        this.uuid = uuid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.image = image;
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
}
