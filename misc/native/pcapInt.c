#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pcap.h>



char *ifaceName;
pcap_t *ifacePcap;
struct sockaddr_in addrLoc;
struct sockaddr_in addrRem;
int portLoc;
int portRem;
int commSock;
pthread_t threadUdp;
pthread_t threadRaw;
pthread_t threadPrint;
long int byteRx;
long int packRx;
long int byteTx;
long int packTx;

void err(char*buf) {
    printf("%s\n", buf);
    exit(1);
}

void doRawLoop() {
    struct pcap_pkthdr head;
    int fail = 0;
    int len;
    const unsigned char *dat;
    for (;;) {
        if (fail++ > 1024) break;
        dat = pcap_next(ifacePcap, &head);
        if (dat == NULL) continue;
        len = head.caplen;
        packRx++;
        byteRx += len;
        send(commSock, dat, len, 0);
        fail = 0;
    }
    err("raw thread exited");
}

void doUdpLoop() {
    unsigned char bufD[16384];
    int bufS;
    for (;;) {
        bufS = sizeof (bufD);
        bufS = recv(commSock, bufD, bufS, 0);
        if (bufS < 0) break;
        packTx++;
        byteTx += bufS;
        pcap_sendpacket(ifacePcap, bufD, bufS);
    }
    err("udp thread exited");
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
    char errbuf[PCAP_ERRBUF_SIZE + 1];

    if (argc < 5) {
        if (argc <= 1) goto help;
        char*curr = argv[1];
        pcap_if_t *pcifcs;
        if ((curr[0] == '-') || (curr[0] == '/')) curr++;
        switch (curr[0]) {
        case 'V':
        case 'v':
            err("pcap interface driver v1.0");
            break;
        case 'L':
        case 'l':
            if (pcap_findalldevs(&pcifcs, errbuf) == -1) err("unable to find all interfaces");
            printf("list of interfaces:\n");
            int i = 0;
            for (; pcifcs; pcifcs = pcifcs->next) {
                i++;
                printf("interface #%i:\n", i);
                printf("  name: %s\n", pcifcs->name);
                if (pcifcs->description != NULL) printf("  desc: %s\n", pcifcs->description);
            }
            if (i < 1) err("no interfaces found");
            break;
        case '?':
        case 'h':
        case 'H':
help :
            curr = argv[0];
            printf("using: %s <iface> <lport> <raddr> <rport> [laddr]\n", curr);
            printf("   or: %s <command>\n", curr);
            printf("commands: l=list interfaces\n");
            printf("          v=version\n");
            printf("          h=this help\n");
            exit(1);
            break;
        default:
            err("unknown command, try -h");
            break;
        }
        exit(1);
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
    if (setsockopt(commSock, SOL_SOCKET, SO_RCVBUF, &sockOpt, sizeof(sockOpt)) < 0) err("failed to set socket rxbuf");
    if (setsockopt(commSock, SOL_SOCKET, SO_SNDBUF, &sockOpt, sizeof(sockOpt)) < 0) err("failed to set socket txbuf");

    printf("pcap version: %s\n", pcap_lib_version());

    ifaceName = malloc(strlen(argv[1]) + 1);
    if (ifaceName == NULL) err("error allocating memory");
    strcpy(ifaceName, argv[1]);
    printf("opening interface %s\n", ifaceName);

    ifacePcap = pcap_create(ifaceName, errbuf);
    if (ifacePcap == NULL) err("unable to open interface");
    if (pcap_set_snaplen(ifacePcap, 65536) < 0) err("unable to set snaplen");
    if (pcap_set_promisc(ifacePcap, 1) < 0) err("unable to set promisc");
    if (pcap_set_immediate_mode(ifacePcap, 1) < 0) err("unable to set immediate");
    if (pcap_activate(ifacePcap) < 0) err("activation failed");
    if (pcap_setdirection(ifacePcap, PCAP_D_IN) < 0) err("unable to set direction");

    printf("serving others\n");

    byteRx = 0;
    packRx = 0;
    byteTx = 0;
    packTx = 0;
    if (pthread_create(&threadRaw, NULL, (void*) & doRawLoop, NULL)) err("error creating raw thread");
    if (pthread_create(&threadUdp, NULL, (void*) & doUdpLoop, NULL)) err("error creating udp thread");

    doMainLoop();
}
