package hu.tengex;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller {
        public void downloadURL(String urlStr, String saveFileStr){
            try {
                java.net.URL url =  new URL(urlStr);
                File saveFile = new File(saveFileStr);
                FileUtils.copyURLToFile(url, saveFile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
