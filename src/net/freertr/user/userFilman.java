package net.freertr.user;

import java.util.ArrayList;
import java.util.List;
import net.freertr.util.bits;
import net.freertr.util.version;

/**
 * file manager
 *
 * @author matecsaba
 */
public class userFilman {

    private final userScreen console;

    private final userFilmanPanel[] pan;

    private int act;

    /**
     * create file manager
     *
     * @param pip console
     */
    public userFilman(userScreen pip) {
        console = pip;
        pan = new userFilmanPanel[2];
        int siz = console.sizX / 2;
        String a = version.getRWpath();
        for (int i = 0; i < pan.length; i++) {
            pan[i] = new userFilmanPanel(console, i * siz, 0, siz, console.sizY - 2);
            pan[i].path = a;
            pan[i].readUp();
        }
    }

    /**
     * do work
     */
    public void doWork() {
        for (;;) {
            for (int i = 0; i < pan.length; i++) {
                pan[i].doRange();
                pan[i].doDraw();
            }
            pan[act].doCurs();
            console.refresh();
            if (doKey()) {
                break;
            }
        }
        doClear();
    }

    /**
     * clear console after
     */
    public void doClear() {
        console.refresh();
        console.fillLines(0, console.sizY, userScreen.colBlack, 32);
        console.refresh();
        console.putCls();
        console.putCur(console.sizX, console.sizY);
        console.refresh();
    }

    private boolean doKey() {
        int i = userScreen.getKey(console.pipe);
        switch (i) {
            case -1: // end
                return true;
            case 0x0261: // ctrl+a
                doKeyUp();
                return false;
            case 0x027a: // ctrl+z
                doKeyDn();
                return false;
            case 0x0269: // ctrl+i
                doKeyTab();
                return false;
            case 0x0272: // ctrl+r
                doKeyRead();
                return false;
            case 0x026c: // ctrl+l
                doClear();
                return false;
            case 0x0266: // ctrl+f
                doKeyFind();
                return false;
            case 0x0270: // ctrl+p
                doKeyF1();
                return false;
            case 0x0262: // ctrl+b
                doKeyBin();
                return false;
            case 0x0268: // ctrl+h
                doKeyHex();
                return false;
            case 0x0276: // ctrl+v
                doKeyF3();
                return false;
            case 0x0265: // ctrl+e
                doKeyF4();
                return false;
            case 0x0263: // ctrl+c
                doKeyF5();
                return false;
            case 0x026e: // ctrl+n
                doKeyF6();
                return false;
            case 0x026b: // ctrl+k
                doKeyF7();
                return false;
            case 0x0264: // ctrl+d
                doKeyF8();
                return false;
            case 0x0271: // ctrl+q
                return true;
            case 0x0278: // ctrl+x
                return true;
            case 0x8002: // tabulator
                doKeyTab();
                return false;
            case 0x8004: // enter
                doKeyEnter();
                return false;
            case 0x800c: // up
                doKeyUp();
                return false;
            case 0x800d: // down
                doKeyDn();
                return false;
            case 0x8008: // home
                doKeyHom();
                return false;
            case 0x8009: // end
                doKeyEnd();
                return false;
            case 0x800a: // pgup
                doKeyPgUp();
                return false;
            case 0x800b: // pgdn
                doKeyPgDn();
                return false;
            case 0x8014: // f1
                doKeyF1();
                return false;
            case 0x8015: // f2
                doKeyF2();
                return false;
            case 0x8016: // f3
                doKeyF3();
                return false;
            case 0x8017: // f4
                doKeyF4();
                return false;
            case 0x8018: // f5
                doKeyF5();
                return false;
            case 0x8019: // f6
                doKeyF6();
                return false;
            case 0x801a: // f7
                doKeyF7();
                return false;
            case 0x801b: // f8
                doKeyF8();
                return false;
            case 0x801d: // f10
                return true;
        }
        return false;
    }

    private void doKeyUp() {
        pan[act].curL--;
    }

    private void doKeyDn() {
        pan[act].curL++;
    }

    private void doKeyPgUp() {
        pan[act].curL -= 5;
    }

    private void doKeyPgDn() {
        pan[act].curL += 5;
    }

    private void doKeyHom() {
        pan[act].curL = 0;
    }

    private void doKeyEnd() {
        pan[act].curL = pan[act].fil.size();
    }

    private void doKeyTab() {
        act = (act + 1) & 1;
    }

    private void doKeyEnter() {
        pan[act].doEnter();
    }

    private void doKeyRead() {
        pan[act].readUp();
    }

    private void doKeyFind() {
        String b = console.askUser("enter name to find:", userScreen.colRed, userScreen.colWhite, userScreen.colBrYellow, userScreen.colBrWhite, -1, -1, -1, "");
        if (b.length() < 1) {
            return;
        }
        b = userReader.filter2reg(b);
        for (int i = pan[act].curL; i < pan[act].fil.size(); i++) {
            String res = pan[act].fil.get(i);
            if (!res.matches(b)) {
                continue;
            }
            pan[act].curL = i;
            return;
        }
    }

    private void doKeyF1() {
        List<String> l = new ArrayList<String>();
        l.add("f1 - help");
        l.add("f2 - view file hash");
        l.add("f3 - text view file");
        l.add("f4 - text edit file");
        l.add("f5 - copy file");
        l.add("f6 - rename entry");
        l.add("f7 - make dir");
        l.add("f8 - erase entry");
        l.add("f10 - exit");
        l.add("tab - change panel");
        l.add("ctrl+p - help");
        l.add("ctrl+r - reread entries");
        l.add("ctrl+l - redraw screen");
        l.add("ctrl+f - find file");
        l.add("ctrl+a - move up");
        l.add("ctrl+z - move down");
        l.add("ctrl+i - change panel");
        l.add("ctrl+b - bin view file");
        l.add("ctrl+h - hex view file");
        l.add("ctrl+v - text view file");
        l.add("ctrl+e - text edit file");
        l.add("ctrl+c - copy file");
        l.add("ctrl+n - rename entry");
        l.add("ctrl+k - make dir");
        l.add("ctrl+d - erase entry");
        l.add("ctrl+q - exit");
        l.add("ctrl+x - exit");
        console.helpWin(userScreen.colBlue, userScreen.colWhite, userScreen.colBrWhite, -1, -1, -1, -1, l);
        console.putStr(console.sizX - 8, console.sizY - 1, userScreen.colBlue, userScreen.colWhite, false, "f1=help");
    }

    private void doKeyF2() {
        String a = pan[act].getFn();
        List<String> b = userFlash.calcFileHashes(a);
        userEditor v = new userEditor(console, b, a, false);
        v.doView();
    }

    private void doKeyBin() {
        String a = pan[act].getFn();
        List<String> b = userFlash.binRead(a);
        userEditor v = new userEditor(console, b, a, false);
        v.doView();
    }

    private void doKeyHex() {
        String a = pan[act].getFn();
        List<String> b = userFlash.hexRead(a);
        userEditor v = new userEditor(console, b, a, false);
        v.doView();
    }

    private void doKeyF3() {
        String a = pan[act].getFn();
        List<String> b = bits.txt2buf(a);
        userEditor v = new userEditor(console, b, a, false);
        v.doView();
    }

    private void doKeyF4() {
        String a = pan[act].getFn();
        List<String> b = bits.txt2buf(a);
        userEditor e = new userEditor(console, b, a, false);
        if (e.doEdit()) {
            return;
        }
        bits.buf2txt(true, b, a);
    }

    private void doKeyF5() {
        String a = pan[act].getFn();
        int i = a.lastIndexOf("/");
        String b = pan[1 - act].path + a.substring(i + 1, a.length());
        b = console.askUser("enter target name:", userScreen.colRed, userScreen.colWhite, userScreen.colBrYellow, userScreen.colBrWhite, -1, -1, -1, b);
        if (b.length() < 1) {
            return;
        }
        userFlash.copy(a, b, false);
        pan[1 - act].readUp();
        doClear();
    }

    private void doKeyF6() {
        String a = pan[act].getFn();
        String b = console.askUser("enter new name:", userScreen.colRed, userScreen.colWhite, userScreen.colBrYellow, userScreen.colBrWhite, -1, -1, -1, a);
        if (b.length() < 1) {
            return;
        }
        userFlash.rename(a, b, false, false);
        pan[act].readUp();
    }

    private void doKeyF7() {
        String b = console.askUser("enter name of new directory:", userScreen.colRed, userScreen.colWhite, userScreen.colBrYellow, userScreen.colBrWhite, -1, -1, -1, pan[act].path);
        if (b.length() < 1) {
            return;
        }
        userFlash.mkdir(b);
        pan[act].readUp();
    }

    private void doKeyF8() {
        String a = pan[act].getFn();
        if (a.endsWith("/")) {
            a = a.substring(0, a.length() - 1);
        }
        String b = console.askUser("really delete " + a + "? (y/n)", userScreen.colRed, userScreen.colWhite, userScreen.colBrYellow, userScreen.colBrWhite, -1, -1, -1, "n");
        b = b.trim().toLowerCase();
        if (!b.equals("y")) {
            return;
        }
        userFlash.delete(a);
        pan[act].readUp();
    }

}
