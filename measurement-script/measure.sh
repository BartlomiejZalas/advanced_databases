#!/bin/bash

FILE=resuls.txt
BACKUP=backup10k.backup
MAIN_DIRECTORY="/home/bartek/advancedDatabases"
PG_USER=postgres
PG_DB_NAME=database1

function cleanResults {
	rm -f $FILE;
}

function restoreDB {
	echo "CLEANING AND RESTORING DATABASE";
	psql -h localhost -d $PG_DB_NAME -U $PG_USER < "$MAIN_DIRECTORY/data/beforeImport.sql";
	pg_restore -i -h localhost -p 5432 -U $PG_USER -d $PG_DB_NAME -v "$MAIN_DIRECTORY/data/$BACKUP";
}

function executeSQL {
	echo "EXECUTING QUERY: $1"
	time=$(psql -h localhost -d $PG_DB_NAME -U $PG_USER < "$MAIN_DIRECTORY/measurement-script/transactions/$1" | grep "Time:" | awk '{s+=$2} END {printf "%.0f", s}' );
	timeInSeconds=$(awk -v time=$time 'BEGIN { print (time / 1000) }');	
	echo -e "$1 $timeInSeconds seconds" >> $FILE;
}

cleanResults;

restoreDB;
executeSQL "addEvent.sql";

restoreDB;
executeSQL "addParticipant.sql";

restoreDB;
executeSQL "topProfitableEvents.sql";

restoreDB;
executeSQL "nearestPlaces.sql";

restoreDB;
executeSQL "addParticipantMostPopularEvents.sql";

restoreDB;
executeSQL "deleteSpam.sql";

restoreDB;
executeSQL "moveToReplacementLocation.sql";

echo "Time measurements ready. Check $FILE for more details."
