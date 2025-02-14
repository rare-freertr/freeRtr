description spf bgp ecmp connection

addrouter r1
int eth1 eth 0000.0000.1111 $1a$ $1b$
int eth2 eth 0000.0000.1111 $3a$ $3b$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.1 255.255.255.255
 ipv6 addr 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1.11
 vrf for v1
 ipv4 addr 1.1.1.1 255.255.255.252
 ipv6 addr 1234:1::1 ffff:ffff::
 exit
int eth1.22
 vrf for v1
 ipv4 addr 1.1.2.1 255.255.255.252
 ipv6 addr 1234:21::1 ffff:ffff::
 exit
int eth2.11
 vrf for v1
 ipv4 addr 1.1.1.10 255.255.255.252
 ipv6 addr 1234:3::2 ffff:ffff::
 exit
int eth2.22
 vrf for v1
 ipv4 addr 1.1.2.10 255.255.255.252
 ipv6 addr 1234:23::2 ffff:ffff::
 exit
router bgp4 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 1
 router-id 4.4.4.1
 neigh 1.1.1.2 remote-as 2
 neigh 1.1.1.2 linkstate
 neigh 1.1.1.9 remote-as 3
 neigh 1.1.1.9 linkstate
 neigh 1.1.2.2 remote-as 2
 neigh 1.1.2.2 linkstate
 neigh 1.1.2.9 remote-as 3
 neigh 1.1.2.9 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 1
 router-id 6.6.6.1
 neigh 1234:1::2 remote-as 2
 neigh 1234:1::2 linkstate
 neigh 1234:3::1 remote-as 3
 neigh 1234:3::1 linkstate
 neigh 1234:21::2 remote-as 2
 neigh 1234:21::2 linkstate
 neigh 1234:23::1 remote-as 3
 neigh 1234:23::1 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
!

addrouter r2
int eth1 eth 0000.0000.2222 $1b$ $1a$
int eth2 eth 0000.0000.2222 $2a$ $2b$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.2 255.255.255.255
 ipv6 addr 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1.11
 vrf for v1
 ipv4 addr 1.1.1.2 255.255.255.252
 ipv6 addr 1234:1::2 ffff:ffff::
 exit
int eth1.22
 vrf for v1
 ipv4 addr 1.1.2.2 255.255.255.252
 ipv6 addr 1234:21::2 ffff:ffff::
 exit
int eth2.11
 vrf for v1
 ipv4 addr 1.1.1.5 255.255.255.252
 ipv6 addr 1234:2::1 ffff:ffff::
 exit
int eth2.22
 vrf for v1
 ipv4 addr 1.1.2.5 255.255.255.252
 ipv6 addr 1234:22::1 ffff:ffff::
 exit
router bgp4 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 2
 router-id 4.4.4.2
 neigh 1.1.1.1 remote-as 1
 neigh 1.1.1.1 linkstate
 neigh 1.1.1.6 remote-as 3
 neigh 1.1.1.6 linkstate
 neigh 1.1.2.1 remote-as 1
 neigh 1.1.2.1 linkstate
 neigh 1.1.2.6 remote-as 3
 neigh 1.1.2.6 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 2
 router-id 6.6.6.2
 neigh 1234:1::1 remote-as 1
 neigh 1234:1::1 linkstate
 neigh 1234:2::2 remote-as 3
 neigh 1234:2::2 linkstate
 neigh 1234:21::1 remote-as 1
 neigh 1234:21::1 linkstate
 neigh 1234:22::2 remote-as 3
 neigh 1234:22::2 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
!

addrouter r3
int eth1 eth 0000.0000.3333 $2b$ $2a$
int eth2 eth 0000.0000.3333 $3b$ $3a$
!
vrf def v1
 rd 1:1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.3 255.255.255.255
 ipv6 addr 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1.11
 vrf for v1
 ipv4 addr 1.1.1.6 255.255.255.252
 ipv6 addr 1234:2::2 ffff:ffff::
 exit
int eth1.22
 vrf for v1
 ipv4 addr 1.1.2.6 255.255.255.252
 ipv6 addr 1234:22::2 ffff:ffff::
 exit
int eth2.11
 vrf for v1
 ipv4 addr 1.1.1.9 255.255.255.252
 ipv6 addr 1234:3::1 ffff:ffff::
 exit
int eth2.22
 vrf for v1
 ipv4 addr 1.1.2.9 255.255.255.252
 ipv6 addr 1234:23::1 ffff:ffff::
 exit
router bgp4 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 3
 router-id 4.4.4.3
 neigh 1.1.1.5 remote-as 2
 neigh 1.1.1.5 linkstate
 neigh 1.1.1.10 remote-as 1
 neigh 1.1.1.10 linkstate
 neigh 1.1.2.5 remote-as 2
 neigh 1.1.2.5 linkstate
 neigh 1.1.2.10 remote-as 1
 neigh 1.1.2.10 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 afi-spf ena
 address spf
 local-as 3
 router-id 6.6.6.3
 neigh 1234:2::1 remote-as 2
 neigh 1234:2::1 linkstate
 neigh 1234:3::2 remote-as 1
 neigh 1234:3::2 linkstate
 neigh 1234:22::1 remote-as 2
 neigh 1234:22::1 linkstate
 neigh 1234:23::2 remote-as 1
 neigh 1234:23::2 linkstate
 afi-spf spf-ecmp
 ecmp
 red conn
 exit
!

r1 tping 100 60 2.2.2.2 vrf v1
r1 tping 100 60 4321::2 vrf v1
r1 tping 100 60 2.2.2.3 vrf v1
r1 tping 100 60 4321::3 vrf v1

r2 tping 100 60 2.2.2.1 vrf v1
r2 tping 100 60 4321::1 vrf v1
r2 tping 100 60 2.2.2.3 vrf v1
r2 tping 100 60 4321::3 vrf v1

r3 tping 100 60 2.2.2.1 vrf v1
r3 tping 100 60 4321::1 vrf v1
r3 tping 100 60 2.2.2.2 vrf v1
r3 tping 100 60 4321::2 vrf v1
