for basic scripts, use a .cmd file

here is an example:

		cd c:\users\yuchen\submit\common

		START /B mvn clean install

		TIMEOUT 20

		cd C:\Users\yuchen\submit\serviceportal

		docker-compose down --rmi local

		START /B mvn clean package

		TIMEOUT 50

		chrome localhost/sp/login?org=demo

		docker-compose up

However, java programs don't terminate properly in .cmd script. Therefore, TIMEOUT is used to set a clock for its termination. 

Also, 

"START" means "start this command in a new process"

"START /B" means start this command in a new process but the same window.

"/WAIT" means wait for this job to finish, which do not works on java programs.
