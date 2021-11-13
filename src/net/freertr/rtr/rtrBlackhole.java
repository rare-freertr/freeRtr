package net.freertr.rtr;

import java.util.List;
import net.freertr.addr.addrIP;
import net.freertr.addr.addrIPv4;
import net.freertr.addr.addrPrefix;
import net.freertr.cfg.cfgAll;
import net.freertr.cfg.cfgPrfxlst;
import net.freertr.ip.ipCor4;
import net.freertr.ip.ipCor6;
import net.freertr.ip.ipFwd;
import net.freertr.ip.ipRtr;
import net.freertr.tab.tabGen;
import net.freertr.tab.tabIndex;
import net.freertr.tab.tabListing;
import net.freertr.tab.tabPrfxlstN;
import net.freertr.tab.tabRoute;
import net.freertr.tab.tabRouteAttr;
import net.freertr.tab.tabRouteEntry;
import net.freertr.user.userHelping;
import net.freertr.util.bits;
import net.freertr.util.cmds;
import net.freertr.util.logger;

/**
 * blackhole generator
 *
 * @author matecsaba
 */
public class rtrBlackhole extends ipRtr implements Runnable {

    /**
     * the forwarder protocol
     */
    public final ipFwd fwdCore;

    /**
     * route type
     */
    public final tabRouteAttr.routeType rouTyp;

    /**
     * router number
     */
    public final int rtrNum;

    /**
     * protocol version
     */
    public final int proto;

    /**
     * distance to give
     */
    protected int distance;

    /**
     * negate operation
     */
    protected boolean negate;

    private tabListing<tabPrfxlstN, addrIP> whitelist;

    private int penalty = 60 * 1000;

    private final tabRoute<addrIP> entries = new tabRoute<addrIP>("ntry");

    private boolean working = true;

    /**
     * create blackhole process
     *
     * @param forwarder forwarder to update
     * @param id process id
     */
    public rtrBlackhole(ipFwd forwarder, int id) {
        fwdCore = forwarder;
        rtrNum = id;
        switch (fwdCore.ipVersion) {
            case ipCor4.protocolVersion:
                rouTyp = tabRouteAttr.routeType.blackhole4;
                proto = 4;
                break;
            case ipCor6.protocolVersion:
                rouTyp = tabRouteAttr.routeType.blackhole6;
                proto = 6;
                break;
            default:
                rouTyp = null;
                proto = 0;
                break;
        }
        distance = 254;
        routerComputedU = new tabRoute<addrIP>("rx");
        routerComputedM = new tabRoute<addrIP>("rx");
        routerComputedF = new tabRoute<addrIP>("rx");
        routerComputedI = new tabGen<tabIndex<addrIP>>();
        routerCreateComputed();
        fwdCore.routerAdd(this, rouTyp, id);
        new Thread(this).start();
    }

    /**
     * convert to string
     *
     * @return string
     */
    public String toString() {
        return "blackhole on " + fwdCore;
    }

    /**
     * create computed
     */
    public synchronized void routerCreateComputed() {
        tabRoute<addrIP> res = new tabRoute<addrIP>("computed");
        res.mergeFrom(tabRoute.addType.better, entries, tabRouteAttr.distanLim);
        routerDoAggregates(rtrBgpUtil.sfiUnicast, res, res, fwdCore.commonLabel, null, 0);
        res.preserveTime(routerComputedU);
        routerComputedU = res;
        fwdCore.routerChg(this);
    }

    /**
     * redistribution changed
     */
    public void routerRedistChanged() {
        routerCreateComputed();
    }

    /**
     * others changed
     */
    public void routerOthersChanged() {
    }

    /**
     * get help
     *
     * @param l list
     */
    public void routerGetHelp(userHelping l) {
        l.add(null, "1  .      negate                     negate operation on remote routes");
        l.add(null, "1  2      distance                   specify default distance");
        l.add(null, "2  .        <num>                    distance");
        l.add(null, "1  2      penalty                    specify time between runs");
        l.add(null, "2  .        <num>                    milliseconds before aging");
        l.add(null, "1  2      whitelist                  specify whitelist");
        l.add(null, "2  .        <name:pl>                name of prefix list");
    }

    /**
     * get config
     *
     * @param l list
     * @param beg beginning
     * @param filter filter
     */
    public void routerGetConfig(List<String> l, String beg, int filter) {
        cmds.cfgLine(l, !negate, beg, "negate", "");
        l.add(beg + "distance " + distance);
        l.add(beg + "penalty " + penalty);
        if (whitelist == null) {
            l.add(beg + "no whitelist");
        } else {
            l.add(beg + "whitelist " + whitelist.listName);
        }
    }

    /**
     * configure
     *
     * @param cmd command
     * @return false if success, true if error
     */
    public boolean routerConfigure(cmds cmd) {
        String s = cmd.word();
        boolean negated = false;
        if (s.equals("no")) {
            s = cmd.word();
            negated = true;
        }
        if (s.equals("whitelist")) {
            if (negated) {
                whitelist = null;
                return false;
            }
            cfgPrfxlst ntry = cfgAll.prfxFind(cmd.word(), false);
            if (ntry == null) {
                cmd.error("no such list");
                return false;
            }
            whitelist = ntry.prflst;
            return false;
        }
        if (s.equals("penalty")) {
            penalty = bits.str2num(cmd.word());
            return false;
        }
        if (s.equals("negate")) {
            negate = !negated;
            return false;
        }
        if (s.equals("distance")) {
            distance = bits.str2num(cmd.word());
            return false;
        }
        return true;
    }

    /**
     * stop work
     */
    public void routerCloseNow() {
        working = false;
    }

    /**
     * get neighbor count
     *
     * @return count
     */
    public int routerNeighCount() {
        return 0;
    }

    /**
     * neighbor list
     *
     * @param tab list
     */
    public void routerNeighList(tabRoute<addrIP> tab) {
    }

    /**
     * get interface count
     *
     * @return count
     */
    public int routerIfaceCount() {
        return 0;
    }

    /**
     * maximum recursion depth
     *
     * @return allowed number
     */
    public int routerRecursions() {
        return 1;
    }

    /**
     * get list of link states
     *
     * @param tab table to update
     * @param par parameter
     * @param asn asn
     * @param adv advertiser
     */
    public void routerLinkStates(tabRoute<addrIP> tab, int par, int asn, addrIPv4 adv) {
    }

    private void doRound() {
        long tim = bits.getTime() - penalty;
        int del = 0;
        for (int i = entries.size() - 1; i >= 0; i--) {
            tabRouteEntry<addrIP> ntry = entries.get(i);
            if (ntry == null) {
                continue;
            }
            if (ntry.best.time > tim) {
                continue;
            }
            entries.del(ntry);
            del++;
        }
        if (del < 1) {
            return;
        }
        routerCreateComputed();
    }

    public void run() {
        for (;;) {
            if (!working) {
                return;
            }
            bits.sleep(penalty / 2);
            try {
                doRound();
            } catch (Exception e) {
                logger.traceback(e);
            }
        }
    }

    private boolean isWhitelisted(addrIP adr) {
        if (whitelist == null) {
            return false;
        }
        return whitelist.matches(rtrBgpUtil.sfiUnicast, 0, new addrPrefix<addrIP>(adr, adr.maxBits()));
    }

    /**
     * check one address
     *
     * @param adr address
     * @return true if blocked, false if allowed
     */
    public boolean checkAddr(addrIP adr) {
        if (isWhitelisted(adr)) {
            return false;
        }
        tabRouteEntry<addrIP> ntry = entries.route(adr);
        if (ntry != null) {
            ntry.best.time = bits.getTime();
            return true;
        }
        ntry = fwdCore.actualU.route(adr);
        if (negate) {
            return ntry == null;
        }
        return ntry != null;
    }

    /**
     * block one address
     *
     * @param adr address
     */
    public void blockAddr(addrIP adr) {
        if (isWhitelisted(adr)) {
            return;
        }
        addrPrefix<addrIP> prf;
        if (proto == 4) {
            prf = new addrPrefix<addrIP>(adr, cfgAll.accessSubnet4);
        } else {
            prf = new addrPrefix<addrIP>(adr, cfgAll.accessSubnet6);
        }
        tabRouteEntry<addrIP> ntry = new tabRouteEntry<addrIP>();
        ntry.prefix = prf;
        ntry.best.time = bits.getTime();
        ntry.best.rouTyp = rouTyp;
        ntry.best.protoNum = rtrNum;
        ntry.best.distance = distance;
        entries.add(tabRoute.addType.always, ntry, false, false);
        routerCreateComputed();
    }

}
