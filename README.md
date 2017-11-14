# servisne.info

servisne.info is a web app for delivering service informations on your email.

Visit http://servisne.info to try it out.

## Environment Variables

Create directory:

```bash
mkdir ~/data
```

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
sudo apt-get install git openjdk-8-jre-headless nginx mongodb-server
```

Install Leiningen:

```bash
sudo curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -o /usr/local/bin/lein
sudo chmod a+x /usr/local/bin/lein
```

Clone the project on the server:

```bash
git clone https://github.com/strika/servisne.info.git
```

You should be able to run the application with:

```bash
lein with-profile production trampoline ring server
```

Example Nginx configuration:

```bash
upstream http_backend {
  server 127.0.0.1:8080;
  keepalive 32;
}
server {
  listen 80 default_server;
  listen [::]:80 default_server;

  root /var/www/html;

  index index.html index.htm index.nginx-debian.html;

  server_name .servisne.info;

  location / {
    proxy_pass http://http_backend;

    proxy_http_version 1.1;
    proxy_set_header Connection "";

    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;

    access_log /home/servisneinfo/servisne.info/log/access.log;
    error_log /home/servisneinfo/servisne.info/log/error.log;
  }
}
```

Restore data with:

```bash
mongoimport --db servisneinfo --collection users users-2017-10-09.json
```
