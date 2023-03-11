package com.shivam.splitwise.service;

import com.shivam.splitwise.dao.UserDAO;
import com.shivam.splitwise.enums.SplitType;
import com.shivam.splitwise.models.User;

import java.util.List;
import java.util.Map;

public interface ISplitExpense {
    public void show(UserDAO userDAO);
    public void show(UserDAO userDAO, String userId);
    public void split(UserDAO userDAO, String userPaid, double amountPaid, int userCount, List<String> users, SplitType type, List<Double> splitValues);
}
