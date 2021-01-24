/*
 * Copyright 2019-present GÉANT RARE project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed On an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


#ifndef _TYPES_P4_
#define _TYPES_P4_


#undef NEED_UDP2

#ifdef HAVE_L2TP
#define NEED_UDP2
#endif

#ifdef HAVE_VXLAN
#define NEED_UDP2
#endif

#ifdef HAVE_PCKOUDP
#define NEED_UDP2
#endif



#undef NEED_ETH4

#ifdef HAVE_TAP
#define NEED_ETH4
#endif

#ifdef HAVE_VXLAN
#define NEED_ETH4
#endif



typedef bit <16> ethertype_t;
typedef bit <48> mac_addr_t;
#ifdef HAVE_MPLS
typedef bit <20> label_t;
#endif
typedef bit <32> ipv4_addr_t;
typedef bit <128> ipv6_addr_t;
typedef bit<16> layer4_port_t;
typedef bit <12> vlan_id_t;
typedef bit <16> switch_vrf_t;
//typedef bit<9> PortId_t;
typedef bit<16> NextHopId_t;
typedef bit<9> SubIntId_t;
#define MAX_PORT 511


#endif // _TYPES_P4_
