package com.example.musicstreamer.Utility;

public class MainUser {

    private String usernameMU;
    private String emailMU;
    private String[] genresLikedMU;

    public MainUser(String usernameMU, String emailMU, String[] genresLikedMU) {
        this.usernameMU = usernameMU;
        this.emailMU = emailMU;
        this.genresLikedMU = genresLikedMU;
    }

    public String getUsernameMU() {
        return usernameMU;
    }

    public String getEmailMU() {
        return emailMU;
    }

    public String[] getGenresLikedMU() {
        return genresLikedMU;
    }
}
