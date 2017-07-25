package hu.tengex;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryDownloader implements Runnable {

    private volatile Thread blinker;
    private URL galleryURL;
    private String savedir;
    private JLabel messageLabel;
    private int counter;
    private ExecutorService pool;

    public GalleryDownloader(String url, String savedir, JLabel messageLabel) throws MalformedURLException {
        this.galleryURL = new URL(url);
        this.savedir = savedir;
        this.messageLabel = messageLabel;
        this.counter = 0;
        this.pool = Executors.newFixedThreadPool(10);
    }

    public String downloadURLtoVariable(URL url) {
        try {
            final URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            br.close();

            return content.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void downloadURLtoFile(URL url, String savedir, int ind) {
        try {
            final String filename = savedir + "_" + ((ind < 10) ? "00" + ind : ((ind < 100) ? "0" + ind : ind)) + ".jpg";
            File file = new File(savedir + "/" + filename);
            FileUtils.copyURLToFile(url, file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        blinker = null;
        pool.shutdownNow();
    }

    @Override
    public void run() {
        blinker = Thread.currentThread();

        try {
            FileUtils.forceMkdir(new File(savedir));
            Jerry galleryHTML = Jerry.jerry(downloadURLtoVariable(galleryURL));

            if (galleryURL.getHost().contains("kitty-kats.net")) {
                galleryHTML.$(".externalLink").each(new JerryFunction() {
                    @Override
                    public Boolean onNode(Jerry $this, int i) {
                        if (blinker != Thread.currentThread()) {
                            System.out.println("Thread stopped");
                            return false;
                        }

                        final String smallImageSrc = $this.find("img").attr("src");
                        if (smallImageSrc != null) {
                            counter++;
                            final String bigImageSrc = getBigImageSrc(smallImageSrc);
                            System.out.println(bigImageSrc);
                            System.out.println(savedir);
                            try {
                                pool.submit(new DownloadTask(new URL(bigImageSrc), savedir, counter));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                });
            } else if (galleryURL.getHost().contains("urlgalleries.net")) {
                galleryHTML.$(".gallery").each(new JerryFunction() {
                    @Override
                    public Boolean onNode(Jerry $this, int i) {
                        if (blinker != Thread.currentThread()) {
                            System.out.println("Thread stopped");
                            return false;
                        }

                        final String smallImageSrc = $this.attr("src").replace("/thumb.php?t=", "");
                        if (smallImageSrc != null) {
                            counter++;
                            final String bigImageSrc = getBigImageSrc(smallImageSrc);
                            System.out.println(bigImageSrc);
                            System.out.println(savedir);
                            try {
                                pool.submit(new DownloadTask(new URL(bigImageSrc), savedir, counter));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }

                        return true;
                    }
                });
            } else {
                messageLabel.setText("Nem ismert oldal!");
            }
        } catch (IOException e1) {
            messageLabel.setText("Hiba a mappa létrehozásakor!");
            messageLabel.setForeground(new Color(200, 0, 0));
        }
    }

    private String getBigImageSrc(String smallImageSrc) {
        if (smallImageSrc.contains("imagevenue.com/")) {
            try {
                final String bigPageSrc = smallImageSrc.replaceAll("/loc\\d+/th_", "/img.php?image=");
                Jerry bigImageHTML = Jerry.jerry(downloadURLtoVariable(new URL(bigPageSrc)));
                String bigImageSrc = bigImageHTML.$("#thepic").attr("src");
                if (bigImageSrc != null) {
                    bigImageSrc = smallImageSrc.replaceAll("loc\\d+/th_.+\\.jpg", "") + bigImageSrc;
                    return bigImageSrc;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (smallImageSrc.contains("imgspice.com/")
                || smallImageSrc.contains("imgtrex.com/")) {
            return smallImageSrc.replace("_t.jpg", ".jpg");
        } else if (smallImageSrc.contains("fapat.me/")) {
            return smallImageSrc.replace("upload/small/", "big/").replace("fapat.me", "i.fapat.me");
        } else if (smallImageSrc.contains("imagetwist.com/")) {
            return smallImageSrc.replace("/th/", "/i/").replace("http://", "https://");
        } else if (smallImageSrc.contains("imgchili.net/")
                || smallImageSrc.contains("imgchili.com/")) {
            return smallImageSrc.replace("://t", "://i");
        } else if (smallImageSrc.contains("sexyimg.eu/")
                || smallImageSrc.contains("imgdrive.net/")
                || smallImageSrc.contains("imagedecode.com/")
                || smallImageSrc.contains("imageknoxx.com/")) {
            return smallImageSrc.replace("small/", "big/");
        } else if (smallImageSrc.contains("img.yt/")) {
            return smallImageSrc.replace("img.yt/upload/small/", "t.img.yt/big/");
        }

        return "";
    }
}
