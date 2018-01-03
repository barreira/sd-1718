/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 *
 * @author rafae
 */
public class MatchPanel extends javax.swing.JPanel implements Observer {

    private final OverwatchStub client;
    private final Player player;
    private List<String> team1;
    private List<String> team2;
    private List<String> heroes1;
    private List<String> heroes2;
    private List<JLabel> labelsTeam1;
    private List<JLabel> labelsTeam2;
    private List<JLabel> labelsHeroes1;
    private List<JLabel> labelsHeroes2;
    private boolean matchStarted;
    private MatchThread matchThread;
    
    
    private class MatchThread extends Thread {
        
        @Override
        public void run() {
            client.listen();
        }
    }
    
 
    public MatchPanel(OverwatchStub client, Player player) {
        this.client = client;
        this.player = player;
        this.client.addObserver(this);
        matchStarted = false;
        matchThread = null;
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        heroes1 = new ArrayList<>();
        heroes2 = new ArrayList<>();
        labelsTeam1 = new ArrayList<>();
        labelsTeam2 = new ArrayList<>();
        labelsHeroes1 = new ArrayList<>();
        labelsHeroes2 = new ArrayList<>();
        initComponents();
        this.styleComponents();
    }

    
    public boolean matchStarted() {
        return matchStarted;
    }
    
    
    @Override
    public void update(Observable observable, Object object) {
        String response = client.getResponse();
        System.out.println(response);
        
        if (response.contains("MATCH")) {
            String[] teams = response.split("\\|");
            String[] team1Parts = teams[0].split(":");
            String[] team2Parts = teams[1].split(":");
            
            for (int i = 1; i < team1Parts.length; i++) {
                if (i == 1) {
                    jLabel4.setText(team1Parts[i]);
                }
                if (i == 2) {
                    jLabel5.setText(team1Parts[i]);
                }
                if (i == 3) {
                    jLabel6.setText(team1Parts[i]);
                }
                if (i == 4) {
                    jLabel7.setText(team1Parts[i]);
                }
                if (i == 5) {
                    jLabel8.setText(team1Parts[i]);
                }
            }
            
            for (int i = 0; i < team2Parts.length; i++) {
                if (i == 0) {
                    jLabel14.setText(team2Parts[i]);
                }
                if (i == 1) {
                    jLabel16.setText(team2Parts[i]);
                }
                if (i == 2) {
                    jLabel18.setText(team2Parts[i]);
                }
                if (i == 3) {
                    jLabel20.setText(team2Parts[i]);
                }
                if (i == 4) {
                    jLabel22.setText(team2Parts[i]);
                }
            }
            
            if (!matchStarted) {
                matchThread = new MatchThread();
                matchThread.start();
            }
        }
        else if (response.contains("OK")) {
            String[] parts = response.split(":");
            
            if (jLabel4.getText().startsWith(parts[1])) {
                jLabel4.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel5.getText().startsWith(parts[1])) {
                jLabel5.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel6.getText().startsWith(parts[1])) {
                jLabel6.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel7.getText().startsWith(parts[1])) {
                jLabel7.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel8.getText().startsWith(parts[1])) {
                jLabel8.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel14.getText().startsWith(parts[1])) {
                jLabel14.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel16.getText().startsWith(parts[1])) {
                jLabel16.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel18.getText().startsWith(parts[1])) {
                jLabel18.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel20.getText().startsWith(parts[1])) {
                jLabel20.setText(parts[1] + " - " + parts[2]);
            }
            else if (jLabel22.getText().startsWith(parts[1])) {
                jLabel22.setText(parts[1] + " - " + parts[2]);
            }
        } else if (response.startsWith("START")) {
            String[] teams = response.split("\\|");
            String[] team1Parts = teams[0].split(":");
            String[] team2Parts = teams[1].split(":");
            
            for (int i = 1; i < team1Parts.length; i++) {
                String[] parts = team1Parts[i].split(",");
                
                if (i == 1) {
                    jLabel4.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 2) {
                    jLabel5.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 3) {
                    jLabel6.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 4) {
                    jLabel7.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 5) {
                    jLabel8.setText(parts[0] + " - " + parts[1]);
                }
            }
            
            for (int i = 0; i < team2Parts.length; i++) {
                String[] parts = team2Parts[i].split(",");
                
                if (i == 0) {
                    jLabel14.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 1) {
                    jLabel16.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 2) {
                    jLabel18.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 3) {
                    jLabel20.setText(parts[0] + " - " + parts[1]);
                }
                if (i == 4) {
                    jLabel22.setText(parts[0] + " - " + parts[1]);
                }
            }
        } else if (response.contains("VICTORY")) {
            client.deleteObserver(this);
            SwingUtilities.windowForComponent(this).dispose();
            PlayerPage p = new PlayerPage(client, player);
            p.setVisible(true);
            ErrorMessage e = new ErrorMessage("VICTORY!");
            e.setVisible(true);
        }
        else if (response.contains("DEFEAT")) {
            client.deleteObserver(this);
            SwingUtilities.windowForComponent(this).dispose();
            PlayerPage p = new PlayerPage(client, player);
            p.setVisible(true);
            ErrorMessage e = new ErrorMessage("DEFEAT...");
            e.setVisible(true);
        }
        else if (response.contains("ABORT")) {
            client.deleteObserver(this);
            SwingUtilities.windowForComponent(this).dispose();
            PlayerPage p = new PlayerPage(client, player);
            p.setVisible(true);
            ErrorMessage e = new ErrorMessage("Match Aborted!");
            e.setVisible(true);
        }
        
        this.repaint();
        this.revalidate();
    }
    
    
  
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        heroesPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(45, 54, 76));
        setMinimumSize(new java.awt.Dimension(548, 700));
        setPreferredSize(new java.awt.Dimension(548, 700));

        heroesPanel.setBackground(new java.awt.Color(45, 54, 76));
        heroesPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 43, 62)));
        heroesPanel.setForeground(new java.awt.Color(255, 255, 255));
        heroesPanel.setMinimumSize(new java.awt.Dimension(100, 700));
        heroesPanel.setPreferredSize(new java.awt.Dimension(102, 700));

        javax.swing.GroupLayout heroesPanelLayout = new javax.swing.GroupLayout(heroesPanel);
        heroesPanel.setLayout(heroesPanelLayout);
        heroesPanelLayout.setHorizontalGroup(
            heroesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        heroesPanelLayout.setVerticalGroup(
            heroesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(88, 144, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Player 1");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Player 2");

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Player 3");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Player 4");

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Player 5");

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(80, 80, 80)
                                        .addComponent(jLabel12))
                                    .addComponent(jLabel7))
                                .addGap(81, 81, 81)
                                .addComponent(jLabel13)
                                .addGap(80, 80, 80))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(88, 144, 255));

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Player 1");

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Player 3");

        jLabel17.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));

        jLabel18.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Player 4");

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Player 5");

        jLabel21.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Player 2");

        jLabel23.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addGap(129, 129, 129)
                        .addComponent(jLabel17))
                    .addComponent(jLabel16)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(92, 92, 92)
                        .addComponent(jLabel21))
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(39, 39, 39))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)))
                    .addComponent(jLabel22))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jLabel17)
                        .addComponent(jLabel19)
                        .addComponent(jLabel21)
                        .addComponent(jLabel23))
                    .addComponent(jLabel20))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("VS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(209, 209, 209)))
                .addComponent(heroesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(heroesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 336, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void styleComponents() {
        for (int i = 0; i < Overwatch.NUM_PLAYERS / 2; i++) {
            team1.add("");
            team2.add("");
            heroes1.add("");
            heroes2.add("");
        }
        
        
        labelsTeam1.add(jLabel4);
        labelsTeam1.add(jLabel5);
        labelsTeam1.add(jLabel6);
        labelsTeam1.add(jLabel7);
        labelsTeam1.add(jLabel8);
        
        labelsHeroes1.add(jLabel9);
        labelsHeroes1.add(jLabel10);
        labelsHeroes1.add(jLabel11);
        labelsHeroes1.add(jLabel12);
        labelsHeroes1.add(jLabel13);
        
        labelsTeam2.add(jLabel14);
        labelsTeam2.add(jLabel16);
        labelsTeam2.add(jLabel18);
        labelsTeam2.add(jLabel20);
        labelsTeam2.add(jLabel22);
        
        labelsHeroes2.add(jLabel15);
        labelsHeroes2.add(jLabel17);
        labelsHeroes2.add(jLabel19);
        labelsHeroes2.add(jLabel21);
        labelsHeroes2.add(jLabel23);
        
        heroesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        int i = 0;
        
        for (String hero : Overwatch.heroes) {
            c.gridy = i;
            HeroPanel h = new HeroPanel(hero);
            h.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        client.selectHero(h.getHero());
                    }
            });
                    
            heroesPanel.add(h, c);
            i++;
        }
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel heroesPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
