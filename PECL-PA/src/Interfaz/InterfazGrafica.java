
package Interfaz;

import Clases.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JTextArea;


public class InterfazGrafica extends javax.swing.JFrame {
    
    private Gestor gestor;
    private Registro log;
    private ExecutorService ejecutor = Executors.newCachedThreadPool();
    private Map<String, JTextArea> areasRefugio, areasGestor, areasExterior;
    private Controlador controladorRefugio, controladorGestor, controladorExterior;
    
    private boolean encendido = false;

    public InterfazGrafica() {
        //Inicia los elementos del frame
        initComponents();
        //Asignamos las areas a sus elementos de la interfaz
        this.areasExterior = Map.of(
            "HumanosE1", HumanosE1,
            "HumanosE2", HumanosE2,
            "HumanosE3", HumanosE3,
            "HumanosE4", HumanosE4,
            "ZombisE1", ZombisE1,
            "ZombisE2", ZombisE2,
            "ZombisE3", ZombisE3,
            "ZombisE4", ZombisE4
        );
        this.areasGestor = Map.of(
            "Tunel1", Tunel1,
            "Tunel2", Tunel2,
            "Tunel3", Tunel3,
            "Tunel4", Tunel4,
            "Puerta1", Puerta1,
            "Puerta2", Puerta2,
            "Puerta3", Puerta3,
            "Puerta4", Puerta4
        );
        this.areasRefugio = Map.of(
            "ZonaComedero", ZonaComedero,
            "ZonaComun", ZonaComun,
            "ZonaDescanso", ZonaDescanso
        );
        //Iniciamos los controladores
        controladorRefugio = new Controlador(areasRefugio);
        controladorGestor = new Controlador(areasGestor);
        controladorExterior = new Controlador(areasExterior);
        //Iniciamos el Registro
        log = new Registro();
        log.reiniciarRegistro();
        //Iniciamos el gestor
        gestor = new Gestor(log, ejecutor, controladorGestor, controladorRefugio, controladorExterior);
    }
    
    public Gestor getGestor(){
        return this.gestor;
    }
    public ExecutorService getEjecutor(){
        return ejecutor;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BtInicio = new javax.swing.JButton();
        BtPausa = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ZonaComun = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        ZonaDescanso = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        ZonaComedero = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        Tunel1 = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        Tunel2 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        Tunel3 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        Tunel4 = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        HumanosE1 = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        ZombisE1 = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        HumanosE2 = new javax.swing.JTextArea();
        jScrollPane15 = new javax.swing.JScrollPane();
        ZombisE2 = new javax.swing.JTextArea();
        jScrollPane16 = new javax.swing.JScrollPane();
        HumanosE3 = new javax.swing.JTextArea();
        jScrollPane17 = new javax.swing.JScrollPane();
        ZombisE3 = new javax.swing.JTextArea();
        jScrollPane18 = new javax.swing.JScrollPane();
        HumanosE4 = new javax.swing.JTextArea();
        jScrollPane19 = new javax.swing.JScrollPane();
        ZombisE4 = new javax.swing.JTextArea();
        jScrollPane20 = new javax.swing.JScrollPane();
        Puerta1 = new javax.swing.JTextArea();
        jScrollPane21 = new javax.swing.JScrollPane();
        Puerta2 = new javax.swing.JTextArea();
        jScrollPane22 = new javax.swing.JScrollPane();
        Puerta3 = new javax.swing.JTextArea();
        jScrollPane23 = new javax.swing.JScrollPane();
        Puerta4 = new javax.swing.JTextArea();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TÚNELES");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("EXTERIOR");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("REFUGIO");

        BtInicio.setText("COMENZAR");
        BtInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtInicioActionPerformed(evt);
            }
        });

        BtPausa.setSelected(true);
        BtPausa.setText("PARAR");
        BtPausa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtPausaActionPerformed(evt);
            }
        });

        ZonaComun.setColumns(20);
        ZonaComun.setRows(5);
        jScrollPane1.setViewportView(ZonaComun);

        ZonaDescanso.setColumns(20);
        ZonaDescanso.setRows(5);
        jScrollPane2.setViewportView(ZonaDescanso);

        ZonaComedero.setColumns(20);
        ZonaComedero.setRows(5);
        jScrollPane3.setViewportView(ZonaComedero);

        Tunel1.setColumns(20);
        Tunel1.setRows(5);
        jScrollPane8.setViewportView(Tunel1);

        Tunel2.setColumns(20);
        Tunel2.setRows(5);
        jScrollPane9.setViewportView(Tunel2);

        Tunel3.setColumns(20);
        Tunel3.setRows(5);
        jScrollPane10.setViewportView(Tunel3);

        Tunel4.setColumns(20);
        Tunel4.setRows(5);
        jScrollPane11.setViewportView(Tunel4);

        HumanosE1.setColumns(20);
        HumanosE1.setRows(5);
        jScrollPane12.setViewportView(HumanosE1);

        ZombisE1.setColumns(20);
        ZombisE1.setRows(5);
        jScrollPane13.setViewportView(ZombisE1);

        HumanosE2.setColumns(20);
        HumanosE2.setRows(5);
        jScrollPane14.setViewportView(HumanosE2);

        ZombisE2.setColumns(20);
        ZombisE2.setRows(5);
        jScrollPane15.setViewportView(ZombisE2);

        HumanosE3.setColumns(20);
        HumanosE3.setRows(5);
        jScrollPane16.setViewportView(HumanosE3);

        ZombisE3.setColumns(20);
        ZombisE3.setRows(5);
        jScrollPane17.setViewportView(ZombisE3);

        HumanosE4.setColumns(20);
        HumanosE4.setRows(5);
        jScrollPane18.setViewportView(HumanosE4);

        ZombisE4.setColumns(20);
        ZombisE4.setRows(5);
        jScrollPane19.setViewportView(ZombisE4);

        Puerta1.setColumns(20);
        Puerta1.setRows(5);
        jScrollPane20.setViewportView(Puerta1);

        Puerta2.setColumns(20);
        Puerta2.setRows(5);
        jScrollPane21.setViewportView(Puerta2);

        Puerta3.setColumns(20);
        Puerta3.setRows(5);
        jScrollPane22.setViewportView(Puerta3);

        Puerta4.setColumns(20);
        Puerta4.setRows(5);
        jScrollPane23.setViewportView(Puerta4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane10)
                            .addComponent(jScrollPane11)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(BtInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtPausa, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
            .addGroup(layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(147, 147, 147)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(118, 118, 118))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(150, 150, 150))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jScrollPane2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane14)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane16)
                                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane19)
                                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtPausa)
                            .addComponent(BtInicio))
                        .addGap(36, 36, 36))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtPausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtPausaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtPausaActionPerformed

    private void BtInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtInicioActionPerformed
        if (!encendido){
            encendido = true;
            Barbaros paciente_0 = new Barbaros(getGestor());
            getEjecutor().execute(paciente_0);
            new Thread(()->{ //Lo hacemos en un hilo a parte para que no se congele el hilo de interfaz
                getGestor().generadorHumanos();
            }).start();
            BtInicio.setText("Apagar");
        }  else {
            /*
            encendido = false;
            getEjecutor().shutdown(); // Finaliza gradualmente
                try {
                    if (!getEjecutor().awaitTermination(5, TimeUnit.SECONDS)){
                        getEjecutor().shutdownNow(); // Fuerza la terminación
                    if (!getEjecutor().awaitTermination(60, TimeUnit.SECONDS)) 
                        System.err.println("El pool no terminó");
                    } 
                } catch (InterruptedException ie) {
                    getEjecutor().shutdownNow();
                }*/
            BtInicio.setText("");
        }
    }//GEN-LAST:event_BtInicioActionPerformed

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazGrafica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazGrafica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazGrafica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazGrafica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() 
            {   public void run() {
                    InterfazGrafica gui = new InterfazGrafica();
                    gui.setVisible(true);
                }
            }
        );
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtInicio;
    private javax.swing.JToggleButton BtPausa;
    private javax.swing.JTextArea HumanosE1;
    private javax.swing.JTextArea HumanosE2;
    private javax.swing.JTextArea HumanosE3;
    private javax.swing.JTextArea HumanosE4;
    private javax.swing.JTextArea Puerta1;
    private javax.swing.JTextArea Puerta2;
    private javax.swing.JTextArea Puerta3;
    private javax.swing.JTextArea Puerta4;
    private javax.swing.JTextArea Tunel1;
    private javax.swing.JTextArea Tunel2;
    private javax.swing.JTextArea Tunel3;
    private javax.swing.JTextArea Tunel4;
    private javax.swing.JTextArea ZombisE1;
    private javax.swing.JTextArea ZombisE2;
    private javax.swing.JTextArea ZombisE3;
    private javax.swing.JTextArea ZombisE4;
    private javax.swing.JTextArea ZonaComedero;
    private javax.swing.JTextArea ZonaComun;
    private javax.swing.JTextArea ZonaDescanso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    // End of variables declaration//GEN-END:variables
}
