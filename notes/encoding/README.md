1. to set jvm encoding, 

	1.1 in windows: setx JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8
	
	
2. to check windows default encoding:

	2.1 in powershell: [System.Text.Encoding]::Default
	
	
3. to set cmd encoding to utf8 in windows: 

	3.1 in cmd:  cmd /K chcp 65001

4. for redis, start with --raw


