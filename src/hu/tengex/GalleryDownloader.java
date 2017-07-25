package hu.tengex;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class GalleryDownloader {

    private URL galleryURL;
    private String savedir;
    private JLabel messageLabel;

    public GalleryDownloader(String url, String savedir, JLabel messageLabel) throws MalformedURLException {
        this.galleryURL = new URL(url);
        this.savedir = savedir;
        this.messageLabel = messageLabel;

        //System.out.println(galleryURL.getHost());
        //System.out.println(galleryURL.toString());
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

    public void downloadURLtoFile(URL url, String savedir, int ind) {
        try {
            File file = new File(savedir);
            FileUtils.copyURLToFile(url, file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadGallery() {
        Jerry galleryHTML = Jerry.jerry(downloadURLtoVariable(galleryURL));

        if (galleryURL.getHost().contains("kitty-kats.net")) {
            galleryHTML.$(".externalLink").each(new JerryFunction() {
                @Override
                public Boolean onNode(Jerry $this, int i) {
                    final String smallImageSrc = $this.find("img").attr("src");
                    if (smallImageSrc != null) {
                        //szamlalo++;
                        //getBigImageSrc(smallImageSrc);
                        System.out.println(getBigImageSrc(smallImageSrc));
                    }
                    return true;
                }
            });
        } else if (galleryURL.getHost().contains("urlgalleries.net")) {
            galleryHTML.$(".gallery").each(new JerryFunction() {
                @Override
                public Boolean onNode(Jerry $this, int i) {
                    final String smallImageSrc = $this.attr("src").replace("/thumb.php?t=", "");
                    if (smallImageSrc != null) {
                        //szamlalo++;
                        //getBigImageSrc(smallImageSrc);
                        System.out.println(getBigImageSrc(smallImageSrc));
                    }

                    return true;
                }
            });
        } else {
            messageLabel.setText("Nem ismert oldal!");
        }
    }

    private String getBigImageSrc(String smallImageSrc) {
        if (smallImageSrc.contains("imagevenue.com/")) {
            try {
                final String bigPageSrc = smallImageSrc.replaceAll("/loc\\d+/th_", "/img.php?image=");
                Jerry bigImageHTML = Jerry.jerry(downloadURLtoVariable(new URL(bigPageSrc)));
                String bigImageSrc = bigImageHTML.$("#thepic").attr("src");
                if(bigImageSrc != null) {
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
