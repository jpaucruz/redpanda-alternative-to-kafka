#!/bin/bash

MOVEMENTS=$1
RANDOM_FRAUD=$(shuf -i 3-6 -n 1)
MIN_FRAUD_EDGE=$(($MOVEMENTS / 3))
MAX_FRAUD_EDGE=$(($MIN_FRAUD_EDGE * 2))

fraud() {
	for i in $(seq 1 $RANDOM_FRAUD);
	do
        	curl --location --request POST 'http://localhost:8090/movement' --header 'Content-Type: application/json' --data-raw '{"id": 9999'$1$i', "cardId": "900000'$1'"}'
        	sleep 2
	done
}

for i in $(seq 1 $MOVEMENTS);
do
	if [ $i -eq $MIN_FRAUD_EDGE ] || [ $i -eq $MAX_FRAUD_EDGE ]
	then
		fraud $i
	else
		curl --location --request POST 'http://localhost:8090/movement' --header 'Content-Type: application/json' --data-raw '{"id": '$i', "cardId": "100000'$i'"}'
		sleep 1
	fi
done
