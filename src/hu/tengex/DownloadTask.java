package hu.tengex;

import java.net.URL;

public class DownloadTask implements Runnable {

    private URL url;
    private String savedir;
    private int ind;

    public DownloadTask(URL url, String savedir, int ind) {
        this.url = url;
        this.savedir = savedir;
        this.ind = ind;
    }

    @Override
    public void run() {
        GalleryDownloader.downloadURLtoFile(url, savedir, ind);
        GalleryDownloader.incDownloadedFilesCount();
    }
}
