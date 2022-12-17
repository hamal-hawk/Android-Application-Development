package com.example.civiladvocacy;

import java.io.Serializable;

public class Official implements Serializable {
    String name;
    String office;
    String party;
    String facebook;
    String twitter;
    String youtube;
    String address;
    String phone;
    String email;
    String website;
    String photo_url;

    public Official(String name, String office, String party, String facebook, String twitter, String youtube, String address, String phone, String email, String website, String photo_url) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.photo_url = photo_url;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", office='" + office + '\'' +
                ", party='" + party + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", youtube='" + youtube + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public String getParty() {
        return party;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }
}
