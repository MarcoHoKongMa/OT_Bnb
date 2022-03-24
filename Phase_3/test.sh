#!/bin/bash

javac Phase_3/*.java
# java Phase_3.App ./Phase_2/Files/current_users.txt ./Phase_2/Files/available_tickets.txt ./Phase_2/Files/daily_transaction.txt Phase_3/inputs/Create\ Transaction/Test\ Case1/input.txt Phase_3/output.txt
echo "Running Scripts"
for i in 1 2 3 4 5; do
    echo $i
    java Phase_3.App ./Phase_2/Files/current_users.txt ./Phase_2/Files/available_tickets.txt ./Phase_2/Files/daily_transaction.txt Phase_3/inputs/Create_Transaction/Test_Case$i/input.txt Phase_3/inputs/Create_Transaction/Test_Case$i/output$i.txt
done