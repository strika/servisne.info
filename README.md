servisne.info
=============

servisne.info is a web app for delivering service informations on your email.

Visit http://servisne.info to try it out.

[![Build
Status](https://semaphoreapp.com/api/v1/projects/f451e93c-2dfe-4f5e-bc2f-1b481f9cab9c/196908/badge.png)](https://semaphoreapp.com)

### Deploy to OpenShift

Create an account at Red Hat's [OpenShift](https://openshift.redhat.com). Install the CLI client.

Execute:
```bash
rhc app create servisne http://cartreflect-claytondev.rhcloud.com/github/openshift-cartridges/clojure-cartridge --from-code https://github.com/strika/servisne.info.git
```
