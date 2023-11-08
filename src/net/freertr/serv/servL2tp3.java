package net.freertr.serv;

import java.util.List;
import net.freertr.addr.addrIP;
import net.freertr.auth.authLocal;
import net.freertr.cfg.cfgAll;
import net.freertr.cfg.cfgBrdg;
import net.freertr.cfg.cfgIfc;
import net.freertr.ip.ipFwdIface;
import net.freertr.ip.ipPrt;
import net.freertr.pack.packHolder;
import net.freertr.pack.packL2tp3;
import net.freertr.pipe.pipeSide;
import net.freertr.prt.prtGenConn;
import net.freertr.tab.tabGen;
import net.freertr.tab.tabRouteIface;
import net.freertr.user.userFilter;
import net.freertr.user.userFormat;
import net.freertr.user.userHelping;
import net.freertr.util.bits;
import net.freertr.util.cmds;
import net.freertr.util.counter;
import net.freertr.util.state;

/**
 * layer two tunneling protocol v3 (rfc3931) server
 *
 * @author matecsaba
 */
public class servL2tp3 extends servGeneric implements ipPrt {

    /**
     * create instance
     */
    public servL2tp3() {
    }

    /**
     * interface to use
     */
    public cfgIfc dialIfc;

    /**
     * interface to use
     */
    public cfgBrdg brdgIfc;

    /**
     * physical interface
     */
    public boolean physInt = false;

    /**
     * password
     */
    public String password;

    /**
     * sending ttl value, -1 means maps out
     */
    public int sendingTTL = 255;

    /**
     * sending tos value, -1 means maps out
     */
    public int sendingTOS = -1;

    /**
     * sending df value, -1 means maps out
     */
    public int sendingDFN = -1;

    /**
     * sending flow value, -1 means maps out
     */
    public int sendingFLW = -1;

    /**
     * ticks after send a hello
     */
    public int helloTicks = 5;

    /**
     * ticks after give up retry
     */
    public int retryTicks = 8;

    /**
     * list of connections
     */
    public tabGen<servL2tp3conn> conns = new tabGen<servL2tp3conn>();

    /**
     * counter
     */
    public counter cntr;

    /**
     * defaults text
     */
    public final static String[] defaultL = {
        "server l2tp3 .*! port " + packL2tp3.prot,
        "server l2tp3 .*! protocol " + proto2string(protoAllDgrm),
        "server l2tp3 .*! timer 5 8",
        "server l2tp3 .*! no physical-interface",
        "server l2tp3 .*! no password"
    };

    /**
     * defaults filter
     */
    public static tabGen<userFilter> defaultF;

    public tabGen<userFilter> srvDefFlt() {
        return defaultF;
    }

    public void srvShRun(String beg, List<String> l, int filter) {
        l.add(beg + "timer " + helloTicks + " " + retryTicks);
        if (dialIfc == null) {
            l.add(beg + "no clone");
        } else {
            l.add(beg + "clone " + dialIfc.name);
        }
        if (brdgIfc == null) {
            l.add(beg + "no bridge");
        } else {
            l.add(beg + "bridge " + brdgIfc.name);
        }
        cmds.cfgLine(l, !physInt, beg, "physical-interface", "");
        cmds.cfgLine(l, password == null, beg, "password", authLocal.passwdEncode(password, (filter & 2) != 0));
    }

    public boolean srvCfgStr(cmds cmd) {
        String s = cmd.word();
        if (s.equals("timer")) {
            helloTicks = bits.str2num(cmd.word());
            retryTicks = bits.str2num(cmd.word());
            return false;
        }
        if (s.equals("clone")) {
            dialIfc = cfgAll.ifcFind(cmd.word(), 0);
            if (dialIfc == null) {
                cmd.error("no such interface");
                return false;
            }
            if (dialIfc.type != tabRouteIface.ifaceType.dialer) {
                cmd.error("not dialer interface");
                dialIfc = null;
                return false;
            }
            return false;
        }
        if (s.equals("bridge")) {
            brdgIfc = cfgAll.brdgFind(cmd.word(), false);
            if (brdgIfc == null) {
                cmd.error("no such bridge group");
                return false;
            }
            return false;
        }
        if (s.equals("password")) {
            password = authLocal.passwdDecode(cmd.getRemaining());
            return false;
        }
        if (s.equals("physical-interface")) {
            physInt = true;
            return false;
        }
        if (!s.equals("no")) {
            return true;
        }
        s = cmd.word();
        if (s.equals("clone")) {
            dialIfc = null;
            return false;
        }
        if (s.equals("bridge")) {
            brdgIfc = null;
            return false;
        }
        if (s.equals("password")) {
            password = null;
            return false;
        }
        if (s.equals("physical-interface")) {
            physInt = false;
            return false;
        }
        return true;
    }

    public void srvHelp(userHelping l) {
        l.add(null, "1 2  timer                        set timers");
        l.add(null, "2 3    <num>                      hello ticks");
        l.add(null, "3 .      <num>                    retry ticks");
        l.add(null, "1 2  clone                        set interface to clone");
        l.add(null, "2 .    <name:ifc>                 name of interface");
        l.add(null, "1 2  bridge                       set interface to clone");
        l.add(null, "2 .    <num>                      number of bridge");
        l.add(null, "1 2  password                     set password");
        l.add(null, "2 .    <str>                      password");
        l.add(null, "1 .  physical-interface           adding as physical to bridge");
    }

    public String srvName() {
        return "l2tp3";
    }

    public int srvPort() {
        return packL2tp3.prot;
    }

    public int srvProto() {
        return protoAllDgrm;
    }

    public boolean srvInit() {
        return genRawStart(this, 0);
    }

    public boolean srvDeinit() {
        return genRawStop(this, 0);
    }

    public boolean srvAccept(pipeSide pipe, prtGenConn id) {
        return true;
    }

    /**
     * get protocol number
     *
     * @return number
     */
    public int getProtoNum() {
        return packL2tp3.prot;
    }

    public String toString() {
        return "l2tp3";
    }

    /**
     * close interface
     *
     * @param iface interface
     */
    public void closeUp(ipFwdIface iface) {
    }

    /**
     * set state
     *
     * @param iface interface
     * @param stat state
     */
    public void setState(ipFwdIface iface, state.states stat) {
    }

    /**
     * received packet
     *
     * @param rxIfc interface
     * @param pck packet
     */
    public void recvPack(ipFwdIface rxIfc, packHolder pck) {
        servL2tp3conn ntry = new servL2tp3conn(rxIfc, pck.IPsrc, this);
        servL2tp3conn old = conns.add(ntry);
        if (old != null) {
            old.doRecv(pck);
            return;
        }
        ntry.doStartup();
        ntry.doRecv(pck);
    }

    /**
     * alert packet
     *
     * @param rxIfc interface
     * @param pck packet
     * @return false on success, true on error
     */
    public boolean alertPack(ipFwdIface rxIfc, packHolder pck) {
        return true;
    }

    /**
     * error packet
     *
     * @param err error code
     * @param rtr address
     * @param rxIfc interface
     * @param pck packet
     */
    public void errorPack(counter.reasons err, addrIP rtr, ipFwdIface rxIfc, packHolder pck) {
    }

    /**
     * get counter
     *
     * @return counter
     */
    public counter getCounter() {
        return cntr;
    }

    /**
     * get show
     *
     * @return result
     */
    public userFormat getShow() {
        userFormat res = new userFormat("|", "addr|conloc|conrem|sess|for|since");
        for (int i = 0; i < conns.size(); i++) {
            servL2tp3conn ntry = conns.get(i);
            if (ntry == null) {
                continue;
            }
            res.add(ntry.peer + "|" + ntry.conLoc + "|" + ntry.conRem + "|" + ntry.session.size() + "|" + bits.timePast(ntry.created) + "|" + bits.time2str(cfgAll.timeZoneName, ntry.created + cfgAll.timeServerOffset, 3));
        }
        return res;
    }

}
