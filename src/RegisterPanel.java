/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

/**
 *
 * @author rafae
 */
public class RegisterPanel extends javax.swing.JPanel {

    private final OverwatchStub client;
    
 
    public RegisterPanel(OverwatchStub client) {
        this.client = client;
        initComponents();
        this.styleComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        confirmField = new javax.swing.JPasswordField();
        userLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        confirmLabel = new javax.swing.JLabel();
        registerButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(45, 54, 76));

        userTextField.setBackground(new java.awt.Color(45, 54, 76));
        userTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        userTextField.setForeground(new java.awt.Color(255, 255, 255));
        userTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 43, 62)));
        userTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        userTextField.setSelectionColor(new java.awt.Color(19, 199, 174));

        passwordField.setBackground(new java.awt.Color(45, 54, 76));
        passwordField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        passwordField.setForeground(new java.awt.Color(255, 255, 255));
        passwordField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 43, 62)));
        passwordField.setCaretColor(new java.awt.Color(255, 255, 255));
        passwordField.setSelectionColor(new java.awt.Color(19, 199, 174));

        confirmField.setBackground(new java.awt.Color(45, 54, 76));
        confirmField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        confirmField.setForeground(new java.awt.Color(255, 255, 255));
        confirmField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 43, 62)));
        confirmField.setCaretColor(new java.awt.Color(255, 255, 255));
        confirmField.setSelectionColor(new java.awt.Color(19, 199, 174));

        userLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        userLabel.setForeground(new java.awt.Color(255, 255, 255));
        userLabel.setText("UserName");

        passwordLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(255, 255, 255));
        passwordLabel.setText("Password");

        confirmLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        confirmLabel.setForeground(new java.awt.Color(255, 255, 255));
        confirmLabel.setText("Confirmar Password");

        registerButton.setBackground(new java.awt.Color(88, 144, 255));
        registerButton.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        registerButton.setForeground(new java.awt.Color(255, 255, 255));
        registerButton.setText("Registar");
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerButton.setOpaque(true);
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registerButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userTextField)
                    .addComponent(passwordField)
                    .addComponent(confirmField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userLabel)
                            .addComponent(passwordLabel)
                            .addComponent(confirmLabel)
                            .addComponent(registerButton))
                        .addGap(0, 213, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userLabel)
                .addGap(13, 13, 13)
                .addComponent(userTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(confirmLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(registerButton)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registerButtonMouseClicked
        Player p;
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword()); 
        
        if (!pass.equals(confirm)) {
            ErrorMessage error = 
                    new ErrorMessage("As passwords não coincidem!");
            error.setVisible(true);
        }
        else {
            try {
                p = client.signup(userTextField.getText(), pass);

                PlayerPage s = new PlayerPage(client, p);
                s.setVisible(true);
                SwingUtilities.windowForComponent(this).dispose();
            } catch(InvalidAccountException e) {
                ErrorMessage error = new ErrorMessage("Utilizador inválido!");
                error.setVisible(true);
            }
        }
    }//GEN-LAST:event_registerButtonMouseClicked


    private void styleComponents() {
        userTextField.setBorder(BorderFactory.createCompoundBorder(
            userTextField.getBorder(), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            passwordField.getBorder(), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            confirmField.getBorder(), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField confirmField;
    private javax.swing.JLabel confirmLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField userTextField;
    // End of variables declaration//GEN-END:variables
}
