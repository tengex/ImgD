package hu.tengex;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Controller {
    public void getURLcontent(String urlStr){
        try {
            final URL url = new URL(urlStr);
            final URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            br.close();

            System.out.println("Done");
            System.out.println(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadURL(String urlStr, String saveFileStr) {
        try {
            java.net.URL url = new URL(urlStr);
            File saveFile = new File(saveFileStr);
            FileUtils.copyURLToFile(url, saveFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
