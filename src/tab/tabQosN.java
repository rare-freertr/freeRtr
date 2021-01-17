package tab;

import addr.addrIP;
import java.util.ArrayList;
import java.util.List;
import pack.packHolder;
import tab.tabListingEntry.actionType;
import util.bits;
import util.counter;

/**
 * qos entry
 *
 * @author matecsaba
 */
public class tabQosN {

    /**
     * traffic class entry
     */
    protected tabPlcmapN entry;

    /**
     * parent of this node (common bandwidth pool)
     */
    protected tabQosN parent;

    /**
     * skip them if not matches
     */
    protected int childs;

    /**
     * bytes used in this interval
     */
    protected long bytes;

    /**
     * beginning of interval
     */
    protected long time;

    /**
     * list of packets
     */
    protected final List<packHolder> packets;

    /**
     * packet counter
     */
    protected counter cntr = new counter();

    /**
     * create new qos class
     */
    public tabQosN() {
        packets = new ArrayList<packHolder>();
    }

    /**
     * get statistics
     *
     * @param l list to append
     */
    protected void getStats(List<String> l) {
        l.add("description=" + entry.description);
        l.add("  childs=" + childs + ", queues=" + packets.size() + "/" + getQueues() + ", interval=" + getInterval() + ", bytes/interval=" + getBytePerInt());
        l.add("  match=" + entry.getCounters());
        l.add("  transmit=" + cntr.getShStat());
    }

    /**
     * update byte counts (in parents also)
     *
     * @param add bytes to add
     */
    protected void updateBytes(int add) {
        bytes += add;
        if (parent == null) {
            return;
        }
        parent.updateBytes(add);
    }

    /**
     * get access list
     *
     * @return acl
     */
    public tabListing<tabAceslstN<addrIP>, addrIP> getAccessList() {
        return entry.aclMatch;
    }

    /**
     * get interval in ms
     *
     * @return interval
     */
    public int getInterval() {
        int i = entry.interval;
        if (i < 1) {
            return 100;
        } else {
            return i;
        }
    }

    /**
     * get bytes/interval
     *
     * @return bytes/int
     */
    public long getBytePerInt() {
        int i = getInterval();
        if (i > 1000) {
            return entry.accessRate * (i / 1000);
        }
        return entry.accessRate / (1000 / i);
    }

    /**
     * get action
     *
     * @return action
     */
    public actionType getAction() {
        return entry.action;
    }

    /**
     * get queue size
     *
     * @return queue size
     */
    protected int getQueues() {
        int i = entry.queues;
        if (i > 0) {
            return i;
        } else {
            return 128;
        }
    }

    /**
     * check if there is room for bytes
     *
     * @param add bytes to check
     * @return false if available, true if full
     */
    protected boolean checkMyBytes(int add) {
        if (bytes < 1) {
            return false;
        }
        return (bytes + add) > getBytePerInt();
    }

    /**
     * check if there is room for bytes
     *
     * @param add bytes to check
     * @return false if available, true if full
     */
    protected boolean checkPrntBytes(int add) {
        if (parent == null) {
            return false;
        }
        if (parent.checkMyBytes(add)) {
            return true;
        }
        return parent.checkPrntBytes(add);
    }

    /**
     * update time
     *
     * @param curr current time
     * @return ms left from interval, 0 means just reset
     */
    protected long updateTime(long curr) {
        long left = getInterval() - (curr - time);
        if (left > 0) {
            return left;
        }
        time = curr;
        bytes -= (entry.accessRate - entry.exceedRate);
        if (bytes < 0) {
            bytes = 0;
        }
        return 0;
    }

    /**
     * recursively update time
     *
     * @param curr current time
     */
    protected void recUpdateTime(long curr) {
        updateTime(curr);
        if (parent == null) {
            return;
        }
        parent.recUpdateTime(curr);
    }

    /**
     * enqueue one packet
     *
     * @param pck packet to enqueue
     */
    public void enqueuePack(packHolder pck) {
        entry.cntr.rx(pck);
        if (packets.size() > getQueues()) {
            if (!entry.randomDetect) {
                return;
            }
            packets.remove(bits.random(0, packets.size()));
        }
        packets.add(pck.copyBytes(true, true));
    }

    /**
     * check packet
     *
     * @param pck packet
     * @return false if allowed, true if droping
     */
    public boolean checkPacket(packHolder pck) {
        entry.cntr.rx(pck);
        int len = pck.dataSize();
        switch (entry.action) {
            case actPermit:
                return false;
            case actPolice:
            case actPriorty:
            case actShaper:
            case actBndwdth:
                if (checkMyBytes(len)) {
                    return true;
                }
                if (checkPrntBytes(len)) {
                    return true;
                }
                return false;
            case actDeny:
            default:
                return true;
        }
    }

    /**
     * dequeue one packet
     *
     * @return packet got, null if nothing
     */
    public packHolder dequeuePack() {
        if (packets.size() < 1) {
            return null;
        }
        switch (entry.action) {
            case actPermit:
                return packets.remove(0);
            case actPolice:
            case actPriorty:
                packHolder pck = packets.remove(0);
                if (checkMyBytes(pck.dataSize())) {
                    packets.clear();
                    return null;
                }
                if (checkPrntBytes(pck.dataSize())) {
                    return null;
                }
                return pck;
            case actShaper:
                pck = packets.get(0);
                if (checkMyBytes(pck.dataSize())) {
                    return null;
                }
                if (checkPrntBytes(pck.dataSize())) {
                    return null;
                }
                packets.remove(0);
                return pck;
            case actBndwdth:
                pck = packets.get(0);
                if (checkMyBytes(pck.dataSize())) {
                    bytes = 0;
                    return null;
                }
                if (checkPrntBytes(pck.dataSize())) {
                    return null;
                }
                packets.remove(0);
                return pck;
            case actDeny:
            default:
                packets.clear();
                return null;
        }
    }

}
