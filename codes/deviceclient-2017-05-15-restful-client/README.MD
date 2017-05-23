1. This project is modified from the deviceclient project of zhiyuninfo.com, read:
		company-provided-README.md
		
2. This project depends on https://coding.net/t/zhiyuninfo/p/common, my private account has access

3. This project serves as a restful client to deviceservice

4. http://hotcloud.tech:8080/login?from=%2F, account is test:test. This resource is about Jenkins

5. http://zhiyuninfo.com/serviceportal-0.1 is a web view of the official deviceservice, account is admin:day2cloud

6. official deviceservice url is :http://zhiyuninfo.com:9091

7. to run this project, 

		mvn clean package
		
		java -jar target\deviceclient-0.1-jar-with-dependencies.jar http:\\localhost:9091\agents\settings
		
8. to curl deviceservice, use

		curl --header "Authorization: Basic ZGV2aWNlY2xpZW50OnpoaXl1bjg4ODg4OA==" localhost:9091/agents/settings
		
