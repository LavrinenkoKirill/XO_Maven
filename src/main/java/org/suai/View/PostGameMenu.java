package org.suai.View;


import javax.swing.*;
import java.awt.*;

public class PostGameMenu extends JDialog {
    public PostGameMenu(GameView view, String str) {
        super(view, "Post game menu");
        this.setLayout(new FlowLayout());
        this.add(new JLabel(str));
        this.setSize(300, 300);
        this.setVisible(true);

    }
}