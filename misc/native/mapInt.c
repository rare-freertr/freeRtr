#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <poll.h>
#include <linux/if_ether.h>
#include <linux/if.h>
#include <linux/if_packet.h>
#include <sys/ioctl.h>
#include <sys/mman.h>

#include "utils.h"


#define blocksMax 64


char *ifaceName;
int ifaceIndex;
int ifaceSock;
unsigned char *ifaceMem;
struct iovec *ifaceRiv;
struct iovec *ifaceTiv;
struct pollfd ifacePfd;
struct sockaddr_in addrLoc;
struct sockaddr_in addrRem;
struct sockaddr_ll addrIfc;
int blockNxt = 0;
int portLoc;
int portRem;
int commSock;
pthread_t threadUdp;
pthread_t threadRaw;
pthread_t threadStat;
long byteRx;
long packRx;
long byteTx;
long packTx;

void err(char*buf) {
    printf("%s\n", buf);
    _exit(1);
}

void doRawLoop() {
    int bufS;
    unsigned char *bufD;
    int blockNum = 0;
    struct tpacket2_hdr *ppd;
    for (;;) {
        ppd = (struct tpacket2_hdr *) ifaceRiv[blockNum].iov_base;
        if ((ppd->tp_status & TP_STATUS_USER) == 0) {
            poll(&ifacePfd, 1, 1);
            continue;
        }
        bufS = ppd->tp_snaplen;
        bufD = (unsigned char *) ppd + ppd->tp_mac;
        if ((ppd->tp_status & TP_STATUS_VLAN_VALID) != 0) {
            if ((ppd->tp_status & TP_STATUS_VLAN_TPID_VALID) == 0) ppd->tp_vlan_tpid = ETH_P_8021Q;
            bufD -= 4;
            bufS += 4;
            memmove(bufD, bufD + 4, 12);
            put16msb(bufD, 12, ppd->tp_vlan_tpid);
            put16msb(bufD, 14, ppd->tp_vlan_tci);
        }
        packRx++;
        byteRx += bufS;
        send(commSock, bufD, bufS, 0);
        ppd->tp_status = TP_STATUS_KERNEL;
        blockNum = (blockNum + 1) % blocksMax;
    }
    err("raw thread exited");
}

void doUdpLoop() {
    unsigned char bufD[16384];
    int bufS;
    struct tpacket2_hdr *ppd;
    for (;;) {
        bufS = sizeof (bufD);
        bufS = recv(commSock, bufD, bufS, 0);
        if (bufS < 0) break;
        ppd = (struct tpacket2_hdr *) ifaceTiv[blockNxt].iov_base;
        if (ppd->tp_status != TP_STATUS_AVAILABLE) continue;
        memcpy(ifaceTiv[blockNxt].iov_base + TPACKET_ALIGN(sizeof(struct tpacket2_hdr)), bufD, bufS);
        ppd->tp_len = bufS;
        ppd->tp_status = TP_STATUS_SEND_REQUEST;
        packTx++;
        byteTx += bufS;
        blockNxt = (blockNxt + 1) % blocksMax;
        sendto(ifaceSock, NULL, 0, 0, (struct sockaddr *) &addrIfc, sizeof (addrIfc));
    }
    err("udp thread exited");
}

void doStatLoop() {
    struct ifreq ifr;
    unsigned char buf[1];
    int needed = IFF_RUNNING | IFF_UP;
    for (;;) {
        sleep(1);
        memset(&ifr, 0, sizeof (ifr));
        strcpy(ifr.ifr_name, ifaceName);
        if (ioctl(ifaceSock, SIOCGIFFLAGS, &ifr) < 0) break;
        if ((ifr.ifr_flags & needed) == needed) buf[0] = 1;
        else buf[0] = 0;
        sendto(commSock, buf, 1, 0, (struct sockaddr *) &addrRem, sizeof (addrRem));
    }
    err("stat thread exited");
}

void doMainLoop() {
    unsigned char buf[1024];

doer:
    printf("> ");
    buf[0] = 0;
    int i = scanf("%1023s", buf);
    if (i < 1) {
        sleep(1);
        goto doer;
    }
    switch (buf[0]) {
    case 0:
        goto doer;
        break;
    case 'H':
    case 'h':
    case '?':
        printf("commands:\n");
        printf("h - this help\n");
        printf("q - exit process\n");
        printf("d - display counters\n");
        printf("c - clear counters\n");
        break;
    case 'Q':
    case 'q':
        err("exiting");
        break;
    case 'D':
    case 'd':
        printf("iface counters:\n");
        printf("                      packets                bytes\n");
        printf("received %20li %20li\n", packRx, byteRx);
        printf("sent     %20li %20li\n", packTx, byteTx);
        break;
    case 'C':
    case 'c':
        printf("counters cleared.\n");
        byteRx = 0;
        packRx = 0;
        byteTx = 0;
        packTx = 0;
        break;
    default:
        printf("unknown command '%s', try ?\n", buf);
        break;
    }
    printf("\n");

    goto doer;
}

int main(int argc, char **argv) {

    if (argc < 5) {
        if (argc <= 1) goto help;
        char*curr = argv[1];
        if ((curr[0] == '-') || (curr[0] == '/')) curr++;
        switch (curr[0]) {
        case 'V':
        case 'v':
            err("memory mapped interface driver v1.0");
            break;
        case '?':
        case 'h':
        case 'H':
help :
            curr = argv[0];
            printf("using: %s <iface> <lport> <raddr> <rport> [laddr]\n", curr);
            printf("   or: %s <command>\n", curr);
            printf("commands: v=version\n");
            printf("          h=this help\n");
            _exit(1);
            break;
        default:
            err("unknown command, try -h");
            break;
        }
        _exit(1);
    }

    portLoc = atoi(argv[2]);
    portRem = atoi(argv[4]);
    memset(&addrLoc, 0, sizeof (addrLoc));
    memset(&addrRem, 0, sizeof (addrRem));
    if (inet_aton(argv[3], &addrRem.sin_addr) == 0) err("bad raddr address");
    if (argc > 5) {
        if (inet_aton(argv[5], &addrLoc.sin_addr) == 0) err("bad laddr address");
    } else {
        addrLoc.sin_addr.s_addr = htonl(INADDR_ANY);
    }
    addrLoc.sin_family = AF_INET;
    addrLoc.sin_port = htons(portLoc);
    addrRem.sin_family = AF_INET;
    addrRem.sin_port = htons(portRem);

    if ((commSock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0) err("unable to open socket");
    if (bind(commSock, (struct sockaddr *) &addrLoc, sizeof (addrLoc)) < 0) err("failed to bind socket");
    printf("binded to local port %s %i.\n", inet_ntoa(addrLoc.sin_addr), portLoc);
    if (connect(commSock, (struct sockaddr *) &addrRem, sizeof (addrRem)) < 0) err("failed to connect socket");
    printf("will send to %s %i.\n", inet_ntoa(addrRem.sin_addr), portRem);
    int sockOpt = 524288;
    setsockopt(commSock, SOL_SOCKET, SO_RCVBUF, &sockOpt, sizeof(sockOpt));
    setsockopt(commSock, SOL_SOCKET, SO_SNDBUF, &sockOpt, sizeof(sockOpt));

    ifaceName = malloc(strlen(argv[1]) + 1);
    if (ifaceName == NULL) err("error allocating memory");
    strcpy(ifaceName, argv[1]);
    printf("opening interface %s.\n", ifaceName);

    int i;
    if ((ifaceSock = socket(PF_PACKET, SOCK_RAW, htons(ETH_P_ALL))) < 0) err("unable to open socket");
    struct ifreq ifr;
    memset(&ifr, 0, sizeof (ifr));
    strcpy(ifr.ifr_name, ifaceName);
    if (ioctl(ifaceSock, SIOCGIFINDEX, &ifr) < 0) err("unable to get ifcidx");
    ifaceIndex = ifr.ifr_ifindex;
    memset(&addrIfc, 0, sizeof (addrIfc));
    addrIfc.sll_family = AF_PACKET;
    addrIfc.sll_ifindex = ifaceIndex;
    addrIfc.sll_protocol = htons(ETH_P_ALL);
    if (bind(ifaceSock, (struct sockaddr *) &addrIfc, sizeof (addrIfc)) < 0) err("failed to bind socket");
    addrIfc.sll_pkttype = PACKET_OUTGOING;
    struct packet_mreq pmr;
    memset(&pmr, 0, sizeof (pmr));
    pmr.mr_ifindex = ifaceIndex;
    pmr.mr_type = PACKET_MR_PROMISC;
    if (setsockopt(ifaceSock, SOL_PACKET, PACKET_ADD_MEMBERSHIP, &pmr, sizeof (pmr)) < 0) err("failed to set promisc");
    int ver = TPACKET_V2;
    if (setsockopt(ifaceSock, SOL_PACKET, PACKET_VERSION, &ver, sizeof (ver)) < 0) err("failed to set version");
    struct tpacket_req3 rrq;
    memset(&rrq, 0, sizeof (rrq));
    rrq.tp_block_size = 16384;
    rrq.tp_frame_size = 16384;
    rrq.tp_block_nr = blocksMax;
    rrq.tp_frame_nr = (rrq.tp_block_size * rrq.tp_block_nr) / rrq.tp_frame_size;
    rrq.tp_retire_blk_tov = 1;
    if (setsockopt(ifaceSock, SOL_PACKET, PACKET_RX_RING, &rrq, sizeof (rrq)) < 0) err("failed enable rx ring buffer");
    if (setsockopt(ifaceSock, SOL_PACKET, PACKET_TX_RING, &rrq, sizeof (rrq)) < 0) err("failed enable tx ring buffer");
    ifaceMem = mmap(NULL, (size_t)rrq.tp_block_size * rrq.tp_block_nr * 2, PROT_READ | PROT_WRITE, MAP_SHARED, ifaceSock, 0);
    if (ifaceMem == MAP_FAILED) err("failed to mmap ring buffer");
    ifaceRiv = malloc(rrq.tp_block_nr * sizeof (*ifaceRiv));
    if (ifaceRiv == NULL) err("failed to allocate rx iovec memory");
    ifaceTiv = malloc(rrq.tp_block_nr * sizeof (*ifaceRiv));
    if (ifaceTiv == NULL) err("failed to allocate tx iovec memory");
    for (i = 0; i < rrq.tp_block_nr; i++) {
        ifaceRiv[i].iov_base = ifaceMem + (i * rrq.tp_block_size);
        ifaceRiv[i].iov_len = rrq.tp_block_size;
        ifaceTiv[i].iov_base = ifaceMem + ((i + blocksMax) * rrq.tp_block_size);
        ifaceTiv[i].iov_len = rrq.tp_block_size;
    }
    memset(&ifacePfd, 0, sizeof (ifacePfd));
    ifacePfd.fd = ifaceSock;
    ifacePfd.events = POLLIN | POLLERR;

    setgid(1);
    setuid(1);
    printf("serving others\n");

    byteRx = 0;
    packRx = 0;
    byteTx = 0;
    packTx = 0;
    if (pthread_create(&threadRaw, NULL, (void*) & doRawLoop, NULL)) err("error creating raw thread");
    if (pthread_create(&threadUdp, NULL, (void*) & doUdpLoop, NULL)) err("error creating udp thread");
    if (pthread_create(&threadStat, NULL, (void*) & doStatLoop, NULL)) err("error creating stat thread");

    doMainLoop();
}
