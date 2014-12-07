#!/bin/bash

mongoexport --host $OPENSHIFT_MONGODB_DB_HOST:$OPENSHIFT_MONGODB_DB_PORT \
  --username $OPENSHIFT_MONGODB_DB_USERNAME \
  --password $OPENSHIFT_MONGODB_DB_PASSWORD \
  --db servisneinfo \
  --collection users \
  --out users.json \
  --journal
