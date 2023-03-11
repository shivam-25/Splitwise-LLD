package com.shivam.splitwise.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shivam.splitwise.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    Map<String, User> userStorage;

    public UserDAO(Map<String, User> userStorage) {
        this.userStorage = userStorage;
    }

    public Map<String, User> getUserStorage() {
        return userStorage;
    }

    public void setUserStorage(Map<String, User> userStorage) {
        this.userStorage = userStorage;
    }

    public UserDAO() {
        this.userStorage = new HashMap<>();
    }

    public void addUser(User user) {
        userStorage.put(user.getUserId(), user);
    }

    public User getUser(String id) {
        return userStorage.get(id);
    }

    public Map<String, User> get() {
        return userStorage;
    }
}
