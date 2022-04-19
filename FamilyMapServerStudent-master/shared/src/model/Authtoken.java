package model;

import java.util.Objects;

/**
 * class that holds data for the authorization token
 */
public class Authtoken {
    /**
     * the actual authorization token
     */
    private String authtoken;
    /**
     * username associated with the authToken
     */
    private String username;

    /**
     * Authtoken constructor
     * @param authtoken the actual authToken
     * @param username username associated with the authToken
     */
    public Authtoken(String authtoken,String username){
        this.authtoken = authtoken;
        this.username = username;
    }

    /**
     * get authToken
     * @return authToken
     */
    public String getAuthToken() {
        return authtoken;
    }

    /**
     * set authToken
     * @param authtoken
     */
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authtoken authToken = (Authtoken) o;
        return Objects.equals(authtoken,authToken.authtoken) && Objects.equals(username, authToken.username);
    }
}
