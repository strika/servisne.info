servisne.info
=============

servisne.info is a web app for delivering service informations on your email.

Visit http://servisne.info to try it out.

### Deploy to OpenShift

Create an account at Red Hat's [OpenShift](https://openshift.redhat.com). Install the CLI client.

Execute:
```bash
rhc app create servisne http://cartreflect-claytondev.rhcloud.com/github/openshift-cartridges/clojure-cartridge --from-code https://github.com/strika/servisne.info.git
```
