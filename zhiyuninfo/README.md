1. Server should be at least T2.large

2. config application.properties in serviceportal

	set api.gateway=http://www.hotcloud.tech:8090

3. config in browser:

	localhost
	
	login as demo:123456
	
	系统配置
	
	set 第二个url = http://172.31.32.11/9091
	
////////////

confirmed a bug in deviceservice\src\main\java\com\zhiyuninfo\deviceservice\impl\DeviceServiceImpl.java

line 237

		if(newDevice.getSerialNo()!=null && "".equals(newDevice.getSerialNo().trim())){
			//确保不能写入空字符串，否则很容易违背唯一性约束
			device.setSerialNo(null);
		}else{
			device.setSerialNo(newDevice.getSerialNo());
		}
		
//////////

Merge:

	1. in deviceservice, 
			in DeviceServiceImpl
				1.1 change line 
					device.setProjectId(newDevice.getProjectId());
				to 
					if(newDevice.getProjectId!=null){
						device.setProjectId(newDevice.getProjectId());
					}

				1.2 change line 237, comfirmed bug
				
	2. in deviceclient
			2.1 add NullValueDeviceUtility.java
			2.2 in ComAgent.java
				change method prepareSingleDeviceSendData()
				to use nullValueDeviceUtility
				
/////////////
attributes				null value in update()			


//id					//not Null
name:					null
//type: 				//irrelavent
touchTime:				-1
//updateTime:				//irrelevant
touchBy:				null
companyId:				null
//projectId: 				//strictly updatable
longitude:				-1.0f
latitude:				-1.0f
comId:					null
tcpDeviceIP:				null
customizedAttrValues: 			null
//linkAttrValues			//irrelevant
supportGroupId:				null
supportEngineerId:			null
ownerId:				null
//serialNo: 				//(bug)irrelavent, always set to null
------------inherited from BaseDto-------
tenantId:				null
//uuid					//irrelevant
companyId				null
//lastModifyuser			//irrelevant
//lastModifyTime			//irrelevant
				
/////////////
1. in common, 
	in Device,
	  add a method that constructs null value instance
	  
2. in deviceservice,
	in DeviceServiceImpl
		edit update that use the nullValueInstance instead of its null value directly
		
3. in deviceclient, 
	 use the nullValueInstance for every updating senarios

edit update()

edit other resttemplates in agents		

///////////////

1. To update some attributes of a class without risking over-writing others.

Basically, these attributes can be updated individually via @Modifying query methods and restful api.

However, a general update method can be achieved by assigning each of the attributes a "update-proof" value. 

Still, the codes of such a general update method can be verboss. More suspiciously, spring data haven't providing a 
magic over this. 

2. Integrate test

Integrate test can be extramely time consuming. Integrate test should be avoided with every possibility. 

If unit test is properly organized, then the need of integrate test will be reduced greatly. 


Anther method that may help is to use curl commands instead of browser for repeatative operations.

3. Suspicious codes in DeviceClient application:

Agent.java: 3 places (two agent update + one devicetype update, non editable)

DeviceAgent.java: 2 places : both need to consider

OSPAgent.java: 1 place

WeatherAgent.java: 1 place	

Question: 

How to know if the changes in deviceclient work correctly?

How to know that the changes in Common and in DeviceService work correctly?

The change is to use a voidInstance instead of a solidInstance from JPA.

In old way, JPA instance is updated fully but correctly(with greater risk)

In the new way, JPA instance is updated fully but correctly

In the old way, service layer receive a full instance

In the new way, service layer receive a partial instance

In the old way, the client sends a full but correct instance

In the new way, the client sends a partial instance

If a JPA instance merge a partial instance is the same with the old full instance, then the change is correct. 



If a


///

merge-> update

commend the situation

//

java -jar target/deviceclient-0.1.jar -host http://ec2-35-166-69-106.us-west-2.compute.amazonaws.com:9091 -type fastComDevice yuchen-test