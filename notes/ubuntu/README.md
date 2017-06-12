1. VPN pptp
		
	
http://www.anrip.com/post/1511


	1.1 安装pptpd
	
	1.2 配置pptpd， 用户名等
	
		1.2.1 /etc/pptpd.conf
		
			localip 172.25.1.1
			remoteip 172.25.1.10-29
		
		1.2.2 /etc/ppp/pptpd-options
		
			ms-dns 8.8.8.8
			ms-dns 8.8.4.4
			
		1.2.3 /etc/ppp/chap-secrets
		
			user   pptpd    mm1234
	
	1.3 配置nat以连接外网
	
		1.3.1 /etc/sysctl.conf
		
			net.ipv4.ip_forward=1
			
		1.3.2 /etc/iptables-rules
		
			iptables -t nat -A POSTROUTING -s 172.25.1.0/24 -o eth0 -j MASQUERADE
			iptables -I FORWARD -p tcp --syn -i ppp+ -j TCPMSS --set-mss 1356
			
			
9、网口启动时恢复NAT规则
echo "#!/bin/sh" > /etc/network/if-pre-up.d/iptables
echo "iptables-restore < /etc/iptables-rules" >> /etc/network/if-pre-up.d/iptables
chmod 0755 /etc/network/if-pre-up.d/iptables
			
			
		
		



