#Device Client
The client which can recognize the device and send data to device service


###How to run this application as Device Client:
```
1. mvn clean assembly:assembly
2. java -jar target/deviceclient-0.1-client.jar -host http://localhost:9091
3. Check if the agent is registered in http://localhost:9091/agents
```

###How to run this application as Weather Client:
```
1. mvn clean assembly:assembly
2. java -jar target/deviceclient-0.1-client.jar -host http://localhost:9091 -type weather
3. Check if the agent is registered in http://localhost:9091/agents
```

### How to deploy this application into aliyun
```
Operations on local machine
1. scp target/deviceclient-0.1-client.jar root@www.zhiyuninfo.com:/home/deviceclient

Operations in aliyun server

For Device Client
1. docker run --name deviceclient -d -v /home/deviceclient:/deviceclient java sh -c 'java -jar /deviceclient/deviceclient-0.1-client.jar -random true'
2. docker exec -ti deviceclient bash
3. cp deviceclient/fgn-flamer-data.txt .
4. exit
5. docker restart deviceclient

For Weather Client
1. cp /home/deviceclient/*.jar /home/weatherclient/
2. docker run --name weatherclient -d -v /home/weatherclient:/deviceclient java sh -c 'java -jar /deviceclient/deviceclient-0.1-client.jar -type weather'
```


###Notes
