package com.bloodbanksystem.ferozkhan.bloodbanksystem;

public class Donors {
    private String name;
    private String email;
    public Donors()
    {
        this.name = null;
        this.email = null;
    }
    public Donors(String name)
    {
        this.name = name;
        this.email = null;
    }
    public Donors(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
