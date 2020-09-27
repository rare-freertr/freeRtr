package rtr;

import addr.addrIP;
import ip.ipFwd;
import ip.ipMpls;
import java.util.ArrayList;
import java.util.List;
import tab.tabLabel;
import tab.tabLabelBier;
import tab.tabLabelNtry;
import tab.tabRoute;
import tab.tabRouteAttr;
import tab.tabRouteEntry;

/**
 * bgp4 update group
 *
 * @author matecsaba
 */
public class rtrBgpGroup extends rtrBgpParam {

    /**
     * group number
     */
    public final int groupNum;

    /**
     * minimum version
     */
    public int minversion;

    /**
     * willing unicast prefixes
     */
    public tabRoute<addrIP> wilUni = new tabRoute<addrIP>("tx");

    /**
     * willing multicast prefixes
     */
    public tabRoute<addrIP> wilMlt = new tabRoute<addrIP>("tx");

    /**
     * willing other prefixes
     */
    public tabRoute<addrIP> wilOtr = new tabRoute<addrIP>("tx");

    /**
     * willing flowspec prefixes
     */
    public tabRoute<addrIP> wilFlw = new tabRoute<addrIP>("tx");

    /**
     * willing vpnuni prefixes
     */
    public tabRoute<addrIP> wilVpnU = new tabRoute<addrIP>("tx");

    /**
     * willing vpnmulti prefixes
     */
    public tabRoute<addrIP> wilVpnM = new tabRoute<addrIP>("tx");

    /**
     * willing vpnflow prefixes
     */
    public tabRoute<addrIP> wilVpnF = new tabRoute<addrIP>("tx");

    /**
     * willing other vpnuni prefixes
     */
    public tabRoute<addrIP> wilVpoU = new tabRoute<addrIP>("tx");

    /**
     * willing other vpnmulti prefixes
     */
    public tabRoute<addrIP> wilVpoM = new tabRoute<addrIP>("tx");

    /**
     * willing other vpnflow prefixes
     */
    public tabRoute<addrIP> wilVpoF = new tabRoute<addrIP>("tx");

    /**
     * willing vpls prefixes
     */
    public tabRoute<addrIP> wilVpls = new tabRoute<addrIP>("tx");

    /**
     * willing mspw prefixes
     */
    public tabRoute<addrIP> wilMspw = new tabRoute<addrIP>("tx");

    /**
     * willing evpn prefixes
     */
    public tabRoute<addrIP> wilEvpn = new tabRoute<addrIP>("tx");

    /**
     * willing mdt prefixes
     */
    public tabRoute<addrIP> wilMdt = new tabRoute<addrIP>("tx");

    /**
     * willing srte prefixes
     */
    public tabRoute<addrIP> wilSrte = new tabRoute<addrIP>("tx");

    /**
     * willing mvpn prefixes
     */
    public tabRoute<addrIP> wilMvpn = new tabRoute<addrIP>("tx");

    /**
     * willing other mvpn prefixes
     */
    public tabRoute<addrIP> wilMvpo = new tabRoute<addrIP>("tx");

    /**
     * changed unicast prefixes
     */
    public tabRoute<addrIP> chgUni = new tabRoute<addrIP>("chg");

    /**
     * changed multicast prefixes
     */
    public tabRoute<addrIP> chgMlt = new tabRoute<addrIP>("chg");

    /**
     * changed other prefixes
     */
    public tabRoute<addrIP> chgOtr = new tabRoute<addrIP>("chg");

    /**
     * changed flowspec prefixes
     */
    public tabRoute<addrIP> chgFlw = new tabRoute<addrIP>("chg");

    /**
     * changed vpnuni prefixes
     */
    public tabRoute<addrIP> chgVpnU = new tabRoute<addrIP>("chg");

    /**
     * changed vpnmulti prefixes
     */
    public tabRoute<addrIP> chgVpnM = new tabRoute<addrIP>("chg");

    /**
     * changed vpnflow prefixes
     */
    public tabRoute<addrIP> chgVpnF = new tabRoute<addrIP>("chg");

    /**
     * changed other vpnuni prefixes
     */
    public tabRoute<addrIP> chgVpoU = new tabRoute<addrIP>("chg");

    /**
     * changed other vpnmulti prefixes
     */
    public tabRoute<addrIP> chgVpoM = new tabRoute<addrIP>("chg");

    /**
     * changed other vpnflow prefixes
     */
    public tabRoute<addrIP> chgVpoF = new tabRoute<addrIP>("chg");

    /**
     * changed vpls prefixes
     */
    public tabRoute<addrIP> chgVpls = new tabRoute<addrIP>("chg");

    /**
     * changed mspw prefixes
     */
    public tabRoute<addrIP> chgMspw = new tabRoute<addrIP>("chg");

    /**
     * changed evpn prefixes
     */
    public tabRoute<addrIP> chgEvpn = new tabRoute<addrIP>("chg");

    /**
     * changed mdt prefixes
     */
    public tabRoute<addrIP> chgMdt = new tabRoute<addrIP>("chg");

    /**
     * changed srte prefixes
     */
    public tabRoute<addrIP> chgSrte = new tabRoute<addrIP>("chg");

    /**
     * changed mvpn prefixes
     */
    public tabRoute<addrIP> chgMvpn = new tabRoute<addrIP>("chg");

    /**
     * changed other mvpn prefixes
     */
    public tabRoute<addrIP> chgMvpo = new tabRoute<addrIP>("chg");

    /**
     * local address
     */
    public addrIP localAddr;

    /**
     * type of peer
     */
    protected int peerType;

    /**
     * create group
     *
     * @param parent bgp process
     * @param num group number
     */
    public rtrBgpGroup(rtrBgp parent, int num) {
        super(parent, false);
        groupNum = num;
    }

    public void flapBgpConn() {
    }

    public void doTempCfg(String cmd, boolean negated) {
    }

    public void getConfig(List<String> l, String beg, boolean filter) {
        l.addAll(getParamCfg(beg, "group " + groupNum + " ", filter));
    }

    /**
     * get status of group
     *
     * @param l list to append
     */
    public void getStatus(List<String> l) {
        String a = "";
        for (int i = 0; i < lower.neighs.size(); i++) {
            rtrBgpNeigh ntry = lower.neighs.get(i);
            if (ntry.groupMember != groupNum) {
                continue;
            }
            a += " " + ntry.peerAddr;
        }
        for (int i = 0; i < lower.lstnNei.size(); i++) {
            rtrBgpNeigh ntry = lower.lstnNei.get(i);
            if (ntry.groupMember != groupNum) {
                continue;
            }
            a += " " + ntry.peerAddr;
        }
        l.add("peers =" + a);
        l.add("type = " + rtrBgpUtil.peerType2string(peerType));
        l.add("safi = " + mask2string(addrFams));
        l.add("local = " + localAddr);
        l.add("unicast advertise = " + wilUni.size() + ", list=" + chgUni.size());
        l.add("multicast advertise = " + wilMlt.size() + ", list=" + chgMlt.size());
        l.add("other advertise = " + wilOtr.size() + ", list=" + chgOtr.size());
        l.add("flowspec advertise = " + wilFlw.size() + ", list=" + chgFlw.size());
        l.add("vpnuni advertise = " + wilVpnU.size() + ", list=" + chgVpnU.size());
        l.add("vpnmlt advertise = " + wilVpnM.size() + ", list=" + chgVpnM.size());
        l.add("vpnflw advertise = " + wilVpnF.size() + ", list=" + chgVpnF.size());
        l.add("ovpnuni advertise = " + wilVpoU.size() + ", list=" + chgVpoU.size());
        l.add("ovpnmlt advertise = " + wilVpoM.size() + ", list=" + chgVpoM.size());
        l.add("ovpnflw advertise = " + wilVpoF.size() + ", list=" + chgVpoF.size());
        l.add("vpls advertise = " + wilVpls.size() + ", list=" + chgVpls.size());
        l.add("mspw advertise = " + wilMspw.size() + ", list=" + chgMspw.size());
        l.add("evpn advertise = " + wilEvpn.size() + ", list=" + chgEvpn.size());
        l.add("mdt advertise = " + wilMdt.size() + ", list=" + chgMdt.size());
        l.add("srte advertise = " + wilSrte.size() + ", list=" + chgSrte.size());
        l.add("mvpn advertise = " + wilMvpn.size() + ", list=" + chgMvpn.size());
        l.add("omvpn advertise = " + wilMvpo.size() + ", list=" + chgMvpo.size());
        l.add("version = " + minversion + " of " + lower.compRound);
    }

    /**
     * get willing
     *
     * @param safi safi to query
     * @return table
     */
    public tabRoute<addrIP> getWilling(int safi) {
        if (safi == lower.afiUni) {
            return wilUni;
        }
        if (safi == lower.afiLab) {
            return wilUni;
        }
        if (safi == lower.afiMlt) {
            return wilMlt;
        }
        if (safi == lower.afiOtr) {
            return wilOtr;
        }
        if (safi == lower.afiFlw) {
            return wilFlw;
        }
        if (safi == lower.afiVpnU) {
            return wilVpnU;
        }
        if (safi == lower.afiVpnM) {
            return wilVpnM;
        }
        if (safi == lower.afiVpnF) {
            return wilVpnF;
        }
        if (safi == lower.afiVpoU) {
            return wilVpoU;
        }
        if (safi == lower.afiVpoM) {
            return wilVpoM;
        }
        if (safi == lower.afiVpoF) {
            return wilVpoF;
        }
        if (safi == lower.afiVpls) {
            return wilVpls;
        }
        if (safi == lower.afiMspw) {
            return wilMspw;
        }
        if (safi == lower.afiEvpn) {
            return wilEvpn;
        }
        if (safi == lower.afiMdt) {
            return wilMdt;
        }
        if (safi == lower.afiSrte) {
            return wilSrte;
        }
        if (safi == lower.afiMvpn) {
            return wilMvpn;
        }
        if (safi == lower.afiMvpo) {
            return wilMvpo;
        }
        return null;
    }

    /**
     * get changed
     *
     * @param safi safi to query
     * @return table
     */
    public tabRoute<addrIP> getChanged(int safi) {
        if (safi == lower.afiUni) {
            return chgUni;
        }
        if (safi == lower.afiLab) {
            return chgUni;
        }
        if (safi == lower.afiMlt) {
            return chgMlt;
        }
        if (safi == lower.afiOtr) {
            return chgOtr;
        }
        if (safi == lower.afiFlw) {
            return chgFlw;
        }
        if (safi == lower.afiVpnU) {
            return chgVpnU;
        }
        if (safi == lower.afiVpnM) {
            return chgVpnM;
        }
        if (safi == lower.afiVpnF) {
            return chgVpnF;
        }
        if (safi == lower.afiVpoU) {
            return chgVpoU;
        }
        if (safi == lower.afiVpoM) {
            return chgVpoM;
        }
        if (safi == lower.afiVpoF) {
            return chgVpoF;
        }
        if (safi == lower.afiVpls) {
            return chgVpls;
        }
        if (safi == lower.afiMspw) {
            return chgMspw;
        }
        if (safi == lower.afiEvpn) {
            return chgEvpn;
        }
        if (safi == lower.afiMdt) {
            return chgMdt;
        }
        if (safi == lower.afiSrte) {
            return chgSrte;
        }
        if (safi == lower.afiMvpn) {
            return chgMvpn;
        }
        if (safi == lower.afiMvpo) {
            return chgMvpo;
        }
        return null;
    }

    private void nextHopSelf(boolean nhs, tabRouteAttr<addrIP> ntry, tabRouteEntry<addrIP> route) {
        ntry.nextHop = localAddr.copyBytes();
        ntry.labelRem = new ArrayList<Integer>();
        tabLabelNtry loc = ntry.labelLoc;
        if (loc == null) {
            ipFwd tab;
            if (ntry.rouTab == null) {
                tab = lower.fwdCore;
            } else {
                tab = ntry.rouTab;
            }
            tabRouteEntry<addrIP> org = tab.labeldR.find(route);
            if (org == null) {
                loc = tab.commonLabel;
            } else {
                loc = org.best.labelLoc;
            }
        }
        int val = loc.getValue();
        if (labelPop && nhs && (val == lower.fwdCore.commonLabel.getValue())) {
            val = ipMpls.labelImp;
        }
        ntry.labelRem.add(val);
        if (lower.segrouLab != null) {
            ntry.segrouSiz = lower.segrouMax;
            ntry.segrouBeg = lower.segrouLab[0].getValue();
        }
        if (lower.bierLab != null) {
            ntry.bierHdr = tabLabelBier.num2bsl(lower.bierLen);
            ntry.bierSiz = lower.bierLab.length;
            ntry.bierBeg = lower.bierLab[0].getValue();
        }
    }

    private void nextHopSelf(boolean nhs, tabRouteEntry<addrIP> ntry) {
        for (int i = 0; i < ntry.alts.size(); i++) {
            tabRouteAttr<addrIP> attr = ntry.alts.get(i);
            nextHopSelf(nhs, attr, ntry);
        }
    }

    private void clearAttribs(tabRouteAttr<addrIP> ntry) {
        if ((sendCommunity & 1) == 0) {
            ntry.stdComm = null;
        }
        if ((sendCommunity & 2) == 0) {
            ntry.extComm = null;
        }
        if ((sendCommunity & 4) == 0) {
            ntry.lrgComm = null;
        }
        if (!accIgp) {
            ntry.accIgp = 0;
        }
        if (!traffEng) {
            ntry.bandwidth = 0;
        }
        if (!pmsiTun) {
            ntry.pmsiLab = 0;
            ntry.pmsiTyp = 0;
            ntry.pmsiTun = null;
        }
        if (!tunEnc) {
            ntry.tunelTyp = 0;
            ntry.tunelVal = null;
        }
        if (!attribSet) {
            ntry.attribAs = 0;
            ntry.attribVal = null;
        }
        if (!segRout) {
            ntry.segrouIdx = 0;
            ntry.segrouBeg = 0;
            ntry.segrouOld = 0;
            ntry.segrouSiz = 0;
            ntry.segrouPrf = null;
        }
        if (!bier) {
            ntry.bierIdx = 0;
            ntry.bierBeg = 0;
            ntry.bierOld = 0;
            ntry.bierSiz = 0;
            ntry.bierHdr = 0;
        }
        if (removePrivAsOut) {
            rtrBgpUtil.removePrivateAs(ntry.pathSeq);
            rtrBgpUtil.removePrivateAs(ntry.pathSet);
        }
        if (overridePeerOut) {
            rtrBgpUtil.replaceIntList(ntry.pathSeq, remoteAs, localAs);
            rtrBgpUtil.replaceIntList(ntry.pathSet, remoteAs, localAs);
        }
        ntry.srcRtr = null;
        ntry.oldHop = null;
        ntry.iface = null;
        switch (peerType) {
            case rtrBgpUtil.peerServr:
            case rtrBgpUtil.peerExtrn:
                ntry.originator = null;
                ntry.clustList = null;
                ntry.confSeq = null;
                ntry.confSet = null;
                ntry.locPref = 0;
                break;
            case rtrBgpUtil.peerCnfed:
                ntry.originator = null;
                ntry.clustList = null;
                break;
        }
    }

    /**
     * originate prefix
     *
     * @param afi afi
     * @param ntry prefix
     */
    public void originatePrefix(int afi, tabRouteEntry<addrIP> ntry) {
        boolean nhs = (afi == lower.afiUni) && ((addrFams & rtrBgpParam.mskLab) != 0);
        if (intVpnClnt) {
            rtrBgpUtil.decodeAttribSet(ntry);
        }
        nextHopSelf(nhs, ntry);
        switch (peerType) {
            case rtrBgpUtil.peerExtrn:
            case rtrBgpUtil.peerServr:
                for (int i = 0; i < ntry.alts.size(); i++) {
                    tabRouteAttr<addrIP> attr = ntry.alts.get(i);
                    attr.pathSeq = tabLabel.prependLabel(attr.pathSeq, localAs);
                }
                break;
            case rtrBgpUtil.peerCnfed:
                for (int i = 0; i < ntry.alts.size(); i++) {
                    tabRouteAttr<addrIP> attr = ntry.alts.get(i);
                    attr.confSeq = tabLabel.prependLabel(attr.confSeq, localAs);
                    if (attr.locPref == 0) {
                        attr.locPref = 100;
                    }
                }
                break;
            case rtrBgpUtil.peerIntrn:
            case rtrBgpUtil.peerRflct:
                for (int i = 0; i < ntry.alts.size(); i++) {
                    tabRouteAttr<addrIP> attr = ntry.alts.get(i);
                    if (attr.locPref == 0) {
                        attr.locPref = 100;
                    }
                }
                break;
        }
        for (int i = 0; i < ntry.alts.size(); i++) {
            tabRouteAttr<addrIP> attr = ntry.alts.get(i);
            attr.segrouIdx = lower.segrouIdx;
            attr.bierIdx = lower.bierIdx;
            clearAttribs(attr);
        }
    }

    /**
     * readvertise prefix
     *
     * @param afi afi
     * @param ntry prefix
     * @return false on success, true on failure
     */
    public boolean readvertPrefix(int afi, tabRouteEntry<addrIP> ntry) {
        boolean nhs = (afi == lower.afiUni) && ((addrFams & rtrBgpParam.mskLab) != 0);
        if (intVpnClnt) {
            rtrBgpUtil.decodeAttribSet(ntry);
        }
        if (!allowAsOut) {
            if (rtrBgpUtil.findIntList(ntry.best.pathSeq, remoteAs) >= 0) {
                return true;
            }
            if (rtrBgpUtil.findIntList(ntry.best.pathSet, remoteAs) >= 0) {
                return true;
            }
        }
        if (rtrBgpUtil.findIntList(ntry.best.stdComm, rtrBgpUtil.commNoAdvertise) >= 0) {
            return true;
        }
        switch (peerType) {
            case rtrBgpUtil.peerExtrn:
                if (rtrBgpUtil.findIntList(ntry.best.stdComm, rtrBgpUtil.commNoExport) >= 0) {
                    return true;
                }
                for (int i = 0; i < ntry.alts.size(); i++) {
                    tabRouteAttr<addrIP> attr = ntry.alts.get(i);
                    attr.pathSeq = tabLabel.prependLabel(attr.pathSeq, localAs);
                    if (attr.pathSeq.size() > 1) {
                        attr.metric = 0;
                    }
                }
                if (!nxtHopUnchgd) {
                    nextHopSelf(nhs, ntry);
                }
                break;
            case rtrBgpUtil.peerCnfed:
                if (rtrBgpUtil.findIntList(ntry.best.stdComm, rtrBgpUtil.commNoConfed) >= 0) {
                    return true;
                }
                switch (ntry.best.rouSrc) {
                    case rtrBgpUtil.peerExtrn:
                    case rtrBgpUtil.peerServr:
                        if (!nxtHopUnchgd) {
                            nextHopSelf(nhs, ntry);
                        }
                        break;
                }
                for (int i = 0; i < ntry.alts.size(); i++) {
                    tabRouteAttr<addrIP> attr = ntry.alts.get(i);
                    attr.confSeq = tabLabel.prependLabel(attr.confSeq, localAs);
                }
                break;
            case rtrBgpUtil.peerIntrn:
                switch (ntry.best.rouSrc) {
                    case rtrBgpUtil.peerIntrn:
                        return true;
                    case rtrBgpUtil.peerExtrn:
                    case rtrBgpUtil.peerServr:
                        if (!nxtHopUnchgd) {
                            nextHopSelf(nhs, ntry);
                        }
                        break;
                }
                break;
            case rtrBgpUtil.peerRflct:
                switch (ntry.best.rouSrc) {
                    case rtrBgpUtil.peerExtrn:
                    case rtrBgpUtil.peerServr:
                        if (!nxtHopUnchgd) {
                            nextHopSelf(nhs, ntry);
                        }
                        break;
                }
                break;
            case rtrBgpUtil.peerServr:
                if (rtrBgpUtil.findIntList(ntry.best.stdComm, rtrBgpUtil.commNoExport) >= 0) {
                    return true;
                }
                switch (ntry.best.rouSrc) {
                    case rtrBgpUtil.peerExtrn:
                        if (!nxtHopUnchgd) {
                            nextHopSelf(nhs, ntry);
                        }
                        break;
                }
                break;
        }
        clearAttribs(ntry.best);
        if (nxtHopSelf) {
            nextHopSelf(nhs, ntry);
            return false;
        }
        if (!nhs) {
            return false;
        }
        for (int i = 0; i < ntry.alts.size(); i++) {
            tabRouteAttr<addrIP> attr = ntry.alts.get(i);
            if (attr.labelRem == null) {
                nextHopSelf(nhs, attr, ntry);
            }
        }
        return false;
    }

    private void readvertTable(int afi, tabRoute<addrIP> tab, tabRoute<addrIP> cmp) {
        for (int i = 0; i < cmp.size(); i++) {
            tabRouteEntry<addrIP> ntry = cmp.get(i);
            ntry = ntry.copyBytes(tabRoute.addType.ecmp);
            if (ntry.best.rouSrc == rtrBgpUtil.peerOriginate) {
                originatePrefix(afi, ntry);
            } else if (readvertPrefix(afi, ntry)) {
                continue;
            }
            tabRoute.addUpdatedEntry(tabRoute.addType.ecmp, tab, afi, ntry, true, roumapOut, roupolOut, prflstOut);
        }
    }

    private void importTable(int afi, tabRoute<addrIP> tab, tabRoute<addrIP> imp) {
        for (int i = 0; i < imp.size(); i++) {
            tabRouteEntry<addrIP> ntry = imp.get(i);
            ntry = ntry.copyBytes(tabRoute.addType.ecmp);
            if (ntry.best.rouSrc == rtrBgpUtil.peerOriginate) {
                originatePrefix(afi, ntry);
            } else if (readvertPrefix(afi, ntry)) {
                continue;
            }
            tabRoute.addUpdatedEntry(tabRoute.addType.ecmp, tab, afi, ntry, true, voumapOut, voupolOut, null);
        }
    }

    /**
     * create needed prefix list
     *
     * @param cUni unicast
     * @param cMlt multicast
     * @param cOtr other
     * @param cFlw flowspec
     * @param cVpnU vpn uni
     * @param cVpnM vpn multi
     * @param cVpnF vpn flow
     * @param cVpoU ovpn uni
     * @param cVpoM ovpn multi
     * @param cVpoF ovpn flow
     * @param cVpls vpls
     * @param cMspw mspw
     * @param cEvpn evpn
     * @param cMdt mdt
     * @param cSrte srte
     * @param cMvpn mvpn
     * @param cMvpo omvpn
     */
    public void createNeeded(tabRoute<addrIP> cUni, tabRoute<addrIP> cMlt, tabRoute<addrIP> cOtr, tabRoute<addrIP> cFlw,
            tabRoute<addrIP> cVpnU, tabRoute<addrIP> cVpnM, tabRoute<addrIP> cVpnF,
            tabRoute<addrIP> cVpoU, tabRoute<addrIP> cVpoM, tabRoute<addrIP> cVpoF,
            tabRoute<addrIP> cVpls, tabRoute<addrIP> cMspw, tabRoute<addrIP> cEvpn,
            tabRoute<addrIP> cMdt, tabRoute<addrIP> cSrte, tabRoute<addrIP> cMvpn, tabRoute<addrIP> cMvpo) {
        tabRoute<addrIP> nUni = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nMlt = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nOtr = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nFlw = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpnU = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpnM = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpnF = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpoU = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpoM = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpoF = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nVpls = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nMspw = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nEvpn = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nMdt = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nSrte = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nMvpn = new tabRoute<addrIP>("bgp");
        tabRoute<addrIP> nMvpo = new tabRoute<addrIP>("bgp");
        if (sendDefRou) {
            tabRouteEntry<addrIP> ntry = new tabRouteEntry<addrIP>();
            ntry.prefix = rtrBgpUtil.defaultRoute(lower.afiUni);
            ntry.best.aggrRtr = new addrIP();
            ntry.best.aggrRtr.fromIPv4addr(lower.routerID);
            ntry.best.aggrAs = localAs;
            ntry.best.rouSrc = rtrBgpUtil.peerOriginate;
            originatePrefix(lower.afiUni, ntry);
            tabRoute.addUpdatedEntry(tabRoute.addType.better, nUni, lower.afiUni, ntry, true, roumapOut, roupolOut, prflstOut);
            ntry = new tabRouteEntry<addrIP>();
            ntry.prefix = rtrBgpUtil.defaultRoute(lower.afiUni);
            ntry.best.aggrRtr = new addrIP();
            ntry.best.aggrRtr.fromIPv4addr(lower.routerID);
            ntry.best.aggrAs = localAs;
            ntry.best.rouSrc = rtrBgpUtil.peerOriginate;
            originatePrefix(lower.afiMlt, ntry);
            tabRoute.addUpdatedEntry(tabRoute.addType.better, nMlt, lower.afiMlt, ntry, true, roumapOut, roupolOut, prflstOut);
        }
        for (int i = 0; i < lower.routerRedistedU.size(); i++) {
            tabRouteEntry<addrIP> ntry = lower.routerRedistedU.get(i);
            if (ntry == null) {
                continue;
            }
            ntry = ntry.copyBytes(tabRoute.addType.ecmp);
            ntry.best.rouSrc = rtrBgpUtil.peerOriginate;
            originatePrefix(lower.afiUni, ntry);
            tabRoute.addUpdatedEntry(tabRoute.addType.ecmp, nUni, lower.afiUni, ntry, true, roumapOut, roupolOut, prflstOut);
        }
        for (int i = 0; i < lower.routerRedistedM.size(); i++) {
            tabRouteEntry<addrIP> ntry = lower.routerRedistedM.get(i);
            if (ntry == null) {
                continue;
            }
            ntry = ntry.copyBytes(tabRoute.addType.ecmp);
            ntry.best.rouSrc = rtrBgpUtil.peerOriginate;
            originatePrefix(lower.afiMlt, ntry);
            tabRoute.addUpdatedEntry(tabRoute.addType.ecmp, nMlt, lower.afiMlt, ntry, true, roumapOut, roupolOut, prflstOut);
        }
        for (int i = 0; i < lower.routerRedistedF.size(); i++) {
            tabRouteEntry<addrIP> ntry = lower.routerRedistedF.get(i);
            if (ntry == null) {
                continue;
            }
            ntry = ntry.copyBytes(tabRoute.addType.ecmp);
            ntry.best.rouSrc = rtrBgpUtil.peerOriginate;
            originatePrefix(lower.afiFlw, ntry);
            tabRoute.addUpdatedEntry(tabRoute.addType.ecmp, nFlw, lower.afiFlw, ntry, true, voumapOut, voupolOut, null);
        }
        readvertTable(lower.afiUni, nUni, cUni);
        readvertTable(lower.afiMlt, nMlt, cMlt);
        readvertTable(lower.afiOtr, nOtr, cOtr);
        importTable(lower.afiFlw, nFlw, cFlw);
        importTable(lower.afiVpnU, nVpnU, cVpnU);
        importTable(lower.afiVpnM, nVpnM, cVpnM);
        importTable(lower.afiVpnF, nVpnF, cVpnF);
        importTable(lower.afiVpoU, nVpoU, cVpoU);
        importTable(lower.afiVpoM, nVpoM, cVpoM);
        importTable(lower.afiVpoF, nVpoF, cVpoF);
        importTable(lower.afiVpls, nVpls, cVpls);
        importTable(lower.afiMspw, nMspw, cMspw);
        importTable(lower.afiEvpn, nEvpn, cEvpn);
        importTable(lower.afiMdt, nMdt, cMdt);
        importTable(lower.afiSrte, nSrte, cSrte);
        importTable(lower.afiMvpn, nMvpn, cMvpn);
        importTable(lower.afiMvpo, nMvpo, cMvpo);
        lower.routerDoAggregates(lower.afiUni, nUni, localAddr, lower.fwdCore.commonLabel, rtrBgpUtil.peerOriginate, lower.routerID, lower.localAs);
        lower.routerDoAggregates(lower.afiMlt, nMlt, localAddr, lower.fwdCore.commonLabel, rtrBgpUtil.peerOriginate, lower.routerID, lower.localAs);
        wilUni = nUni;
        wilMlt = nMlt;
        wilOtr = nOtr;
        wilFlw = nFlw;
        wilVpnU = nVpnU;
        wilVpnM = nVpnM;
        wilVpnF = nVpnF;
        wilVpoU = nVpoU;
        wilVpoM = nVpoM;
        wilVpoF = nVpoF;
        wilVpls = nVpls;
        wilMspw = nMspw;
        wilEvpn = nEvpn;
        wilMdt = nMdt;
        wilSrte = nSrte;
        wilMvpn = nMvpn;
        wilMvpo = nMvpo;
    }

}
