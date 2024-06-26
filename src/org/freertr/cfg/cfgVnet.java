package org.freertr.cfg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.freertr.ifc.ifcUdpInt;
import org.freertr.pipe.pipeConnect;
import org.freertr.pipe.pipeDiscard;
import org.freertr.pipe.pipeLine;
import org.freertr.pipe.pipeShell;
import org.freertr.pipe.pipeSide;
import org.freertr.tab.tabGen;
import org.freertr.tab.tabRouteIface;
import org.freertr.user.userFilter;
import org.freertr.user.userFlash;
import org.freertr.user.userHelping;
import org.freertr.user.userHwdet;
import org.freertr.util.bits;
import org.freertr.util.cmds;
import org.freertr.util.logBuf;
import org.freertr.util.logger;
import org.freertr.util.version;

/**
 * virtual ethernet
 *
 * @author matecsaba
 */
public class cfgVnet implements Comparator<cfgVnet>, cfgGeneric {

    /**
     * number of this bridge
     */
    public final int number;

    /**
     * hidden process
     */
    protected boolean hidden = false;

    /**
     * description of this bridge
     */
    public String description;

    /**
     * port number to use
     */
    protected int port;

    /**
     * side one
     */
    public final cfgVnetSide side1 = new cfgVnetSide(this, 1);

    /**
     * side two
     */
    public final cfgVnetSide side2 = new cfgVnetSide(this, 2);

    /**
     * defaults text
     */
    public final static String[] defaultL = {
        "vnet .*! no description",
        "vnet .*! no side1 type",
        "vnet .*! no side1 local",
        "vnet .*! no side1 connect",
        "vnet .*! no side1 log-actions",
        "vnet .*! no side1 log-console",
        "vnet .*! no side1 log-collect",
        "vnet .*! side1 time 1000",
        "vnet .*! side1 delay 1000",
        "vnet .*! side1 random-time 0",
        "vnet .*! side1 random-delay 0",
        "vnet .*! no side2 type",
        "vnet .*! no side2 local",
        "vnet .*! no side2 connect",
        "vnet .*! no side2 log-actions",
        "vnet .*! no side2 log-console",
        "vnet .*! no side2 log-collect",
        "vnet .*! side2 time 1000",
        "vnet .*! side2 delay 1000",
        "vnet .*! side2 random-time 0",
        "vnet .*! side2 random-delay 0",};

    /**
     * defaults filter
     */
    public static tabGen<userFilter> defaultF;

    public int compare(cfgVnet o1, cfgVnet o2) {
        if (o1.number < o2.number) {
            return -1;
        }
        if (o1.number > o2.number) {
            return +1;
        }
        return 0;
    }

    /**
     * copy bytes
     *
     * @return copy
     */
    public cfgVnet copyBytes() {
        cfgVnet n = new cfgVnet("" + number);
        n.description = description;
        side1.copyBytes(n.side1);
        side2.copyBytes(n.side2);
        return n;
    }

    public String toString() {
        return "vnet " + number;
    }

    /**
     * create new bridge instance
     *
     * @param nam name of bridge
     */
    public cfgVnet(String nam) {
        number = bits.str2num(nam);
    }

    private void getHelp(userHelping l, int s, String n) {
        l.add(null, "1 2     side" + s + "                       configure side " + n);
        l.add(null, "2 3       type                      type of process");
        l.add(null, "3 .         socat                   use socat");
        l.add(null, "3 .         pcap                    use pcapint");
        l.add(null, "3 .         raw                     use rawint");
        l.add(null, "3 .         map                     use mapint");
        l.add(null, "2 3       local                     name of local interface");
        l.add(null, "3 .         <str>                   name");
        l.add(null, "2 3       connect                   name of connected interface");
        l.add(null, "3 .         <str>                   name");
        l.add(null, "2 .       log-actions               log actions");
        l.add(null, "2 .       log-console               log console activity");
        l.add(null, "2 3       log-collect               collect console activity");
        l.add(null, "3 .         <num>                   lines to store");
        l.add(null, "2 3       time                      specify time between runs");
        l.add(null, "3 .         <num>                   milliseconds between runs");
        l.add(null, "2 3       delay                     specify initial delay");
        l.add(null, "3 .         <num>                   milliseconds before start");
        l.add(null, "2 3       random-time               specify random time between runs");
        l.add(null, "3 .         <num>                   milliseconds between runs");
        l.add(null, "2 3       random-delay              specify random initial delay");
        l.add(null, "3 .         <num>                   milliseconds before start");
    }

    public void getHelp(userHelping l) {
        l.add(null, "1 2,.   description                 description of this bridge");
        l.add(null, "2 2,.     [text]                    text describing this bridge");
        getHelp(l, 1, "one");
        getHelp(l, 2, "two");
    }

    public List<String> getShRun(int filter) {
        List<String> l = new ArrayList<String>();
        l.add("vnet " + number);
        if (hidden) {
            return l;
        }
        cmds.cfgLine(l, description == null, cmds.tabulator, "description", description);
        side1.getShRun(l, cmds.tabulator);
        side2.getShRun(l, cmds.tabulator);
        l.add(cmds.tabulator + cmds.finish);
        l.add(cmds.comment);
        if ((filter & 1) == 0) {
            return l;
        }
        return userFilter.filterText(l, defaultF);
    }

    public void doCfgStr(cmds cmd) {
        String a = cmd.word();
        if (a.equals("description")) {
            description = cmd.getRemaining();
            return;
        }
        if (a.equals("side1")) {
            side1.doCfgStr(cmd);
            return;
        }
        if (a.equals("side2")) {
            side2.doCfgStr(cmd);
            return;
        }
        if (!a.equals(cmds.negated)) {
            cmd.badCmd();
            return;
        }
        a = cmd.word();
        if (a.equals("description")) {
            description = null;
            return;
        }
        if (a.equals("side1")) {
            side1.doUnCfg(cmd);
            return;
        }
        if (a.equals("side2")) {
            side2.doUnCfg(cmd);
            return;
        }
        cmd.badCmd();
    }

    public String getPrompt() {
        return "vnet";
    }

    /**
     * stop work
     */
    public void stopNow() {
        side1.stopNow();
        side2.stopNow();
    }

    /**
     * start work
     *
     * @param prt port to use
     */
    public void startNow(int prt) {
        port = prt;
        List<String> lst = bits.str2lst(userHwdet.scrBeg);
        userHwdet.setupVeth(lst, side1.getOSname(), side2.getOSname());
        userHwdet.setupIface(lst, side1.getOSname(), 8192);
        userHwdet.setupIface(lst, side2.getOSname(), 8192);
        String a = version.getRWpath() + "veth" + bits.randomD() + ".tmp";
        if (bits.buf2txt(true, lst, a)) {
            return;
        }
        userFlash.setFilePerm(a, true, false, true, true, false, true);
        pipeShell.exec(a, null, true, true, true);
        userFlash.delete(a);
        side1.startNow(prt + 0, prt + 1);
        side2.startNow(prt + 2, prt + 3);
    }

}

class cfgVnetSide implements Runnable {

    public final cfgVnet parent;

    public final int sideId;

    public userHwdet.ifcTyp ifcTyp;

    public tabRouteIface.ifaceType locTyp;

    public String locNam;

    public String conNam;

    public int prtLoc;

    public int prtRem;

    public boolean need2run;

    protected int interval = 1000;

    protected int initial = 1000;

    public int randInt;

    public int randIni;

    public boolean logAct = false;

    public boolean logCon = false;

    public logBuf logCol;

    public pipeSide cons;

    private pipeShell proc;

    private pipeSide pipe;

    public int restartC;

    public long restartT;

    public cfgVnetSide(cfgVnet p, int i) {
        parent = p;
        sideId = i;
    }

    public void copyBytes(cfgVnetSide n) {
        n.ifcTyp = ifcTyp;
        n.locTyp = locTyp;
        n.locNam = locNam;
        n.initial = initial;
        n.interval = interval;
        n.randIni = randIni;
        n.randInt = randInt;
        n.logAct = logAct;
        n.logCon = logCon;
        n.logCol = logCol;
        n.prtLoc = prtLoc;
        n.prtRem = prtRem;
    }

    public void getShRun(List<String> lst, String beg1) {
        String beg2 = "side" + sideId;
        cmds.cfgLine(lst, ifcTyp == null, beg1, beg2 + " type", "" + ifcTyp);
        cmds.cfgLine(lst, locNam == null, beg1, beg2 + " local", locNam);
        cmds.cfgLine(lst, conNam == null, beg1, beg2 + " connect", conNam);
        cmds.cfgLine(lst, !logAct, beg1, beg2 + " log-actions", "");
        cmds.cfgLine(lst, !logCon, beg1, beg2 + " log-console", "");
        cmds.cfgLine(lst, logCol == null, beg1, beg2 + " log-collect", "" + logBuf.getSize(logCol));
        lst.add(beg1 + beg2 + " delay " + initial);
        lst.add(beg1 + beg2 + " time " + interval);
        lst.add(beg1 + beg2 + " random-time " + randInt);
        lst.add(beg1 + beg2 + " random-delay " + randIni);
    }

    public void doCfgStr(cmds cmd) {
        String a = cmd.word();
        if (a.equals("delay")) {
            initial = bits.str2num(cmd.word());
            return;
        }
        if (a.equals("time")) {
            interval = bits.str2num(cmd.word());
            return;
        }
        if (a.equals("random-time")) {
            randInt = bits.str2num(cmd.word());
            return;
        }
        if (a.equals("random-delay")) {
            randIni = bits.str2num(cmd.word());
            return;
        }
        if (a.equals("log-actions")) {
            logAct = true;
            return;
        }
        if (a.equals("log-collect")) {
            logCol = new logBuf(bits.str2num(cmd.word()));
            return;
        }
        if (a.equals("log-console")) {
            logCon = true;
            return;
        }
        if (a.equals("local")) {
            a = cfgIfc.dissectName(cmd.word())[0];
            locTyp = cfgIfc.string2type(a);
            if (locTyp == null) {
                cmd.error("bad name");
                return;
            }
            if (cfgAll.ifcFind(a, 0) != null) {
                cmd.error("interface already exists");
                return;
            }
            locNam = a;
            return;
        }
        if (a.equals("connect")) {
            conNam = cmd.word();
            return;
        }
        if (a.equals("type")) {
            a = cmd.word();
            ifcTyp = userHwdet.string2type(a);
            return;
        }
        cmd.badCmd();
    }

    public void doUnCfg(cmds cmd) {
        String a = cmd.word();
        if (a.equals("random-time")) {
            randInt = 0;
            return;
        }
        if (a.equals("random-delay")) {
            randIni = 0;
            return;
        }
        if (a.equals("log-actions")) {
            logAct = false;
            return;
        }
        if (a.equals("log-collect")) {
            logCol = null;
            return;
        }
        if (a.equals("log-console")) {
            logCon = false;
            return;
        }
        if (a.equals("local")) {
            locNam = null;
            locTyp = null;
            return;
        }
        if (a.equals("connect")) {
            conNam = null;
            return;
        }
        if (a.equals("type")) {
            ifcTyp = null;
            return;
        }
        cmd.badCmd();
    }

    public void startNow(int pl, int pr) {
        prtLoc = pl;
        prtRem = pr;
        if (!cfgInit.booting) {
            return;
        }
        if (locNam != null) {
            ifcUdpInt hdr = new ifcUdpInt("127.0.0.1", pl, "127.0.0.1", pr, "-", locTyp != tabRouteIface.ifaceType.ether, false);
            cfgIfc ifc = cfgAll.ifcAdd(locNam, locTyp, hdr, 1);
            if (ifc == null) {
                return;
            }
            ifc.initPhysical();
        }
        if (ifcTyp == null) {
            return;
        }
        need2run = true;
        new Thread(this).start();
    }

    public void stopNow() {
        need2run = false;
        restartNow();
    }

    public void restartNow() {
        try {
            proc.kill();
        } catch (Exception e) {
        }
        try {
            pipe.setClose();
        } catch (Exception e) {
        }
    }

    public void run() {
        for (;;) {
            if (!cfgInit.booting) {
                break;
            }
            bits.sleep(1000);
        }
        int del = initial;
        if (randIni > 0) {
            del += bits.random(1, randIni);
        }
        if (del > 0) {
            bits.sleep(del);
        }
        for (;;) {
            bits.sleep(interval);
            if (!need2run) {
                break;
            }
            try {
                doRound();
            } catch (Exception e) {
                logger.traceback(e);
            }
        }
    }

    public String getOSname() {
        return "exthrpin" + parent.number + (sideId == 1 ? "a" : "b");
    }

    private synchronized boolean doRound() {
        String a = null;
        if (locNam != null) {
            a = userHwdet.interface2command("./", ifcTyp, getOSname(), prtLoc, prtRem);
        }
        if (conNam != null) {
            a = userHwdet.connection2command("./", ifcTyp, conNam, getOSname());
        }
        if (a == null) {
            return true;
        }
        if (logAct) {
            logger.info("restarting process vnet " + parent.number + " " + sideId);
        }
        if (randInt > 0) {
            bits.sleep(bits.random(1, randInt));
        }
        restartT = bits.getTime();
        restartC++;
        pipeLine pl = new pipeLine(65536, false);
        pipe = pl.getSide();
        pipe.lineTx = pipeSide.modTyp.modeCRLF;
        pipe.lineRx = pipeSide.modTyp.modeCRorLF;
        proc = pipeShell.exec(pl.getSide(), a, null, true, true, false, true);
        if (proc == null) {
            return false;
        }
        for (;;) {
            if (!proc.isRunning()) {
                break;
            }
            if (cons == null) {
                pipeDiscard.logLines("vnet " + parent.number + " " + sideId + " said ", pipe, logCon, logCol);
                bits.sleep(1000);
                continue;
            }
            boolean b = pipeConnect.redirect(pipe, cons);
            b |= pipeConnect.redirect(cons, pipe);
            if (b) {
                cons.setClose();
                cons = null;
            }
            bits.sleep(100);
        }
        return false;
    }

}
