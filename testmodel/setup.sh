#!/usr/bin/bash
# Build and deploy the testmodel webapp
# This script requires the standard InterMine dependencies:
#  * psql (createdb, psql) - your user should have a postgres
#    role with password authentication set up.
#  * ant
#  * a deployment container (tomcat).

set -e # Errors are fatal.

USERPROFILEDB=userprofile-demo
PRODDB=objectstore-demo
MINENAME=demomine
DIR="$(cd $(dirname "$0"); pwd)"
IMDIR=$HOME/.intermine
LOG=$DIR/build.log
PROP_FILE=$IMDIR/testmodel.properties

# Inherit SERVER, PORT, PSQL_USER, PSQL_PWD, TOMCAT_USER and TOMCAT_PWD if in env.
if test -z $SERVER; then
    SERVER=localhost
fi
if test -z $PORT; then
    PORT=8080
fi
if test -z $PSQL_USER; then
    PSQL_USER=$USER
fi
if test -z $PSQL_PWD; then
    PSQL_PWD=$USER;
fi
if test -z $TOMCAT_USER; then
    TOMCAT_USER=manager
fi
if test -z $TOMCAT_PWD; then
    TOMCAT_PWD=manager
fi
if test -z $REL; then
    REL=demo
fi

ANT="ant -v -Drelease=$REL"
if test $REL; then
    PROP_FILE=$PROP_FILE.$REL
fi

if $DEBUG; then
    echo "Setting up testmodel application with the following properties:"
    echo " USERPROFILEDB = $USERPROFILEDB"
    echo " PRODDB        = $PRODDB"
    echo " MINENAME      = $MINENAME"
    echo " MINEDIR       = $DIR"
    echo " IMDIR         = $IMDIR"
    echo " LOG           = $LOG"
    echo " PROP_FILE     = $PROP_FILE"
    echo " REL           = $REL"
    echo " ANT           = $ANT"
    echo " SERVER        = $SERVER"
    echo " PORT          = $PORT"
    echo " PSQL_USER     = $PSQL_USER"
    echo " PSQL_PWD      = $PSQL_PWD"
    echo " TOMCAT_USER   = $TOMCAT_USER"
    echo " TOMCAT_PWD    = $TOMCAT_PWD"
fi

cd $HOME

if test ! -d $IMDIR; then
    if $DEBUG; then echo Making .intermine configuration directory.; fi
    mkdir $IMDIR
fi

if test ! -f $PROP_FILE; then
    if $DEBUG; then echo $PROP_FILE not found. Providing default properties file.; fi
    cd $IMDIR
    cp $DIR/testmodel.properties $PROP_FILE
    sed -i "s/PSQL_USER/$PSQL_USER/g" $PROP_FILE
    sed -i "s/PSQL_PWD/$PSQL_PWD/g" $PROP_FILE
    sed -i "s/TOMCAT_USER/$TOMCAT_USER/g" $PROP_FILE
    sed -i "s/TOMCAT_PWD/$TOMCAT_PWD/g" $PROP_FILE
    sed -i "s/USERPROFILEDB/$USERPROFILEDB/g" $PROP_FILE
    sed -i "s/PRODDB/$PRODDB/g" $PROP_FILE
    sed -i "s/SERVER/$SERVER/g" $PROP_FILE
    sed -i "s/8080/$PORT/g" $PROP_FILE
    sed -i "s/USER/$USER/g" $PROP_FILE
fi

if $DEBUG; then echo Checking databases.; fi
for db in $USERPROFILEDB $PRODDB; do
    if psql --list | egrep -q '\s'$db'\s'; then
        echo $db exists.
    else
        echo Creating $db
        createdb $db
    fi
done

if $DEBUG; then echo Beginning build - logging to $LOG; fi

echo Processing data sources.
cd $DIR/dbmodel/extra/books
make 

cd $DIR/dbmodel
echo "Loading test data set - this takes about 3-4 minutes."
$ANT clean load-workers-and-books >> $LOG

cd $DIR/webapp/main

echo "Building and releasing web-application - about 1min left."
$ANT -Ddont.minify=true \
    build-test-userprofile-withuser \
    create-quicksearch-index \
    default \
    remove-webapp \
    release-webapp | tee $LOG | grep tomcat-deploy

echo build complete. Log available in $LOG
