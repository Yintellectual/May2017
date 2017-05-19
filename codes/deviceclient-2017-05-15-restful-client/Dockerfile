FROM java:latest
MAINTAINER zhulin zl@zhiyuninfo.com
ADD ./target/deviceclient-0.1-client.jar /deviceclient/deviceclient-0.1-client.jar
WORKDIR /deviceclient
CMD java -jar deviceclient-0.1-client.jar http://www.i-qyy.com:8080/devicemanager-1.0/
