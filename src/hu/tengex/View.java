package hu.tengex;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;

public class View extends JFrame {

    private View view;
    private JTextField urlTextField;
    private JTextField savedirTextField;
    private JLabel messageLabel;
    private GalleryDownloader galleryDownloader;

    public View() {
        super("Image Downloader");
        this.view = this;
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        inputFields.setLayout(new GridLayout(5, 0));

        urlTextField = new JTextField(25);
        urlTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    urlTextField.setText(null);
                    urlTextField.paste();
                }
            }
        });

        savedirTextField = new JTextField(25);
        savedirTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    savedirTextField.setText(null);
                    savedirTextField.paste();
                }
            }
        });

        messageLabel = new JLabel("");
        messageLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        inputFields.add(new JLabel("<html><b>&nbsp;URL / HTML forráskód:"));
        inputFields.add(urlTextField);
        inputFields.add(new JLabel("<html><b>&nbsp;Mappanév:"));
        inputFields.add(savedirTextField);
        inputFields.add(messageLabel);

        return inputFields;
    }

    private JPanel getMainButtons() {
        final JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Button enterButton = new Button(0);
        mainButtons.add(enterButton);
        mainButtons.add(new Button(1));
        mainButtons.add(new Button(2));

        getRootPane().setDefaultButton(enterButton);

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
                    messageLabel.setText("");
                    messageLabel.setForeground(new Color(50, 100, 0));

                    if (galleryDownloader != null) {
                        galleryDownloader.stop();
                    }

                    savedirTextField.setText(toTitleCase(savedirTextField.getText()));
                    //try {
                    galleryDownloader = new GalleryDownloader(urlTextField.getText(), savedirTextField.getText(), messageLabel);
                    new Thread(galleryDownloader).start();
                    /*} catch (MalformedURLException e1) {
                        messageLabel.setText("Hibás URL!");
                        messageLabel.setForeground(new Color(200, 0, 0));
                    }*/

                } else if (1 == type) {
                    urlTextField.setText(null);
                    savedirTextField.setText(null);
                    urlTextField.requestFocusInWindow();
                } else if (2 == type) {
                    JOptionPane.showMessageDialog(view, "<html><b>Image Downloader v180123-1 by tengex</b>\n\n<html><u>Galériák letöltése:</u> kitty-kats.net <i>(csak HTML forráskódból)</i>, urlgalleries.net\n\n<html><u>Kompatibilis képmegosztók:</u> imagevenue.com, imgspice.com, fapat.me,\n  imagetwist.com, imgtrex.com, imgchili.net, imgchili.com, sexyimg.eu,\n  imgdrive.net, imagedecode.com, imageknoxx.com, t.img.yt, pixhost.org\n  turboimagehost.com, imgserve.net\n\n<html><u>URL:</u> a galériát tartalmazó oldal teljes URL címe\n<html><u>HTML forráskód:</u> a galériát tartalmazó oldal forráskódja\n<html><u>Mappanév:</u> a mentéshez létrehozandó mappa neve</html>\n\n<html><i>Szövegmezőbe beilleszteni lehet jobb kattintással, enter nyomására\n<html><i>indul a letöltés. Letöltés indításakor az előző letöltési folyamat leáll!", "Segítség", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}

