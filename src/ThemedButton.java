/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author rafae
 */
public class ThemedButton extends JButton {
    
    public ThemedButton(String text) {
        super(text);
        this.styleComponent();
    }
    
    
    private void styleComponent() {
        this.setForeground(Color.WHITE);
        this.setFont(new Font("SansSerif", Font.PLAIN, 12));
        this.setContentAreaFilled(false);
        this.setOpaque(true);
        this.setBackground(Color.decode(ThemeColors.BLUE_COLOR));
    }
}
