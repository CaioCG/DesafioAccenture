package br.com.accenture.qa.context;

import java.util.List;

public class ScenarioState {
    private String userId;
    private String username;
    private String password;
    private String token;
    private List<String> selectedIsbns = List.of();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getSelectedIsbns() {
        return selectedIsbns;
    }

    public void setSelectedIsbns(List<String> selectedIsbns) {
        this.selectedIsbns = List.copyOf(selectedIsbns);
    }
}
