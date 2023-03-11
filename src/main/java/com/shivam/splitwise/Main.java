package com.shivam.splitwise;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.shivam.splitwise.dao.UserDAO;
import com.shivam.splitwise.enums.SplitType;
import com.shivam.splitwise.models.User;
import com.shivam.splitwise.service.SplitExpense;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String ar[]) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        ObjectMapper mapper = new ObjectMapper();

        UserDAO userDAO = mapper.readValue(new File("spliwiseUsers.json"), UserDAO.class);
        SplitExpense splitExpense = new SplitExpense();
        while(!input.trim().equals("exit")) {
            if(input.trim().equalsIgnoreCase("SHOW")) {
                splitExpense.show(userDAO);
            } else if(input.trim().contains("SHOW")) {
                String userShow = input.split(" ")[1];
                splitExpense.show(userDAO, userShow);
            } else if(input.trim().contains("EXPENSE")) {
                String[] expenseDetails = input.split(" ");
                String userPaid = expenseDetails[1];
                double amount = Double.parseDouble(expenseDetails[2]);
                int userCount = Integer.parseInt(expenseDetails[3]);
                List<String> usersInvolved = new ArrayList<>();
                for(int i=4;i<4+userCount;i++) {
                    usersInvolved.add(expenseDetails[i].trim());
                }
                SplitType type = SplitType.valueOf(expenseDetails[4+userCount]);
                List<Double> splitValues = new ArrayList<>();
                if(type.toString().equals(SplitType.EQUAL.toString())) {
                    splitValues = null;
                } else if(type.toString().equals(SplitType.EXACT.toString()) || type.toString().equals(SplitType.PERCENT.toString())) {
                    for (int i = 4 + userCount + 1; i < 4 + userCount + 1 + userCount; i++) {
                        splitValues.add(Double.parseDouble(expenseDetails[i].trim()));
                    }
                }

                splitExpense.split(userDAO, userPaid, amount, userCount, usersInvolved, type, splitValues);
            }
            input = reader.readLine();
        }

        mapper.writeValue(new File("spliwiseUsers.json"), userDAO);

    }
}
