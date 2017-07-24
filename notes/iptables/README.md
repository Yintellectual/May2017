"https://help.ubuntu.com/community/IptablesHowTo" 

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
		
	
	