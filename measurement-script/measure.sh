#!/bin/bash

CREATE_INDICES=true
INDICES_FILE_NAME=add-indices-hash-gist.sql
FILE=results.txt
BACKUP=backup10k.backup
#MAIN_DIRECTORY="/home/bartek/advancedDatabases"
MAIN_DIRECTORY="/Users/paweliwanow/nauka/Advanced Databases/advanced_databases"
PG_USER=postgres
#PG_DB_NAME=database1
PG_DB_NAME=advanced

function cleanResults {
	rm -f $FILE;
}

function restoreDB {
	echo "CLEANING AND RESTORING DATABASE";
	psql -h localhost -d $PG_DB_NAME -U $PG_USER < "$MAIN_DIRECTORY/data/beforeImport.sql";
	pg_restore -i -h localhost -p 5432 -U $PG_USER -d $PG_DB_NAME -v "$MAIN_DIRECTORY/data/$BACKUP";
	if [ "$CREATE_INDICES" = true ] ; then
		psql -h localhost -d $PG_DB_NAME -U $PG_USER < "$MAIN_DIRECTORY/$INDICES_FILE_NAME";
	fi
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
