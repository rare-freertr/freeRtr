description openflow: bridging and routing

addrouter r1
int eth1 eth 0000.0000.1111 $1a$ $1b$
!
vrf def v1
 rd 1:1
 exit
vrf def v9
 rd 1:1
 exit
int lo9
 vrf for v9
 ipv4 addr 10.10.10.227 255.255.255.255
 exit
int eth1
 vrf for v9
 ipv4 addr 10.11.12.254 255.255.255.0
 exit
server dhcp4 eth1
 pool 10.11.12.1 10.11.12.99
 gateway 10.11.12.254
 netmask 255.255.255.0
 dns-server 10.10.10.227
 domain-name ovs
 static 0000.0000.2222 10.11.12.111
 interface eth1
 vrf v9
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.101 255.255.255.255
 ipv6 addr 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
bridge 1
 exit
int bvi1
 vrf for v1
 ipv4 addr 1.1.1.1 255.255.255.0
 ipv6 addr 1234:1::1 ffff:ffff::
 exit
int sdn1
 bridge-gr 1
 exit
int sdn2
 bridge-gr 1
 exit
int sdn3
 bridge-gr 1
 exit
int sdn4
 vrf for v1
 ipv4 addr 1.1.4.1 255.255.255.0
 ipv6 addr 1234:4::1 ffff:ffff::
 exit
server openflow of
 export-vrf v1
 export-port sdn1 1
 export-port sdn2 2
 export-port sdn3 3
 export-port sdn4 4
 export-port bvi1 0
 vrf v9
 exit
ipv4 route v1 2.2.2.103 255.255.255.255 1.1.1.2
ipv4 route v1 2.2.2.104 255.255.255.255 1.1.1.3
ipv4 route v1 2.2.2.105 255.255.255.255 1.1.1.4
ipv4 route v1 2.2.2.106 255.255.255.255 1.1.4.2
ipv6 route v1 4321::103 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
ipv6 route v1 4321::104 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
ipv6 route v1 4321::105 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::4
ipv6 route v1 4321::106 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::2
!

addother r2
int eth1 eth 0000.0000.2222 $1b$ $1a$
int eth2 eth 0000.0000.2222 $2a$ $2b$
int eth3 eth 0000.0000.2222 $3a$ $3b$
int eth4 eth 0000.0000.2222 $4a$ $4b$
int eth5 eth 0000.0000.2222 $5a$ $5b$
int eth6 eth 0000.0000.2222 $6a$ $6b$
!
!

addrouter r3
int eth1 eth 0000.0000.3333 $2b$ $2a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.103 255.255.255.255
 ipv6 addr 4321::103 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.2 255.255.255.0
 ipv6 addr 1234:1::2 ffff:ffff::
 exit
ipv4 route v1 1.1.4.0 255.255.255.0 1.1.1.1
ipv6 route v1 1234:4:: ffff:ffff:: 1234:1::1
ipv4 route v1 2.2.2.101 255.255.255.255 1.1.1.1
ipv4 route v1 2.2.2.104 255.255.255.255 1.1.1.3
ipv4 route v1 2.2.2.105 255.255.255.255 1.1.1.4
ipv4 route v1 2.2.2.106 255.255.255.255 1.1.1.1
ipv6 route v1 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
ipv6 route v1 4321::104 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
ipv6 route v1 4321::105 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::4
ipv6 route v1 4321::106 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
!

addrouter r4
int eth1 eth 0000.0000.4444 $3b$ $3a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.104 255.255.255.255
 ipv6 addr 4321::104 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.3 255.255.255.0
 ipv6 addr 1234:1::3 ffff:ffff::
 exit
ipv4 route v1 1.1.4.0 255.255.255.0 1.1.1.1
ipv6 route v1 1234:4:: ffff:ffff:: 1234:1::1
ipv4 route v1 2.2.2.101 255.255.255.255 1.1.1.1
ipv4 route v1 2.2.2.103 255.255.255.255 1.1.1.2
ipv4 route v1 2.2.2.105 255.255.255.255 1.1.1.4
ipv4 route v1 2.2.2.106 255.255.255.255 1.1.1.1
ipv6 route v1 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
ipv6 route v1 4321::103 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
ipv6 route v1 4321::105 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::4
ipv6 route v1 4321::106 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
!

addrouter r5
int eth1 eth 0000.0000.5555 $4b$ $4a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.105 255.255.255.255
 ipv6 addr 4321::105 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.4 255.255.255.0
 ipv6 addr 1234:1::4 ffff:ffff::
 exit
ipv4 route v1 1.1.4.0 255.255.255.0 1.1.1.1
ipv6 route v1 1234:4:: ffff:ffff:: 1234:1::1
ipv4 route v1 2.2.2.101 255.255.255.255 1.1.1.1
ipv4 route v1 2.2.2.103 255.255.255.255 1.1.1.2
ipv4 route v1 2.2.2.104 255.255.255.255 1.1.1.3
ipv4 route v1 2.2.2.106 255.255.255.255 1.1.1.1
ipv6 route v1 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
ipv6 route v1 4321::103 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
ipv6 route v1 4321::104 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
ipv6 route v1 4321::106 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
!

addrouter r6
int eth1 eth 0000.0000.6666 $5b$ $5a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.106 255.255.255.255
 ipv6 addr 4321::106 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.4.2 255.255.255.0
 ipv6 addr 1234:4::2 ffff:ffff::
 exit
ipv4 route v1 1.1.1.0 255.255.255.0 1.1.4.1
ipv6 route v1 1234:1:: ffff:ffff:: 1234:4::1
ipv4 route v1 2.2.2.101 255.255.255.255 1.1.4.1
ipv4 route v1 2.2.2.103 255.255.255.255 1.1.4.1
ipv4 route v1 2.2.2.104 255.255.255.255 1.1.4.1
ipv4 route v1 2.2.2.105 255.255.255.255 1.1.4.1
ipv6 route v1 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::1
ipv6 route v1 4321::103 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::1
ipv6 route v1 4321::104 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::1
ipv6 route v1 4321::105 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::1
!


r1 tping 100 10 1.1.1.2 /vrf v1
r1 tping 100 10 1234:1::2 /vrf v1
r1 tping 100 10 1.1.1.3 /vrf v1
r1 tping 100 10 1234:1::3 /vrf v1
r1 tping 100 10 1.1.1.4 /vrf v1
r1 tping 100 10 1234:1::4 /vrf v1
r1 tping 100 10 1.1.4.2 /vrf v1
r1 tping 100 10 1234:4::2 /vrf v1

r3 tping 100 10 1.1.1.2 /vrf v1
r3 tping 100 10 1234:1::2 /vrf v1
r3 tping 100 10 1.1.1.3 /vrf v1
r3 tping 100 10 1234:1::3 /vrf v1
r3 tping 100 10 1.1.1.4 /vrf v1
r3 tping 100 10 1234:1::4 /vrf v1
r3 tping 100 10 1.1.4.2 /vrf v1
r3 tping 100 10 1234:4::2 /vrf v1

r4 tping 100 10 1.1.1.2 /vrf v1
r4 tping 100 10 1234:1::2 /vrf v1
r4 tping 100 10 1.1.1.3 /vrf v1
r4 tping 100 10 1234:1::3 /vrf v1
r4 tping 100 10 1.1.1.4 /vrf v1
r4 tping 100 10 1234:1::4 /vrf v1
r4 tping 100 10 1.1.4.2 /vrf v1
r4 tping 100 10 1234:4::2 /vrf v1

r5 tping 100 10 1.1.1.2 /vrf v1
r5 tping 100 10 1234:1::2 /vrf v1
r5 tping 100 10 1.1.1.3 /vrf v1
r5 tping 100 10 1234:1::3 /vrf v1
r5 tping 100 10 1.1.1.4 /vrf v1
r5 tping 100 10 1234:1::4 /vrf v1
r5 tping 100 10 1.1.4.2 /vrf v1
r5 tping 100 10 1234:4::2 /vrf v1

r6 tping 100 10 1.1.1.2 /vrf v1
r6 tping 100 10 1234:1::2 /vrf v1
r6 tping 100 10 1.1.1.3 /vrf v1
r6 tping 100 10 1234:1::3 /vrf v1
r6 tping 100 10 1.1.1.4 /vrf v1
r6 tping 100 10 1234:1::4 /vrf v1
r6 tping 100 10 1.1.4.2 /vrf v1
r6 tping 100 10 1234:4::2 /vrf v1

r1 tping 100 10 2.2.2.101 /vrf v1 /int lo0
r1 tping 100 10 4321::101 /vrf v1 /int lo0
r1 tping 100 10 2.2.2.103 /vrf v1 /int lo0
r1 tping 100 10 4321::103 /vrf v1 /int lo0
r1 tping 100 10 2.2.2.104 /vrf v1 /int lo0
r1 tping 100 10 4321::104 /vrf v1 /int lo0
r1 tping 100 10 2.2.2.105 /vrf v1 /int lo0
r1 tping 100 10 4321::105 /vrf v1 /int lo0
r1 tping 100 10 2.2.2.106 /vrf v1 /int lo0
r1 tping 100 10 4321::106 /vrf v1 /int lo0

r3 tping 100 10 2.2.2.101 /vrf v1 /int lo0
r3 tping 100 10 4321::101 /vrf v1 /int lo0
r3 tping 100 10 2.2.2.103 /vrf v1 /int lo0
r3 tping 100 10 4321::103 /vrf v1 /int lo0
r3 tping 100 10 2.2.2.104 /vrf v1 /int lo0
r3 tping 100 10 4321::104 /vrf v1 /int lo0
r3 tping 100 10 2.2.2.105 /vrf v1 /int lo0
r3 tping 100 10 4321::105 /vrf v1 /int lo0
r3 tping 100 10 2.2.2.106 /vrf v1 /int lo0
r3 tping 100 10 4321::106 /vrf v1 /int lo0

r4 tping 100 10 2.2.2.101 /vrf v1 /int lo0
r4 tping 100 10 4321::101 /vrf v1 /int lo0
r4 tping 100 10 2.2.2.103 /vrf v1 /int lo0
r4 tping 100 10 4321::103 /vrf v1 /int lo0
r4 tping 100 10 2.2.2.104 /vrf v1 /int lo0
r4 tping 100 10 4321::104 /vrf v1 /int lo0
r4 tping 100 10 2.2.2.105 /vrf v1 /int lo0
r4 tping 100 10 4321::105 /vrf v1 /int lo0
r4 tping 100 10 2.2.2.106 /vrf v1 /int lo0
r4 tping 100 10 4321::106 /vrf v1 /int lo0

r5 tping 100 10 2.2.2.101 /vrf v1 /int lo0
r5 tping 100 10 4321::101 /vrf v1 /int lo0
r5 tping 100 10 2.2.2.103 /vrf v1 /int lo0
r5 tping 100 10 4321::103 /vrf v1 /int lo0
r5 tping 100 10 2.2.2.104 /vrf v1 /int lo0
r5 tping 100 10 4321::104 /vrf v1 /int lo0
r5 tping 100 10 2.2.2.105 /vrf v1 /int lo0
r5 tping 100 10 4321::105 /vrf v1 /int lo0
r5 tping 100 10 2.2.2.106 /vrf v1 /int lo0
r5 tping 100 10 4321::106 /vrf v1 /int lo0

r6 tping 100 10 2.2.2.101 /vrf v1 /int lo0
r6 tping 100 10 4321::101 /vrf v1 /int lo0
r6 tping 100 10 2.2.2.103 /vrf v1 /int lo0
r6 tping 100 10 4321::103 /vrf v1 /int lo0
r6 tping 100 10 2.2.2.104 /vrf v1 /int lo0
r6 tping 100 10 4321::104 /vrf v1 /int lo0
r6 tping 100 10 2.2.2.105 /vrf v1 /int lo0
r6 tping 100 10 4321::105 /vrf v1 /int lo0
r6 tping 100 10 2.2.2.106 /vrf v1 /int lo0
r6 tping 100 10 4321::106 /vrf v1 /int lo0

r1 dping sdn 10 0-10000 r6 100 10 2.2.2.105 /vrf v1 /int lo0 /siz 1111 /rep 1111
r1 dping sdn 10 0-10000 r6 100 10 4321::105 /vrf v1 /int lo0 /siz 1111 /rep 1111

r1 send tclsh
r1 output exec "telnet 10.11.12.111 2323 /vrf v9 /int lo9"
output ../binTmp/opnflw-rout02.html
<html><body bgcolor="#000000" text="#FFFFFF" link="#00FFFF" vlink="#00FFFF" alink="#00FFFF">
here are the flows:
<pre>
<!>show:0
</pre>
</body></html>
!
