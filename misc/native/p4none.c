#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <arpa/inet.h>

#include "p4hdr.h"

#include "utils.h"
#include "table.h"
#include "tree.h"
#include "types.h"


int ports;
int cpuport;
char *ifaceName[maxPorts];


void initIface(int port, char *name) {
}


int initTables() {
    return 0;
}


int doOneCommand(unsigned char* buf) {
    return 0;
}


void doStatRound(FILE *commands, int round) {
}


int doConsoleCommand(unsigned char*buf) {
    return 0;
}


int hashDataPacket(unsigned char *bufP) {
    return 0;
}


void processDataPacket(unsigned char *bufA, unsigned char *bufB, unsigned char *bufC, unsigned char *bufD, int bufS, int port, int prt, EVP_CIPHER_CTX *encrCtx, EVP_MD_CTX *hashCtx) {
    put16msb(bufD, preBuff - 2, port);
    sendPack(&bufD[preBuff - 2], bufS + 2, cpuport);
}


void processCpuPack(unsigned char *bufA, unsigned char *bufB, unsigned char *bufC, unsigned char* bufD, int bufS, EVP_CIPHER_CTX *encrCtx, EVP_MD_CTX *hashCtx) {
    int prt = get16msb(bufD, preBuff);
    if (prt < 0) return;
    if (prt >= ports) return;
    sendPack(&bufD[preBuff + 2], bufS - 2, prt);
}

