/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package JFrame;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
/**
 *
 * @author Ismail
 */
public class Payements extends javax.swing.JFrame {

    /**
     * Creates new form Payments
     */
    public Payements() {
        initComponents();
    }
    
    public Payements(String customerName, java.sql.Date checkInDate, java.sql.Date checkOutDate, 
               String roomNumber, String roomType, double originalPrice, double discountedPrice) {
    initComponents();
    this.customerName = customerName;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.originalPrice = originalPrice;
    this.discountedPrice = discountedPrice;

    // Display reservation details on the Payment page
    txtcname.setText(customerName);
    txtcheckin.setText(checkInDate.toString());
    txtcheckout.setText(checkOutDate.toString());
    txtroomno.setText(roomNumber); 
    txtroomtype.setText(roomType);
    txtoriginalprice.setText(String.format("PKR %.2f", originalPrice));
    txtdiscountprice.setText(String.format("PKR %.2f", discountedPrice));
    
    
}

    private String customerName;
    private java.sql.Date checkInDate;
    private java.sql.Date checkOutDate;
    private String roomNumber;
    private String roomType;
    private double originalPrice;
    private String offerApplied;
    private double discountedPrice;

    private boolean validatePaymentInformation() {
        // Check if payment method is selected
        if (cbpaymentmethod.getSelectedItem() == null || cbpaymentmethod.getSelectedItem().toString().equals("Select payment method...")) {
            JOptionPane.showMessageDialog(this, "Please select payment method.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
   private void insertReservationDataPending() {
    String sql = "INSERT INTO Reservations (customer_name, check_in_date, check_out_date, room_number, room_type, original_price,  discounted_price, payment_method, payment_status, discount_applied) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Pending', ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "WJ28@krhps");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtcname.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtcheckin.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtcheckout.getText()));
        pstmt.setString(4, txtroomno.getText()); // Updated to use room number
        pstmt.setString(5, txtroomtype.getText()); // Assuming you have a text field for room type
        pstmt.setDouble(6, Double.parseDouble(txtoriginalprice.getText().replace("PKR ", "")));
        pstmt.setDouble(7, Double.parseDouble(txtdiscountprice.getText().replace("PKR ", "")));
        pstmt.setString(8, cbpaymentmethod.getSelectedItem().toString());
        pstmt.setString(9, "0"); // Provide a value for the discount_applied field

        pstmt.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving reservation details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void insertReservationDataComplete() {
    String sql = "INSERT INTO Reservations (customer_name, check_in_date, check_out_date, room_number, room_type, original_price,  discounted_price, payment_method, payment_status, discount_applied) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Completed', ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "WJ28@krhps");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtcname.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtcheckin.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtcheckout.getText()));
        pstmt.setString(4, txtroomno.getText()); // Updated to use room number
        pstmt.setString(5, txtroomtype.getText()); // Assuming you have a text field for room type
        pstmt.setDouble(6, Double.parseDouble(txtoriginalprice.getText().replace("PKR ", "")));
        pstmt.setDouble(7, Double.parseDouble(txtdiscountprice.getText().replace("PKR ", "")));
        pstmt.setString(8, cbpaymentmethod.getSelectedItem().toString());
        pstmt.setString(9, "0"); // Provide a value for the discount_applied field

        pstmt.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving reservation details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void updateReservationStatusToCompleted() {
    String sql = "UPDATE Reservations SET payment_status = 'Completed' WHERE customer_name = ? AND check_in_date = ? AND check_out_date = ? AND room_number = ? AND payment_status = 'Pending'";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "WJ28@krhps");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtcname.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtcheckin.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtcheckout.getText()));
        pstmt.setString(4, txtroomno.getText()); // Updated to use room number
        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Payment completed, reservation status updated.");
        } else {
            JOptionPane.showMessageDialog(this, "No matching reservation found or payment is already completed.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating reservation status. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private boolean reservationExists() {
    String sql = "SELECT COUNT(*) FROM Reservations WHERE customer_name = ? AND check_in_date = ? AND check_out_date = ? AND room_number = ? AND payment_status = 'Pending'";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "WJ28@krhps");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtcname.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtcheckin.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtcheckout.getText()));
        pstmt.setString(4, txtroomno.getText()); // Updated to use room number
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error checking reservation status.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbpaymentmethod = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtcname = new app.bolivia.swing.JCTextField();
        txtcheckin = new app.bolivia.swing.JCTextField();
        txtcheckout = new app.bolivia.swing.JCTextField();
        txtroomno = new app.bolivia.swing.JCTextField();
        txtroomtype = new app.bolivia.swing.JCTextField();
        txtoriginalprice = new app.bolivia.swing.JCTextField();
        txtdiscountprice = new app.bolivia.swing.JCTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payement Information");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("It is a fraud to borrow what you are unable to buy");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, 320, 20));

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Rewind_48px.png"))); // NOI18N
        jLabel9.setText("Back");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("X");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1350, 0, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 120));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel4.setText("Payment Method :");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, -1, -1));

        cbpaymentmethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Payment Method........", "Credit Card", "Payoneer", "Skrill", "Jazz Cash", "Easy Paisa", "Bank Transfer" }));
        jPanel2.add(cbpaymentmethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 250, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 80));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel5.setText("Payment Method :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel6.setText("Payment Method :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel7.setText("Payment Method :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 24)); // NOI18N
        jLabel8.setText("Reservation Check & Payment");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));
        jPanel1.add(txtcname, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 280, 40));
        jPanel1.add(txtcheckin, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 280, 40));
        jPanel1.add(txtcheckout, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 280, 40));
        jPanel1.add(txtroomno, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, 280, 40));
        jPanel1.add(txtroomtype, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 280, 40));
        jPanel1.add(txtoriginalprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 280, 40));
        jPanel1.add(txtdiscountprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 280, 40));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Search");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 30, 100, 40));

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Confirm Reservation");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 430, -1, 50));

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 24)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Pay & Confirm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, -1, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 800, 540));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/first.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 120, 570, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
       System.exit(0);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
       HomePage home=new HomePage();
       home.setVisible(true);
       dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (validatePaymentInformation()) {
            insertReservationDataPending();
            JOptionPane.showMessageDialog(this, "Reservation confirmed with Pending status.", "Success", JOptionPane.INFORMATION_MESSAGE);
            HomePage page=new HomePage();
            page.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (validatePaymentInformation()) {
            if (reservationExists()) {
                updateReservationStatusToCompleted();
            } else {
                insertReservationDataComplete();
            }
            JOptionPane.showMessageDialog(this, "Reservation and Payment completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Payements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payements().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbpaymentmethod;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private app.bolivia.swing.JCTextField txtcheckin;
    private app.bolivia.swing.JCTextField txtcheckout;
    private app.bolivia.swing.JCTextField txtcname;
    private app.bolivia.swing.JCTextField txtdiscountprice;
    private app.bolivia.swing.JCTextField txtoriginalprice;
    private app.bolivia.swing.JCTextField txtroomno;
    private app.bolivia.swing.JCTextField txtroomtype;
    // End of variables declaration//GEN-END:variables
}
