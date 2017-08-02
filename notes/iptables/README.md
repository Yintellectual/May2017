"https://help.ubuntu.com/community/IptablesHowTo" 

Note that a loopback interface, or, the localhost(127.0.0.1), is hard to be further routed. Avoid putting efford routing a localhost port. 

before iptables, 

	echo 1 > /proc/sys/net/ipv4/ip_forward
	
	iptables -t nat -I PREROUTING -p tcp -i eth0 --dport 8085 -j DNAT --to-destination 127.0.0.1:8085

	ssh -g -L 80:localhost:8085 -f -N root@172.26.110.233 >>搞定

	netstat -tulpn
iptables -L

iptables -L -v

1. Iptables is the default firewall for all ubuntu systems. By default, it allows all traffic.

2. Basic commands:

	2.1 iptables -L
	
3. Basic iptables options

	3.1 -A
	
	3.2 -L
	
	3.3 -m conntrack : allow filter rules, permit --ctstate
	
	3.4 --ctstat
	
		3.4.1 NEW
		
		3.4.2 RELATED
		
		3.4.3 ESTABLISHED
		
		3.4.4 INVALID
		
	3.5 -m limit : reguire the rule to match only a limited number of times. permit --limit. 
	
		3.5.1 useful for limiting logging rules
		
		3.5.2 --limit : maximum matching rate
			default is "3/hour"
			- "/second"
			- "/minute"
			- "/hour"
			- "/day"
	
	3.6 -p :protocol
	
	3.7 --dport : the destination ports required for this rule. 
	
		3.7.1 range may be given as start:end, inclusive
		
	3.8 -j : Jump to the specified target
	
		3.8.1 ACCEPT
		
		3.8.2 REJECT
		
		3.8.3 DROP
		
		3.8.4 LOG
		
	3.9 --log-prefix : 
	
	3.10 --log-level : 7 is recommended
	
	3.11 -i : input interface
	
	3.12 -I : inserts a rule
	
		3.12.1 -I INPUT 5 //insert the rule into INPUT and make it 5th
		
	3.13 -v Display more information in the output
	
	3.14 -s --source:
	
	3.15 -d --destination 
	
	3.16 -o --out-interface

https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/4/html/Security_Guide/ch-fw.html#s2-firewall-ipt-background

7.1 iptables as Firewall

	7.1.1 Def. NAT = Network Address Translation
	
	7.1.2 Three kinds of Firewalls:
		
		- NAT: masquerading all requests on a LAN to one source
		
			++ easy to maintain
			
			-- has no protection once get passed
		
		- Packet Filter: reads packets. Netfilter is a built-in Packet Filter
		
			++ filter all traffic on router level rather than the application level
			
			++ faster than Proxy
			
			++ can easily work with iptables
			
			-- can not filter some packets
			
			-- hard to maintain
			
		- Proxy: a proxy firewall stands before the LAN client infront of the Internet
		
			++ works like a cache
			
			++ easy to monitor
			
			-- most proxies works with TCP connections only
			
			-- application services, unlike clients, cannot run behind a proxy.
			
			-- Proxies can become a bottleneck
		
	7.1.3 Netfilter and iptables
	
		- Netfilter are networking subsystem included by the Linux kernel. 
		
		- Netfilter is accessable through iptables untility.
		
		- Netfilter, controlled by iptables, can do NAT and IP masquerading. 
		
7.2 using iptables

	7.2.1 In Ubuntu, iptables are always running but not as a service. 
	
	7.2.2 the syntax of iptables are 
	
		iptables -A chain -j target, where chain is a chain like INPUT, OUTPUT, or FORWARD, and target is a target like ACCEPT, REJECT, and DROP.
		
	7.2.3 Both chains and targets can be created. 
	
	7.2.4 -A for append, -I for insert, -P for default
	
	7.2.5 saving and restoring iptables rules in Ubuntu
		
		in /etc/network/interfaces, add:

			pre-up iptables-restore < /etc/iptables.rules
			post-down iptables-save > /etc/iptables.rules
		
		or manually save and restore it by:
		
			sudo sh -c "iptables-save > /etc/iptables.rules"	
			sudo iptables-restore < /etc/iptables.rules
	
	7.2.6 iptables-apply can be used to test new rules.????
	
	7.2.7 LOGNDROP chain are recommended to provide detailed logging. 
	https://help.ubuntu.com/community/IptablesHowTo#Using_iptables-save.2Frestore_to_test_rules
	
	7.2.8 Disabling the firewall
	
	here is the script, fw.stop:
	
		echo "Stopping firewall and allowing everyone..."
		iptables -F
		iptables -X
		iptables -t nat -F
		iptables -t nat -X
		iptables -t mangle -F
		iptables -t mangle -X
		iptables -P INPUT ACCEPT
		iptables -P FORWARD ACCEPT
		iptables -P OUTPUT ACCEPT
		
	sudo chmod +x /root/fw.stop
	sudo /root/fw.stop
	
7.3 Common iptables filtering

	7.3.1 Opening a port for INPUT and OUTPUT
	
		iptables -A INPUT -p tcp -m tcp -sport 80 -j ACCEPT 
		iptables -A OUTPUT -p tcp -m tcp -dport 80 -j ACCEPT
		
		* port 443 for https, port 22 for ssh
		
7.4 Opening internal traffic of a LAN, and external traffic from a LAN to the Internet

	INPUTs are requests that comes in, 
	OUTPUTs are requests that goes out.
	FORWARDs are requests that will be routed. 
	PREROUTING are requests that will be masqueraded before routing.
	POSTROUTING are requests taht will be masqueraded after routing. 
	MASQUERADE masks the private addresses with external IP address of the firewall.
	
	
	10.0.0.0/24 is 10.0.0.1-10.0.0.255 

	10.0.0.0/8 is 10.0.0.0-10.255.255.255
	
	Whats the difference between --to and --to-destination?
	What is DNAT mean?
	DNAT = Destination Network Translation
	SNAT = Source Network Translation
	
https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/4/html/Security_Guide/s1-firewall-ipt-fwd.html

7.5 Anti-Hacker

	iptables -A OUTPUT -o eth0 -p tcp --dport 31337 --sport 31337 -j DROP
	iptables -A FORWARD -o eth0 -p tcp --dport 31337 --sport 31337 -j DROP

	BO is running on 31337
	
	iptables -A FORWARD -s 192.168.1.0/24 -i eth0 -j DROP

7.6 Connection States and Connection Tracking

	- NEW
	- ESTABLISHED
	- RELATED
	- INVALID
	
	more safer:
	iptables -A FORWARD -m state --state ESTABLISHED,RELATED -j ACCEPT 
	