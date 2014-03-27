set -e

DIR="$(cd $(dirname "$0"); pwd)"
MINENAME=biotestmine
PROD_DB=$MINENAME
ITEMS_DB=$MINENAME-items
USERPROFILE_DB=$MINENAME-userprofile
IMDIR=$HOME/.intermine
PROP_FILE=${MINENAME}.properties

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

if test ! -d $IMDIR; then
    echo Making .intermine configuration directory.
    mkdir $IMDIR
fi

if test ! -f $IMDIR/$PROP_FILE; then
    echo $PROP_FILE not found. Providing default properties file...
    cd $IMDIR
    cp $DIR/../bio/tutorial/malariamine.properties $PROP_FILE
    sed -i "s/PSQL_USER/$PSQL_USER/g" $PROP_FILE
    sed -i "s/PSQL_PWD/$PSQL_PWD/g" $PROP_FILE
    sed -i "s/items-malariamine/$ITEMS_DB/g" $PROP_FILE
    sed -i "s/userprofile-malariamine/$USERPROFILE_DB/g" $PROP_FILE
    sed -i "s/databaseName=malariamine/databaseName=$PROD_DB/g" $PROP_FILE
    sed -i "s/malariamine/$MINENAME/gi" $PROP_FILE
    sed -i "s/localhost/$SERVER/g" $PROP_FILE
    sed -i "s/8080/$PORT/g" $PROP_FILE
    echo Created $PROP_FILE
fi

echo Checking databases...
for $db in $USERPROFILE_DB $PROD_DB $ITEMS_DB; do
    if psql --list | egrep -q $db; then
        echo $db exists.
    else
        echo Creating $db ...
        createdb $db
    fi
done

echo $DIR
echo Personalising project.xml
sed -i "s/DATA_DIR/$HOME/g" project.xml

