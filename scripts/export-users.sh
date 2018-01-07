#!/bin/bash

rm ~/data/users.json

mongoexport \
  --db servisneinfo \
  --collection users \
  --out ~/data/users.json \
  --journal
