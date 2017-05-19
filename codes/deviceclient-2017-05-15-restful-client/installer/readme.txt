目录文件说明:
jsw(Java Service Wrapper): 制作windows服务使用的文件目录,具体作用以及使用方式请参考下文: 制作Windows服务步骤
build.bat: 制作安装包使用(运行前需要安装InstallAnywhere软件,编辑此文件可以修改软件安装的对应目录)
deviceclient.iap_xml: 制作安装包的时候需要读取的配置文件


制作Windows服务步骤:

将java注册为windows服务后，我们就直接可以通过windows的服务来启动和关闭java程序了。
1. 下载java service wrapper
    网址：http://sourceforge.net/projects/wrapper/ 或者 http://wrapper.tanukisoftware.com/doc/english/download.jsp
2. 配置过程
    1）首先确定你的电脑上有java运行环境，没有的话请安装。
    2）将你的java程序打包成jar包。（我的jar的名称为JavaServiceTest.jar，main方法所在类JavaServiceTest）
    3）在硬盘上创建文件夹test, 并在其下创建文件夹bin, conf, lib, logs。
    4）解压wrapper-windows-x86-32-3.5.20.zip， 并将其bin目录下的Wrapper.exe、src/bin目录下的App.bat.in、InstallApp-NT.bat.in、UninstallApp-NT.bat.in文件
          拷贝到test的bin目录中,并分别改名为App.bat、InstallApp-NT.bat、UninstallApp-NT.bat。
    5）将其lib目录下的Wrapper.DLL、wrapper.jar拷贝到test的lib目录中。并且将项目的jar和所用到的jar都拷贝到该目录（包括你自己的java程序jar）。
    6）将其src/conf目录下的wrapper.conf.in拷贝到workapp的conf目录中,并命名为wrapper.conf。
 3. 修改wrapper.conf文件
    主要修改下面几项：
   （1）JVM位置：
        wrapper.java.command=C:\jdk1.5.0_07\bin\java 或者 wrapper.java.command=%JAVA_HOME%/bin/java（需要在系统的环境变量里配置JAVA_HOME）
   （2）MAIN CLASS 此处决定了使用Java Service Wrapper的方式
        wrapper.java.mainclass=org.tanukisoftware.wrapper.WrapperSimpleApp
   （3）你的Java程序所需的jar包必须全部在此标明，注意路径准确：
        wrapper.java.classpath.1=../lib/JavaServiceTest.jar
        wrapper.java.classpath.2=../lib/wrapper.jar
        wrapper.java.classpath.3=../lib/sqljdbc4.jar
   （4）你的Wrapper.DLL或wrapper.jar所在的目录
        wrapper.java.library.path.1=../lib
   （5）你的Java应用程序的运行类（主类）
        wrapper.app.parameter.1=com.test.JavaServiceTest
   （6）注册为服务的名称和显示名，你可以随意进行设置
        wrapper.name=testwrapper
        wrapper.displayname= Test Wrapper Sample Application
   （7）服务描述信息
        wrapper.description= Test Wrapper Sample Application Description
   （8）服务的启动类型
      # Mode in which the service is installed.  AUTO_START, DELAY_START or DEMAND_START
      wrapper.ntservice.starttype=AUTO_START
 4. 修改好了以后，运行MyApp.bat即可运行你的java程序，这里可以测试配置的是否正确，如果可以运行，证明配置ok。
 5. 运行InstallApp-NT.bat可以进行服务的注册，UninstallApp-NT.bat为注销服务。
 6. 运行完注册服务InstallApp-NT.bat可以在 控制面板－管理程序－服务中看到你注册的服务名称。（如：Test Wrapper Sample Application）
