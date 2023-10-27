description unicast+l3evpns over ibgp rr with dynamic capability

addrouter r1
int eth1 eth 0000.0000.1111 $1a$ $1b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
vrf def v3
 rd 1:3
 rt-both 1:3
 exit
vrf def v4
 rd 1:4
 rt-both 1:4
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.1 255.255.255.255
 ipv6 addr 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v1
 ipv4 addr 2.2.2.11 255.255.255.255
 ipv6 addr 4321::11 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo2
 vrf for v2
 ipv4 addr 9.9.2.1 255.255.255.255
 ipv6 addr 9992::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo3
 vrf for v3
 ipv4 addr 9.9.3.1 255.255.255.255
 ipv6 addr 9993::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo4
 vrf for v4
 ipv4 addr 9.9.4.1 255.255.255.255
 ipv6 addr 9994::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.1 255.255.255.252
 ipv6 addr 1234:1::1 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.2 255.255.255.255 1.1.1.2
ipv6 route v1 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
ipv4 route v1 2.2.2.3 255.255.255.255 1.1.1.3
ipv6 route v1 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
router bgp4 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 4.4.4.1
 neigh 2.2.2.3 remote-as 1
 neigh 2.2.2.3 update lo0
 neigh 2.2.2.3 send-comm both
 neigh 2.2.2.3 dynamic
 neigh 2.2.2.3 confed
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-vrf v2 import evpn
 afi-vrf v2 export evpn
 afi-vrf v3 ena
 afi-vrf v3 red conn
 afi-vrf v3 import evpn
 afi-vrf v3 export evpn
 afi-vrf v4 ena
 afi-vrf v4 red conn
 afi-vrf v4 import evpn
 afi-vrf v4 export evpn
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 6.6.6.1
 neigh 4321::3 remote-as 1
 neigh 4321::3 update lo0
 neigh 4321::3 send-comm both
 neigh 4321::3 dynamic
 neigh 4321::3 confed
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-vrf v2 import evpn
 afi-vrf v2 export evpn
 afi-vrf v3 ena
 afi-vrf v3 red conn
 afi-vrf v3 import evpn
 afi-vrf v3 export evpn
 afi-vrf v4 ena
 afi-vrf v4 red conn
 afi-vrf v4 import evpn
 afi-vrf v4 export evpn
 red conn
 exit
!

addrouter r2
int eth1 eth 0000.0000.2222 $2a$ $2b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
vrf def v3
 rd 1:3
 rt-both 1:3
 exit
vrf def v4
 rd 1:4
 rt-both 1:4
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.2 255.255.255.255
 ipv6 addr 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v1
 ipv4 addr 2.2.2.12 255.255.255.255
 ipv6 addr 4321::12 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo2
 vrf for v2
 ipv4 addr 9.9.2.2 255.255.255.255
 ipv6 addr 9992::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo3
 vrf for v3
 ipv4 addr 9.9.3.2 255.255.255.255
 ipv6 addr 9993::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo4
 vrf for v4
 ipv4 addr 9.9.4.2 255.255.255.255
 ipv6 addr 9994::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.2 255.255.255.252
 ipv6 addr 1234:1::2 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.1 255.255.255.255 1.1.1.1
ipv6 route v1 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
ipv4 route v1 2.2.2.3 255.255.255.255 1.1.1.3
ipv6 route v1 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::3
router bgp4 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 4.4.4.2
 neigh 2.2.2.3 remote-as 1
 neigh 2.2.2.3 update lo0
 neigh 2.2.2.3 send-comm both
 neigh 2.2.2.3 dynamic
 neigh 2.2.2.3 confed
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-vrf v2 import evpn
 afi-vrf v2 export evpn
 afi-vrf v3 ena
 afi-vrf v3 red conn
 afi-vrf v3 import evpn
 afi-vrf v3 export evpn
 afi-vrf v4 ena
 afi-vrf v4 red conn
 afi-vrf v4 import evpn
 afi-vrf v4 export evpn
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 6.6.6.2
 neigh 4321::3 remote-as 1
 neigh 4321::3 update lo0
 neigh 4321::3 send-comm both
 neigh 4321::3 dynamic
 neigh 4321::3 confed
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-vrf v2 import evpn
 afi-vrf v2 export evpn
 afi-vrf v3 ena
 afi-vrf v3 red conn
 afi-vrf v3 import evpn
 afi-vrf v3 export evpn
 afi-vrf v4 ena
 afi-vrf v4 red conn
 afi-vrf v4 import evpn
 afi-vrf v4 export evpn
 red conn
 exit
!


addrouter r3
int eth1 eth 0000.0000.3333 $1b$ $1a$
int eth2 eth 0000.0000.3333 $2b$ $2a$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
bridge 1
 mac-learn
 exit
int eth1
 bridge-gr 1
 exit
int eth2
 bridge-gr 1
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.3 255.255.255.255
 ipv6 addr 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v1
 ipv4 addr 2.2.2.13 255.255.255.255
 ipv6 addr 4321::13 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int bvi1
 vrf for v1
 ipv4 addr 1.1.1.3 255.255.255.252
 ipv6 addr 1234:1::3 ffff:ffff::
 exit
ipv4 route v1 2.2.2.1 255.255.255.255 1.1.1.1
ipv6 route v1 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
ipv4 route v1 2.2.2.2 255.255.255.255 1.1.1.2
ipv6 route v1 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
router bgp4 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 4.4.4.3
 neigh 2.2.2.1 remote-as 1
 neigh 2.2.2.1 update lo0
 neigh 2.2.2.1 send-comm both
 neigh 2.2.2.1 dynamic
 neigh 2.2.2.1 route-reflector
 neigh 2.2.2.2 remote-as 1
 neigh 2.2.2.2 update lo0
 neigh 2.2.2.2 send-comm both
 neigh 2.2.2.2 dynamic
 neigh 2.2.2.2 route-reflector
 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address unicast evpn
 local-as 1
 router-id 4.4.4.3
 neigh 4321::1 remote-as 1
 neigh 4321::1 update lo0
 neigh 4321::1 send-comm both
 neigh 4321::1 dynamic
 neigh 4321::1 route-reflector
 neigh 4321::2 remote-as 1
 neigh 4321::2 update lo0
 neigh 4321::2 send-comm both
 neigh 4321::2 dynamic
 neigh 4321::2 route-reflector
 red conn
 exit
!




r3 tping 100 60 2.2.2.2 vrf v1 sou lo0
r3 tping 100 60 4321::2 vrf v1 sou lo0

r3 tping 100 60 2.2.2.1 vrf v1 sou lo0
r3 tping 100 60 4321::1 vrf v1 sou lo0

r3 tping 100 60 2.2.2.12 vrf v1 sou lo1
r3 tping 100 60 4321::12 vrf v1 sou lo1

r3 tping 100 60 2.2.2.11 vrf v1 sou lo1
r3 tping 100 60 4321::11 vrf v1 sou lo1

r1 tping 100 60 2.2.2.2 vrf v1 sou lo0
r1 tping 100 60 4321::2 vrf v1 sou lo0

r2 tping 100 60 2.2.2.1 vrf v1 sou lo0
r2 tping 100 60 4321::1 vrf v1 sou lo0

r1 tping 100 60 2.2.2.12 vrf v1 sou lo1
r1 tping 100 60 4321::12 vrf v1 sou lo1

r2 tping 100 60 2.2.2.11 vrf v1 sou lo1
r2 tping 100 60 4321::11 vrf v1 sou lo1

r1 tping 100 60 9.9.2.2 vrf v2
r2 tping 100 60 9.9.2.1 vrf v2
r1 tping 100 60 9992::2 vrf v2
r2 tping 100 60 9992::1 vrf v2

r1 tping 100 60 9.9.3.2 vrf v3
r2 tping 100 60 9.9.3.1 vrf v3
r1 tping 100 60 9993::2 vrf v3
r2 tping 100 60 9993::1 vrf v3

r1 tping 100 60 9.9.4.2 vrf v4
r2 tping 100 60 9.9.4.1 vrf v4
r1 tping 100 60 9994::2 vrf v4
r2 tping 100 60 9994::1 vrf v4

r1 send clear ipv4 bgp 1 peer 2.2.2.3 del evpn
r1 send clear ipv6 bgp 1 peer 4321::3 del evpn

r1 tping 100 3 2.2.2.12 vrf v1 sou lo1
r1 tping 100 3 4321::12 vrf v1 sou lo1

r2 tping 100 3 2.2.2.11 vrf v1 sou lo1
r2 tping 100 3 4321::11 vrf v1 sou lo1

r1 tping 0 3 9.9.2.2 vrf v2
r2 tping 0 3 9.9.2.1 vrf v2
r1 tping 0 3 9992::2 vrf v2
r2 tping 0 3 9992::1 vrf v2

r1 tping 0 3 9.9.3.2 vrf v3
r2 tping 0 3 9.9.3.1 vrf v3
r1 tping 0 3 9993::2 vrf v3
r2 tping 0 3 9993::1 vrf v3

r1 tping 0 3 9.9.4.2 vrf v4
r2 tping 0 3 9.9.4.1 vrf v4
r1 tping 0 3 9994::2 vrf v4
r2 tping 0 3 9994::1 vrf v4

r1 send clear ipv4 bgp 1 peer 2.2.2.3 add evpn
r1 send clear ipv6 bgp 1 peer 4321::3 add evpn

r1 tping 100 3 2.2.2.12 vrf v1 sou lo1
r1 tping 100 3 4321::12 vrf v1 sou lo1

r2 tping 100 3 2.2.2.11 vrf v1 sou lo1
r2 tping 100 3 4321::11 vrf v1 sou lo1

r1 tping 100 3 9.9.2.2 vrf v2
r2 tping 100 3 9.9.2.1 vrf v2
r1 tping 100 3 9992::2 vrf v2
r2 tping 100 3 9992::1 vrf v2

r1 tping 100 3 9.9.3.2 vrf v3
r2 tping 100 3 9.9.3.1 vrf v3
r1 tping 100 3 9993::2 vrf v3
r2 tping 100 3 9993::1 vrf v3

r1 tping 100 3 9.9.4.2 vrf v4
r2 tping 100 3 9.9.4.1 vrf v4
r1 tping 100 3 9994::2 vrf v4
r2 tping 100 3 9994::1 vrf v4
