#!/bin/bash

rm $OPENSHIFT_DATA_DIR/users.json

mongoexport \
  --host $OPENSHIFT_MONGODB_DB_HOST:$OPENSHIFT_MONGODB_DB_PORT \
  --username $OPENSHIFT_MONGODB_DB_USERNAME \
  --password $OPENSHIFT_MONGODB_DB_PASSWORD \
  --db servisneinfo \
  --collection users \
  --out $OPENSHIFT_DATA_DIR/users.json \
  --journal
