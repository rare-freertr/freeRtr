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

#ifndef _EGRESS_METADATA_P4_
#define _EGRESS_METADATA_P4_

struct egress_metadata_t {

    SubIntId_t target_id;
    SubIntId_t output_id;
    ethertype_t ethertype;
#ifdef HAVE_MPLS
    bit <1> mpls0_valid;
    bit <1> mpls1_valid;
#endif
    bit <1> ipv4_valid;
    bit <1> ipv6_valid;

}

#endif	// _EGRESS_METADATA_P4_
