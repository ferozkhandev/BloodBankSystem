package com.bloodbanksystem.ferozkhan.bloodbanksystem;

public class Donors {
    private String uuid;
    private String name;
    private String email;
    private String image;
    public Donors()
    {
        this.name = null;
        this.email = null;
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
        this.email = null;
        this.image = null;
    }
    public Donors(String uuid, String name, String email)
    {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.image = null;
    }
    public Donors(String uuid, String name,String email, String image)
    {
        this.uuid = uuid;
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
