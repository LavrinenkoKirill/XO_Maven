package org.suai.View;

import javax.swing.*;
import java.awt.*;

public class DisconnectMenu extends JDialog {
    public DisconnectMenu(GameView view) {
        super(view,"Your opponent disconnected");
        this.setLayout(new FlowLayout());
        this.add(new JLabel("Your opponent disconnected"));
        this.setSize(600, 600);
        this.setVisible(true);

    }
}
