package hu.tengex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends JFrame {

    private Controller controller;
    private View view;

    public View(Controller controller) {
        super("ImgD");
        this.controller = controller;
        this.view = this;
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JPanel inputFields = new JPanel();
        inputFields.setLayout(new BoxLayout(inputFields, BoxLayout.Y_AXIS));
        //inputFields.setPreferredSize(new Dimension(350, 150));
        inputFields.add(new JLabel("URL:"));
        inputFields.add(new JTextField());
        inputFields.add(new JLabel("Mappanév:"));
        inputFields.add(new JTextField());

        final JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainButtons.setPreferredSize(new Dimension(350, 50));
        mainButtons.add(new Button(0));
        mainButtons.add(new Button(1));
        mainButtons.add(new Button(2));
        //mainButtons.add(new Button(3)); //Kilépés

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputFields);
        mainPanel.add(mainButtons);

        getContentPane().add(mainPanel);
        this.pack();
        setVisible(true);
    }

    class ExitAction extends WindowAdapter implements ActionListener {

        private final JFrame frame;

        public ExitAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            int option = JOptionPane.showConfirmDialog(frame, "Biztosan kilep a programbol?", "Biztosan kilep?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                frame.dispose();
                System.exit(0);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (0 == JOptionPane.showConfirmDialog(frame, "Biztosan ki szeretne lepni?", "Biztosan kilep?", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                System.exit(0);
            }
        }
    }

    class Button extends JButton {

        public Button(int type) {
            //setPreferredSize(new Dimension(300, 30));
            if (type == 0) {
                setText("Letöltés");
                addActionListener(new Action(type));
            } else if (type == 1) {
                setText("Mezők törlése");
                addActionListener(new Action(type));
            } else if (type == 2) {
                setText("Segítség");
                addActionListener(new Action(type));
            } else if (type == 3) {
                setText("Kilépés");
                addActionListener(new ExitAction(view));
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
                    controller.downloadURL("http://i3.imgchili.net/105494/105494436_femjoy_12096884_016.jpg","105494436_femjoy_12096884_016.jpg");
                }
                else if (1 == type) {
                }
                else if (2 == type) {
                    JOptionPane.showMessageDialog(view, "ImgD\nkitty-kats.net, urlgalleries.net", "Segítség", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}
