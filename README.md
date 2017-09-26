servisne.info
=============

servisne.info is a web app for delivering service informations on your email.

Visit http://servisne.info to try it out.

servisne.info is proudly tested on [Semaphore](https://semaphoreapp.com/strika/servisne-info).

[![Build
Status](https://semaphoreapp.com/api/v1/projects/f451e93c-2dfe-4f5e-bc2f-1b481f9cab9c/196908/badge.png)](https://semaphoreapp.com)

### Development

Export following environment variables:

```bash
export ADMIN_EMAIL=admin@example.com
export ADMIN_TOKEN=token123
export EMAIL_HOST=smtp.mandrillapp.com
export EMAIL_USER=user@example.com
export EMAIL_PASS=myapikey
export OPENSHIFT_MONGODB_HOST=url # default "localhost"
export OPENSHIFT_MONGODB_PORT=1234 # default "27017"
export OPENSHIFT_MONGODB_USERNAME=admin
export OPENSHIFT_MONGODB_PASSWORD=mongopassword
export OPENSHIFT_APP_NAME=admin # used as database name (default "servisne")
export S3_ACCESS_KEY=key
export S3_ACCESS_SECRET=secret
export OPENSHIFT_DATA_DIR=/path
export SENTRY_DSN=url
```

### Deploy to OpenShift

Create an account at Red Hat's [OpenShift](https://openshift.redhat.com). Install the CLI client.

Execute:
```bash
rhc app create servisne http://cartreflect-claytondev.rhcloud.com/github/openshift-cartridges/clojure-cartridge --from-code https://github.com/strika/servisne.info.git
```
