#!/bin/bash

FILE=resuls.txt
BACKUP=backup10k.backup
MAIN_DIRECTORY="/home/bartek/advancedDatabases"

function cleanResults {
	rm -f $FILE;
}

function restoreDB {
	echo "CLEANING AND RESTORING DATABAS";
	psql -h localhost -d database1 -U postgres < "$MAIN_DIRECTORY/data/beforeImport.sql";
	pg_restore -i -h localhost -p 5432 -U postgres -d database1 -v "$MAIN_DIRECTORY/data/$BACKUP";
}

function executeSQL {
	echo "EXECUTING QUERY: $1"
	time=$(psql -h localhost -d database1 -U postgres < "$MAIN_DIRECTORY/measurement-script/transactions/$1" | grep "Time:" | tail -1);
	echo -e "$1 $time" >> $FILE;
}

cleanResults;

restoreDB;
executeSQL "addEvent.sql";

restoreDB;
executeSQL "addParticipant.sql";

restoreDB;
executeSQL "addParticipantMostPopularEvents.sql";

restoreDB;
executeSQL "deleteSpam.sql";

restoreDB;
executeSQL "moveToReplacementLocation.sql";

restoreDB;
executeSQL "nearestPlaces.sql";

restoreDB;
executeSQL "topProfitableEvents.sql";

echo "Time measurements ready. Check $FILE for more details."
