FROM centos
RUN yum install sudo -y
RUN yum install httpd -y
RUN mkdir /root/html
COPY *.html /root/html/
COPY ./web.html  /root/html/
CMD /usr/sbin/httpd -DFOREGROUND
