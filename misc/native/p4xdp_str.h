#define MAX_PORTS 128
#define MAX_ROUTES4 2048
#define MAX_ROUTES6 2048
#define MAX_NEIGHS 512
#define MAX_LABELS 1024

struct port_entry {
    int idx;
    long bytes;
    long packs;
};

struct vrfp_entry {
    int cmd; // 1=route
    int vrf;
};

#define routes_bits (sizeof(__u32) * 8)

struct route4_key {
    __u32 bits;
    __u32 vrf;
    unsigned char addr[4];
};

struct route6_key {
    __u32 bits;
    __u32 vrf;
    unsigned char addr[16];
};

struct routes_res {
    int cmd; // 1=route, 2=cpu, 3=mpls1
    int hop;
    int mpls1;
    int mpls2;
};

struct neigh_key {
    int vrf;
    int id;
};

struct neigh_res {
    unsigned char dmac[6];
    unsigned char smac[6];
    int port;
};
