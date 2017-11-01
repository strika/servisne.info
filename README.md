# servisne.info

servisne.info is a web app for delivering service informations on your email.

Visit http://servisne.info to try it out.

## Environment Variables

Export following environment variables:

```bash
export ADMIN_EMAIL=admin@example.com
export ADMIN_TOKEN=token123
export APP_ENVIRONMENT=production
export DATA_DIR=/path
export EMAIL_HOST=smtp.mandrillapp.com
export EMAIL_PASS=myapikey
export EMAIL_USER=user@example.com
export MONGODB_URI=uri
export S3_ACCESS_KEY=key
export S3_ACCESS_SECRET=secret
export SENTRY_DSN=url
```

## Production Setup

Create user on the server:

```bash
adduser servisneinfo
```

Add the user to sudoers:

```bash
usermod -aG sudo servisneinfo
```

Update the system:

```bash
sudo apt-get update && sudo apt-get -y upgrade
```

Install dependencies:

```bash
sudo apt-get install git
```
