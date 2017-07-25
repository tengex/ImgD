package hu.tengex;

import org.apache.commons.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame {

    private View view;
    private JTextField urlTextField;
    private JTextField savedirTextField;
    private JLabel messageLabel;

    public View() {
        super("Image Downloader");
        this.view = this;
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(getInputFields());
        mainPanel.add(getMainButtons());

        getContentPane().add(mainPanel);
        this.pack();
        setVisible(true);
    }

    private JPanel getInputFields() {
        final JPanel inputFields = new JPanel();
        inputFields.setLayout(new BoxLayout(inputFields, BoxLayout.Y_AXIS));

        urlTextField = new JTextField();
        urlTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    urlTextField.setText(null);
                    urlTextField.paste();
                }
            }
        });

        savedirTextField = new JTextField();
        savedirTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    savedirTextField.setText(null);
                    savedirTextField.paste();
                }
            }
        });

        messageLabel = new JLabel("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        messageLabel.setPreferredSize(new Dimension(100, 30));
        messageLabel.setForeground(new Color(50, 100, 0));

        inputFields.add(new JLabel("URL:"));
        inputFields.add(urlTextField);
        inputFields.add(new JLabel("Mappanév:"));
        inputFields.add(savedirTextField);
        inputFields.add(messageLabel);

        return inputFields;
    }

    private JPanel getMainButtons(){
        final JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainButtons.setPreferredSize(new Dimension(350, 50));
        mainButtons.add(new Button(0));
        mainButtons.add(new Button(1));
        mainButtons.add(new Button(2));

        return mainButtons;
    }

    class Button extends JButton {

        public Button(int type) {
            if (type == 0) {
                setText("Letöltés");
                addActionListener(new Action(type));
            } else if (type == 1) {
                setText("Mezők törlése");
                addActionListener(new Action(type));
            } else if (type == 2) {
                setText("Segítség");
                addActionListener(new Action(type));
            }
        }

        class Action extends AbstractAction {

            private final int type;

            public Action(int type) {
                this.type = type;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (0 == type) {
                    savedirTextField.setText(WordUtils.capitalizeFully(savedirTextField.getText()));
                    final GalleryDownloader galleryDownloader = new GalleryDownloader(urlTextField.getText(), savedirTextField.getText());
                    //galleryDownloader.downloadURL("http://i3.imgchili.net/105494/105494436_femjoy_12096884_016.jpg", "105494436_femjoy_12096884_016.jpg");
                    //galleryDownloader.getURLcontent("http://people.inf.elte.hu/tengex/");
                } else if (1 == type) {
                    urlTextField.setText(null);
                    savedirTextField.setText(null);
                    urlTextField.requestFocusInWindow();
                } else if (2 == type) {
                    JOptionPane.showMessageDialog(view, "<html><b>Image Downloader</b>\n\n<html><u>Galériák letöltése:</u> kitty-kats.net, urlgalleries.net\n\n<html><u>Kompatibilis képmegosztók:</u> imagevenue.com, imgspice.com, fapat.me,\n  imagetwist.com, imgtrex.com, imgspice.com, imgchili.net, imgchili.com,\n  sexyimg.eu, imgdrive.net, imagedecode.com, imageknoxx.com, img.yt\n\n<html><u>URL:</u> a galériát tartalmazó oldal elérési címe\n<html><u>Mappanév:</u> a mentéshez létrehozandó mappa neve</html>\n\n<html><i>Szövegmezőbe beilleszteni jobb kattintással lehet.", "Segítség", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}

