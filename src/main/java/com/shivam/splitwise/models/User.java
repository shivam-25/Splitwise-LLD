package com.shivam.splitwise.models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String name;
    private String email;
    private String mobileNumber;

    private Map<String, Double> amountBorrowed;

    private Map<String, Double> amountLent;

    public User() {}

    public User(String userId, String name, String email, String mobileNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.amountBorrowed = new HashMap<>();
        this.amountLent = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountBorrowed(Map<String, Double> amountBorrowed) {
        this.amountBorrowed = amountBorrowed;
    }

    public void setAmountLent(Map<String, Double> amountLent) {
        this.amountLent = amountLent;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Map<String, Double> getAmountBorrowed() {
        return amountBorrowed;
    }

    public Map<String, Double> getAmountLent() {
        return amountLent;
    }

    public void updateAmountBorrowed(String userId, Double amount) {
        if(this.amountBorrowed.containsKey(userId)) {
            amountBorrowed.put(userId, amount+amountBorrowed.get(userId));
            amountBorrowed = checkAndRemoveMapKey(amountBorrowed, userId);
        } else {
            amountBorrowed.put(userId, amount);
        }
    }

    public void updateAmountLent(String userId, Double amount) {
        if(this.amountLent.containsKey(userId)) {
            amountLent.put(userId, amount+amountLent.get(userId));
            amountLent = checkAndRemoveMapKey(amountLent, userId);
        } else {
            amountLent.put(userId, amount);
        }
    }

    private Map<String, Double> checkAndRemoveMapKey(Map<String, Double> amountMap, String key) {
        if(amountMap.get(key) == 0.00) {
            amountMap.remove(key);
        }
        return amountMap;
    }

}
