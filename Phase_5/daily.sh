#!/bin/bash
rm ""Phase_5/Transaction_Sessions/""$1""/*""
num_of_files=$(ls Phase_5/Transaction_Sessions/*.txt | wc -l)
for FILE_NUM in $(seq "$num_of_files"); do
    touch ""Phase_5/Transaction_Sessions/""$1""/daily_transaction_""$FILE_NUM.txt""
done
touch ""Phase_5/Transaction_Sessions/""$1""/merged_daily_transaction.txt""
javac Phase_5/*.java
n=0
for SESSION in Phase_5/Transaction_Sessions/*.txt; do
    ((n++))
    java Phase_5.App Phase_5/Files/current_users.txt Phase_5/Files/available_tickets.txt ""Phase_5/Transaction_Sessions/""$1""/daily_transaction_""$n.txt"" $SESSION > /dev/null
    cat ""Phase_5/Transaction_Sessions/""$1""/daily_transaction_""$n.txt"" >> ""Phase_5/Transaction_Sessions/""$1""/merged_daily_transaction.txt""
    if (($n != $num_of_files)); then
        echo $'\r' >> ""Phase_5/Transaction_Sessions/""$1""/merged_daily_transaction.txt""
    fi
done
java Phase_5.Stub Phase_5/Transaction_Sessions/Day_1_Sessions/merged_daily_transaction.txt      # Back End Stub (May Not Work)
rm Phase_5/*.class