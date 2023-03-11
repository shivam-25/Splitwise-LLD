package com.shivam.splitwise.service;

import com.shivam.splitwise.dao.UserDAO;
import com.shivam.splitwise.enums.SplitType;
import com.shivam.splitwise.models.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SplitExpense implements ISplitExpense {


    @Override
    public void show(UserDAO userDAO) {
        Set<String> printValues = new HashSet<>();
        for(Map.Entry<String, User> userSet: userDAO.get().entrySet()) {
            display(userDAO, userSet, printValues);
        }
        if(printValues.size() == 0) {
            System.out.println("No Balances");
            return;
        }
        printValues.forEach(v -> System.out.println(v));
    }

    @Override
    public void show(UserDAO userDAO, String userId) {
        User user = userDAO.getUser(userId);
        Set<String> printValues = new HashSet<>();
        display(userDAO, new AbstractMap.SimpleEntry<>(userId, user), printValues);
        if(printValues.size() == 0) {
            System.out.println("No Balances");
            return;
        }
        printValues.forEach(v -> System.out.println(v));
    }

    @Override
    public void split(UserDAO userDAO, String userPaid, double amountPaid, int userCount, List<String> users, SplitType type, List<Double> splitValues) {
        User userMadeExpenses = userDAO.getUser(userPaid);
        List<User> usersInvolvedInSplit = getAllInvolvedUsers(userDAO, users);
        switch (type) {
            case EQUAL:
                distributeEqually(userMadeExpenses, userCount, usersInvolvedInSplit, amountPaid);
                break;

            case EXACT:
                distributeExact(userMadeExpenses, amountPaid, userCount, usersInvolvedInSplit, splitValues);
                break;

            case PERCENT:
                distributePercent(userMadeExpenses, amountPaid, userCount, usersInvolvedInSplit, splitValues);
                break;

            default:
                System.out.println("Distributing equally by default: ");
                distributeEqually(userMadeExpenses, userCount, usersInvolvedInSplit, amountPaid);
                break;

        }
    }

    private void distributePercent(User userMadeExpenses, double amountPaid, int userCount, List<User> usersInvolvedInSplit, List<Double> splitValues) {
        List<Double> amountDistribution = splitValues.stream().map(p -> p*amountPaid/100).collect(Collectors.toList());
        distributeExact(userMadeExpenses, amountPaid, userCount, usersInvolvedInSplit, amountDistribution);
    }

    private void distributeExact(User userMadeExpenses, double amountPaid, int userCount, List<User> usersInvolvedInSplit, List<Double> splitValues) {
        double doubleTotalAmount = splitValues.stream().reduce((x,y) -> x+y).get();
        if(doubleTotalAmount == amountPaid) {
            for (int i = 0; i < userCount; i++) {
                User user = usersInvolvedInSplit.get(i);
                Double amountBorrowed = splitValues.get(i);
                if (!userMadeExpenses.getUserId().equals(user.getUserId())) {
                    userMadeExpenses.updateAmountLent(user.getUserId(), amountBorrowed);
                    user.updateAmountBorrowed(userMadeExpenses.getUserId(), amountBorrowed);
                }
            }
        } else {
            System.out.println("Amount cannot be split");
        }
    }

    private List<User> getAllInvolvedUsers(UserDAO userDAO, List<String> users) {
        List<User> usersInvolved = new ArrayList<>();
        users.forEach( user -> usersInvolved.add(userDAO.getUser(user)));
        return usersInvolved;
    }

    private void distributeEqually(User userMadeExpenses, int userCount, List<User> usersInvolvedInSplit, Double amountPaid) {
        double equalDistributionAmount = amountPaid/userCount;

        usersInvolvedInSplit.forEach(user -> {
            if(!user.getUserId().equals(userMadeExpenses.getUserId())) {
                userMadeExpenses.updateAmountLent(user.getUserId(), equalDistributionAmount);
                user.updateAmountBorrowed(userMadeExpenses.getUserId(), equalDistributionAmount);
            }
        });
    }

    private void display(UserDAO userDAO, Map.Entry<String, User> userSet, Set<String> printValues) {
        String userId = userSet.getKey();
        User user = userSet.getValue();
        Map<String, Double> lentDetails = user.getAmountLent();
        Map<String, Double> borrowedDetails = user.getAmountBorrowed();
        NumberFormat formatter = new DecimalFormat("#0.00");
        for(Map.Entry<String, Double> lent: lentDetails.entrySet()) {
            String userHere = lent.getKey();
            double amountLent = lent.getValue();
            double amountBorrowed = 0.0;
            if(borrowedDetails.containsKey(userHere)) {
                amountBorrowed = borrowedDetails.get(userHere);
            }
            if(amountLent>amountBorrowed) {
                printValues.add(userDAO.getUser(userHere).getName()+" owes "+ userDAO.getUser(userId).getName() + " : " + formatter.format(amountLent-amountBorrowed));
            } else if(amountLent<amountBorrowed) {
                printValues.add(userDAO.getUser(userId).getName()+" owes "+ userDAO.getUser(userHere).getName() + " : " + formatter.format(amountBorrowed-amountLent));
            }
        }
        for(Map.Entry<String, Double> borrowed: borrowedDetails.entrySet()) {
            String userHere = borrowed.getKey();
            double borrowedAmount = borrowed.getValue();
            double lent = 0.0;
            if(lentDetails.containsKey(userHere)) {
                lent = lentDetails.get(userHere);
            }
            if(borrowedAmount>lent) {
                printValues.add(userDAO.getUser(userId).getName()+" owes "+ userDAO.getUser(userHere).getName() + " : " + formatter.format(borrowedAmount-lent));
            } else if(borrowedAmount<lent) {
                printValues.add(userDAO.getUser(userHere).getName()+" owes "+ userDAO.getUser(userId).getName() + " : " + formatter.format(lent-borrowedAmount));
            }
        }
    }
}
