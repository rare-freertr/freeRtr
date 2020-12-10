description ppp over ssh

addrouter r1
int eth1 eth 0000.0000.1111 $1a$ $1b$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 1.1.1.1 255.255.255.255
 exit
ipv4 pool p4 2.2.2.1 0.0.0.1 254
int di1
 enc ppp
 vrf for v1
 ipv4 addr 2.2.2.0 255.255.255.255
 ppp ip4cp local 2.2.2.0
 ipv4 pool p4
 ppp ip4cp open
 exit
int eth1
 vrf for v1
 ipv4 addr 3.3.3.1 255.255.255.252
 exit
aaa userlist usr
 username u password p
 username u privilege 14
 exit
crypto rsakey rsa generate 2048
crypto dsakey dsa generate 1024
crypto ecdsakey ecdsa generate 256
server tel tel
 vrf v1
 security rsakey rsa
 security dsakey dsa
 security ecdsakey ecdsa
 security protocol ssh
 security authen usr
 exec int di1
 exit
!

addrouter r2
int eth1 eth 0000.0000.2222 $1b$ $1a$
!
vrf def v1
 rd 1:1
 exit
proxy-profile p1
 vrf v1
 exit
prefix-list p1
 permit 0.0.0.0/0
 exit
int di1
 enc ppp
 vrf for v1
 ipv4 addr 4.4.4.4 255.255.255.128
 ppp ip4cp open
 ppp ip4cp local 0.0.0.0
 ipv4 gateway-prefix p1
 exit
int eth1
 vrf for v1
 ipv4 addr 3.3.3.2 255.255.255.252
 exit
chat-script login
 send ppp
 binsend 13
 exit
vpdn tel
 interface di1
 proxy p1
 user u
 pass p
 script login
 target 3.3.3.1
 vcid 23
 protocol ssh
 exit
!


r2 tping 100 40 2.2.2.0 /vrf v1
r2 tping 100 5 1.1.1.1 /vrf v1

r2 output show inter dia1 full
output ../binTmp/conn-ssh.html
<html><body bgcolor="#000000" text="#FFFFFF" link="#00FFFF" vlink="#00FFFF" alink="#00FFFF">
here is the interface:
<pre>
<!>show:0
</pre>
</body></html>
!
