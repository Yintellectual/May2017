1. VPN pptp
		
	
http://www.anrip.com/post/1511


	1.1 ��װpptpd
	
	1.2 ����pptpd�� �û�����
	
		1.2.1 /etc/pptpd.conf
		
			localip 172.25.1.1
			remoteip 172.25.1.10-29
		
		1.2.2 /etc/ppp/pptpd-options
		
			ms-dns 8.8.8.8
			ms-dns 8.8.4.4
			
		1.2.3 /etc/ppp/chap-secrets
		
			user   pptpd    mm1234
	
	1.3 ����nat����������
	
		1.3.1 /etc/sysctl.conf
		
			net.ipv4.ip_forward=1
			
		1.3.2 /etc/iptables-rules
		
			iptables -t nat -A POSTROUTING -s 172.25.1.0/24 -o eth0 -j MASQUERADE
			iptables -I FORWARD -p tcp --syn -i ppp+ -j TCPMSS --set-mss 1356
			
			
	#!��������ʱ�ָ�NAT����
	echo "#!/bin/sh" > /etc/network/if-pre-up.d/iptables
	echo "iptables-restore < /etc/iptables-rules" >> /etc/network/if-pre-up.d/iptables
	chmod 0755 /etc/network/if-pre-up.d/iptables
			
	1.4 
		1.4.1 solution to connection limit reached
			"
			MGR: connections limit (100) reached, extra IP addresses ignored

			MGR: Manager process started

			MGR: Maximum of 100 connections available
			"
		
			�������ļ�/etc/pptpd.conf��ע�͵����е�logwtmp
			
2. write .sh for combined command lines

	2.1 a .sh file start with #!/bin/bash
	
	2.2 chmod +x <filename> allows .sh file to be executed
	
	2.3 put command lines into the file.

3. ">"  and ">>"

	">" will out put the result into a file, and replace all the old data
	">>" do concatenate 

4. check ownership

	ls -l <filename>
	

		
		


