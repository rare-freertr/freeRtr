description bgp interas othervpn with multiple labels

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
int lo0
 vrf for v1
 ipv4 addr 2.2.2.1 255.255.255.255
 ipv6 addr 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.1 255.255.255.255
 ipv6 addr 3331::1 ffff:ffff:ffff:ffff::
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
ipv4 route v1 2.2.2.3 255.255.255.255 1.1.1.2
ipv6 route v1 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::2
router bgp4 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 4.4.4.1
 neigh 2.2.2.2 remote-as 1
 neigh 2.2.2.2 update lo0
 neigh 2.2.2.2 send-comm both
 neigh 2.2.2.2 multiple-label vpnuni ovpnuni
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 6.6.6.1
 neigh 4321::2 remote-as 1
 neigh 4321::2 update lo0
 neigh 4321::2 send-comm both
 neigh 4321::2 multiple-label vpnuni ovpnuni
 exit
!

addrouter r2
int eth1 eth 0000.0000.2222 $1b$ $1a$
int eth2 eth 0000.0000.2222 $2a$ $2b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.2 255.255.255.255
 ipv6 addr 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.2 255.255.255.255
 ipv6 addr 3332::2 ffff:ffff:ffff:ffff::
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.2 255.255.255.252
 ipv6 addr 1234:1::2 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
int eth2
 vrf for v1
 ipv4 addr 1.1.1.5 255.255.255.252
 ipv6 addr 1234:2::1 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.3 255.255.255.255 1.1.1.6
ipv6 route v1 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:2::2
ipv4 route v1 2.2.2.1 255.255.255.255 1.1.1.1
ipv6 route v1 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:1::1
router bgp4 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 4.4.4.2
 neigh 2.2.2.1 remote-as 1
 neigh 2.2.2.1 update lo0
 neigh 2.2.2.1 send-comm both
 neigh 2.2.2.1 multiple-label vpnuni ovpnuni
 neigh 2.2.2.1 route-reflect
 neigh 2.2.2.3 remote-as 1
 neigh 2.2.2.3 update lo0
 neigh 2.2.2.3 send-comm both
 neigh 2.2.2.3 multiple-label vpnuni ovpnuni
 neigh 2.2.2.3 route-reflect
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 6.6.6.2
 neigh 4321::1 remote-as 1
 neigh 4321::1 update lo0
 neigh 4321::1 send-comm both
 neigh 4321::1 multiple-label vpnuni ovpnuni
 neigh 4321::1 route-reflect
 neigh 4321::3 remote-as 1
 neigh 4321::3 update lo0
 neigh 4321::3 send-comm both
 neigh 4321::3 multiple-label vpnuni ovpnuni
 neigh 4321::3 route-reflect
 exit
!

addrouter r3
int eth1 eth 0000.0000.3333 $2b$ $2a$
int eth2 eth 0000.0000.3333 $3a$ $3b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.3 255.255.255.255
 ipv6 addr 4321::3 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.3 255.255.255.255
 ipv6 addr 3333::3 ffff:ffff:ffff:ffff::
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.6 255.255.255.252
 ipv6 addr 1234:2::2 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
int eth2
 vrf for v1
 ipv4 addr 1.1.1.9 255.255.255.252
 ipv6 addr 1234:3::1 ffff:ffff::
 mpls enable
 exit
ipv4 route v1 2.2.2.1 255.255.255.255 1.1.1.5
ipv6 route v1 4321::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:2::1
ipv4 route v1 2.2.2.2 255.255.255.255 1.1.1.5
ipv6 route v1 4321::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:2::1
ipv4 route v1 1.1.1.10 255.255.255.255 1.1.1.10 mplsimp
ipv6 route v1 1234:3::2 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:3::2 mplsimp
router bgp4 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 4.4.4.3
 neigh 2.2.2.2 remote-as 1
 neigh 2.2.2.2 update lo0
 neigh 2.2.2.2 send-comm both
 neigh 2.2.2.2 multiple-label vpnuni ovpnuni
 neigh 2.2.2.2 next-hop-multi
 neigh 1.1.1.10 remote-as 2
 neigh 1.1.1.10 send-comm both
 neigh 1.1.1.10 multiple-label vpnuni ovpnuni
 neigh 1.1.1.10 next-hop-multi
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 1
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 1
 router-id 6.6.6.3
 neigh 4321::2 remote-as 1
 neigh 4321::2 update lo0
 neigh 4321::2 send-comm both
 neigh 4321::2 multiple-label vpnuni ovpnuni
 neigh 4321::2 next-hop-multi
 neigh 1234:3::2 remote-as 2
 neigh 1234:3::2 send-comm both
 neigh 1234:3::2 multiple-label vpnuni ovpnuni
 neigh 1234:3::2 next-hop-multi
 exit
!

addrouter r4
int eth1 eth 0000.0000.4444 $3b$ $3a$
int eth2 eth 0000.0000.4444 $4a$ $4b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.4 255.255.255.255
 ipv6 addr 4321::4 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.4 255.255.255.255
 ipv6 addr 3334::4 ffff:ffff:ffff:ffff::
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.10 255.255.255.252
 ipv6 addr 1234:3::2 ffff:ffff::
 mpls enable
 exit
int eth2
 vrf for v1
 ipv4 addr 1.1.1.13 255.255.255.252
 ipv6 addr 1234:4::1 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.5 255.255.255.255 1.1.1.14
ipv6 route v1 4321::5 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::2
ipv4 route v1 2.2.2.6 255.255.255.255 1.1.1.14
ipv6 route v1 4321::6 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::2
ipv4 route v1 1.1.1.9 255.255.255.255 1.1.1.9 mplsimp
ipv6 route v1 1234:3::1 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:3::1 mplsimp
router bgp4 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 4.4.4.4
 neigh 2.2.2.5 remote-as 2
 neigh 2.2.2.5 update lo0
 neigh 2.2.2.5 send-comm both
 neigh 2.2.2.5 multiple-label vpnuni ovpnuni
 neigh 2.2.2.5 next-hop-multi
 neigh 1.1.1.9 remote-as 1
 neigh 1.1.1.9 send-comm both
 neigh 1.1.1.9 multiple-label vpnuni ovpnuni
 neigh 1.1.1.9 next-hop-multi
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 6.6.6.4
 neigh 4321::5 remote-as 2
 neigh 4321::5 update lo0
 neigh 4321::5 send-comm both
 neigh 4321::5 multiple-label vpnuni ovpnuni
 neigh 4321::5 next-hop-multi
 neigh 1234:3::1 remote-as 1
 neigh 1234:3::1 send-comm both
 neigh 1234:3::1 multiple-label vpnuni ovpnuni
 neigh 1234:3::1 next-hop-multi
 exit
!

addrouter r5
int eth1 eth 0000.0000.5555 $4b$ $4a$
int eth2 eth 0000.0000.5555 $5a$ $5b$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.5 255.255.255.255
 ipv6 addr 4321::5 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.5 255.255.255.255
 ipv6 addr 3335::5 ffff:ffff:ffff:ffff::
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.14 255.255.255.252
 ipv6 addr 1234:4::2 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
int eth2
 vrf for v1
 ipv4 addr 1.1.1.17 255.255.255.252
 ipv6 addr 1234:5::1 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.4 255.255.255.255 1.1.1.13
ipv6 route v1 4321::4 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:4::1
ipv4 route v1 2.2.2.6 255.255.255.255 1.1.1.18
ipv6 route v1 4321::6 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:5::2
router bgp4 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 4.4.4.5
 neigh 2.2.2.4 remote-as 2
 neigh 2.2.2.4 update lo0
 neigh 2.2.2.4 send-comm both
 neigh 2.2.2.4 multiple-label vpnuni ovpnuni
 neigh 2.2.2.4 route-reflect
 neigh 2.2.2.6 remote-as 2
 neigh 2.2.2.6 update lo0
 neigh 2.2.2.6 send-comm both
 neigh 2.2.2.6 multiple-label vpnuni ovpnuni
 neigh 2.2.2.6 route-reflect
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 6.6.6.5
 neigh 4321::4 remote-as 2
 neigh 4321::4 update lo0
 neigh 4321::4 send-comm both
 neigh 4321::4 multiple-label vpnuni ovpnuni
 neigh 4321::4 route-reflect
 neigh 4321::6 remote-as 2
 neigh 4321::6 update lo0
 neigh 4321::6 send-comm both
 neigh 4321::6 multiple-label vpnuni ovpnuni
 neigh 4321::6 route-reflect
 exit
!

addrouter r6
int eth1 eth 0000.0000.6666 $5b$ $5a$
!
vrf def v1
 rd 1:1
 label-mode per-prefix
 exit
vrf def v2
 rd 1:2
 rt-both 1:2
 exit
int lo0
 vrf for v1
 ipv4 addr 2.2.2.6 255.255.255.255
 ipv6 addr 4321::6 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 exit
int lo1
 vrf for v2
 ipv4 addr 3.3.3.6 255.255.255.255
 ipv6 addr 3336::6 ffff:ffff:ffff:ffff::
 exit
int eth1
 vrf for v1
 ipv4 addr 1.1.1.18 255.255.255.252
 ipv6 addr 1234:5::2 ffff:ffff::
 mpls enable
 mpls ldp4
 mpls ldp6
 exit
ipv4 route v1 2.2.2.4 255.255.255.255 1.1.1.17
ipv6 route v1 4321::4 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:5::1
ipv4 route v1 2.2.2.5 255.255.255.255 1.1.1.17
ipv6 route v1 4321::5 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff 1234:5::1
router bgp4 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 4.4.4.6
 neigh 2.2.2.5 remote-as 2
 neigh 2.2.2.5 update lo0
 neigh 2.2.2.5 send-comm both
 neigh 2.2.2.5 multiple-label vpnuni ovpnuni
 afi-vrf v2 ena
 afi-vrf v2 red conn
 afi-ovrf v2 ena
 afi-ovrf v2 red conn
 exit
router bgp6 2
 vrf v1
 no safe-ebgp
 address vpnuni ovpnuni
 local-as 2
 router-id 6.6.6.6
 neigh 4321::5 remote-as 2
 neigh 4321::5 update lo0
 neigh 4321::5 send-comm both
 neigh 4321::5 multiple-label vpnuni ovpnuni
 exit
!


r1 tping 100 60 2.2.2.2 vrf v1 sou lo0
r1 tping 100 60 4321::2 vrf v1 sou lo0
r1 tping 100 60 2.2.2.3 vrf v1 sou lo0
r1 tping 100 60 4321::3 vrf v1 sou lo0

r2 tping 100 60 2.2.2.1 vrf v1 sou lo0
r2 tping 100 60 4321::1 vrf v1 sou lo0
r2 tping 100 60 2.2.2.3 vrf v1 sou lo0
r2 tping 100 60 4321::3 vrf v1 sou lo0

r3 tping 100 60 2.2.2.1 vrf v1 sou lo0
r3 tping 100 60 4321::1 vrf v1 sou lo0
r3 tping 100 60 2.2.2.2 vrf v1 sou lo0
r3 tping 100 60 4321::2 vrf v1 sou lo0

r4 tping 100 60 2.2.2.5 vrf v1 sou lo0
r4 tping 100 60 4321::5 vrf v1 sou lo0
r4 tping 100 60 2.2.2.6 vrf v1 sou lo0
r4 tping 100 60 4321::6 vrf v1 sou lo0

r5 tping 100 60 2.2.2.4 vrf v1 sou lo0
r5 tping 100 60 4321::4 vrf v1 sou lo0
r5 tping 100 60 2.2.2.6 vrf v1 sou lo0
r5 tping 100 60 4321::6 vrf v1 sou lo0

r6 tping 100 60 2.2.2.4 vrf v1 sou lo0
r6 tping 100 60 4321::4 vrf v1 sou lo0
r6 tping 100 60 2.2.2.5 vrf v1 sou lo0
r6 tping 100 60 4321::5 vrf v1 sou lo0

r1 tping 100 60 3.3.3.2 vrf v2 sou lo1
r1 tping 100 60 3332::2 vrf v2 sou lo1
r1 tping 100 60 3.3.3.3 vrf v2 sou lo1
r1 tping 100 60 3333::3 vrf v2 sou lo1

r2 tping 100 60 3.3.3.1 vrf v2 sou lo1
r2 tping 100 60 3331::1 vrf v2 sou lo1
r2 tping 100 60 3.3.3.3 vrf v2 sou lo1
r2 tping 100 60 3333::3 vrf v2 sou lo1

r3 tping 100 60 3.3.3.1 vrf v2 sou lo1
r3 tping 100 60 3331::1 vrf v2 sou lo1
r3 tping 100 60 3.3.3.2 vrf v2 sou lo1
r3 tping 100 60 3332::2 vrf v2 sou lo1

r4 tping 100 60 3.3.3.5 vrf v2 sou lo1
r4 tping 100 60 3335::5 vrf v2 sou lo1
r4 tping 100 60 3.3.3.6 vrf v2 sou lo1
r4 tping 100 60 3336::6 vrf v2 sou lo1

r5 tping 100 60 3.3.3.4 vrf v2 sou lo1
r5 tping 100 60 3334::4 vrf v2 sou lo1
r5 tping 100 60 3.3.3.6 vrf v2 sou lo1
r5 tping 100 60 3336::6 vrf v2 sou lo1

r6 tping 100 60 3.3.3.4 vrf v2 sou lo1
r6 tping 100 60 3334::4 vrf v2 sou lo1
r6 tping 100 60 3.3.3.5 vrf v2 sou lo1
r6 tping 100 60 3335::5 vrf v2 sou lo1

r4 tping 100 60 3.3.3.1 vrf v2 sou lo1
r4 tping 100 60 3331::1 vrf v2 sou lo1
r4 tping 100 60 3.3.3.2 vrf v2 sou lo1
r4 tping 100 60 3332::2 vrf v2 sou lo1
r4 tping 100 60 3.3.3.3 vrf v2 sou lo1
r4 tping 100 60 3333::3 vrf v2 sou lo1

r5 tping 100 60 3.3.3.1 vrf v2 sou lo1
r5 tping 100 60 3331::1 vrf v2 sou lo1
r5 tping 100 60 3.3.3.2 vrf v2 sou lo1
r5 tping 100 60 3332::2 vrf v2 sou lo1
r5 tping 100 60 3.3.3.3 vrf v2 sou lo1
r5 tping 100 60 3333::3 vrf v2 sou lo1

r6 tping 100 60 3.3.3.1 vrf v2 sou lo1
r6 tping 100 60 3331::1 vrf v2 sou lo1
r6 tping 100 60 3.3.3.2 vrf v2 sou lo1
r6 tping 100 60 3332::2 vrf v2 sou lo1
r6 tping 100 60 3.3.3.3 vrf v2 sou lo1
r6 tping 100 60 3333::3 vrf v2 sou lo1

r1 tping 100 60 3.3.3.4 vrf v2 sou lo1
r1 tping 100 60 3334::4 vrf v2 sou lo1
r1 tping 100 60 3.3.3.5 vrf v2 sou lo1
r1 tping 100 60 3335::5 vrf v2 sou lo1
r1 tping 100 60 3.3.3.6 vrf v2 sou lo1
r1 tping 100 60 3336::6 vrf v2 sou lo1

r2 tping 100 60 3.3.3.4 vrf v2 sou lo1
r2 tping 100 60 3334::4 vrf v2 sou lo1
r2 tping 100 60 3.3.3.5 vrf v2 sou lo1
r2 tping 100 60 3335::5 vrf v2 sou lo1
r2 tping 100 60 3.3.3.6 vrf v2 sou lo1
r2 tping 100 60 3336::6 vrf v2 sou lo1

r3 tping 100 60 3.3.3.4 vrf v2 sou lo1
r3 tping 100 60 3334::4 vrf v2 sou lo1
r3 tping 100 60 3.3.3.5 vrf v2 sou lo1
r3 tping 100 60 3335::5 vrf v2 sou lo1
r3 tping 100 60 3.3.3.6 vrf v2 sou lo1
r3 tping 100 60 3336::6 vrf v2 sou lo1
