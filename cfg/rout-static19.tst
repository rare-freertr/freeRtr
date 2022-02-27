description verify source with static routing

addrouter r1
int eth1 eth 0000.0000.1111 $1a$ $1b$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.101 255.255.255.255
 ipv6 addr 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.3 255.255.255.254
 ipv6 addr 1234:1::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe
 ipv4 verify rx
 ipv6 verify rx
 exit
ipv4 route v1 0.0.0.0 0.0.0.0 1.1.1.2
ipv6 route v1 :: :: 1234:1::2
!

addrouter r2
int eth1 eth 0000.0000.2222 $1b$ $1a$
int eth2 eth 0000.0000.2222 $2a$ $2b$
!
vrf def v1
 rd 1:1
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.2 255.255.255.254
 ipv6 addr 1234:1::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe
 ipv4 verify rx
 ipv6 verify rx
 exit
int eth2
 vrf for v1
 ipv4 addr 1.1.1.6 255.255.255.254
 ipv6 addr 1234:2::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe
 ipv4 verify rx
 ipv6 verify rx
 exit
ipv4 route v1 2.2.2.101 255.255.255.255 1.1.1.3
ipv6 route v1 4321::101 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
ipv4 route v1 2.2.2.201 255.255.255.255 1.1.1.7
ipv6 route v1 4321::201 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:2::3
!

addrouter r3
int eth1 eth 0000.0000.3333 $2b$ $2a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.201 255.255.255.255
 ipv6 addr 4321::201 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.7 255.255.255.254
 ipv6 addr 1234:2::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:fffe
 ipv4 verify rx
 ipv6 verify rx
 exit
ipv4 route v1 0.0.0.0 0.0.0.0 1.1.1.6
ipv6 route v1 :: :: 1234:2::2
!


r2 tping 100 5 2.2.2.201 /vrf v1
r2 tping 100 5 2.2.2.101 /vrf v1
r2 tping 100 5 4321::201 /vrf v1
r2 tping 100 5 4321::101 /vrf v1
r1 tping 100 5 2.2.2.201 /vrf v1
r1 tping 100 5 4321::201 /vrf v1
r3 tping 100 5 2.2.2.101 /vrf v1
r3 tping 100 5 4321::101 /vrf v1

r2 output show ipv4 route v1
r2 output show ipv6 route v1
