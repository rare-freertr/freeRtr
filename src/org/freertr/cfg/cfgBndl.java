package org.freertr.cfg;

import java.util.ArrayList;
import java.util.List;
import org.freertr.ifc.ifcBundle;
import org.freertr.tab.tabGen;
import org.freertr.user.userFilter;
import org.freertr.user.userHelping;
import org.freertr.util.bits;
import org.freertr.util.cmds;

/**
 * one bundle configuration
 *
 * @author matecsaba
 */
public class cfgBndl implements Comparable<cfgBndl>, cfgGeneric {

    /**
     * name of this bundle
     */
    public final String name;

    /**
     * bundle handler
     */
    public ifcBundle bundleHed;

    /**
     * defaults text
     */
    public final static String[] defaultL = {
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "description",
        "bundle .*!" + cmds.tabulator + "ethernet",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "backup",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "logging",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "loadbalance",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "replicate",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "reporter",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "dynamic",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "sequence",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "dejitter",
        "bundle .*!" + cmds.tabulator + cmds.negated + cmds.tabulator + "peering"
    };

    /**
     * defaults filter
     */
    public static tabGen<userFilter> defaultF;

    public int compareTo(cfgBndl o) {
        return name.toLowerCase().compareTo(o.name.toLowerCase());
    }

    public String toString() {
        return "bndl " + name;
    }

    /**
     * create new bundle instance
     *
     * @param nam name of bridge
     */
    public cfgBndl(String nam) {
        name = "" + bits.str2num(nam);
    }

    /**
     * close this bundle
     */
    public void closeUp() {
    }

    /**
     * get name of interface represents this bundle
     *
     * @return interface name
     */
    public String getIntName() {
        return "bundle" + name;
    }

    /**
     * get config
     *
     * @param filter filter
     * @return config
     */
    public List<String> getShRun(int filter) {
        List<String> l = new ArrayList<String>();
        l.add("bundle " + name);
        bundleHed.getConfig(l, cmds.tabulator);
        l.add(cmds.tabulator + cmds.finish);
        l.add(cmds.comment);
        if ((filter & 1) == 0) {
            return l;
        }
        return userFilter.filterText(l, defaultF);
    }

    /**
     * get help text
     *
     * @param l help text
     */
    public void getHelp(userHelping l) {
        ifcBundle.getHelp(l);
    }

    /**
     * do config string
     *
     * @param cmd config
     */
    public void doCfgStr(cmds cmd) {
        bundleHed.doConfig(cmd);
    }

    /**
     * get prompt
     *
     * @return prompt
     */
    public String getPrompt() {
        return "bndl";
    }

}
