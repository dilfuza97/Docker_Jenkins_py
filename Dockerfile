FROM centos:latest

RUN yum install http -y

COPY index.html /var/www/html/index.html

EXPOSE 8080

CMD ["usr/sbin/httpd", "D", "FOREGROUND"]
