package hu.tengex;

import javax.swing.*;

//TODO Megadott URL alapján töltse le a galériát
//TODO Szövegmezőben mutassa a letöltés jelenlegi állapotát (ehhez kell, hogy észlelje, amikor egy fájl letöltődött)
//TODO Letöltéshez legyen megadva timeout, ha az egyik fájl nem akar lejönni (valamint a letöltés gomb újabb megnyomásakor minden jelenlegi letöltő szál leállítódik)
//TODO Tesztelni Windows-on is

public class Main {

    public static void main(String[] args) {
        System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        new View();
    }
}
