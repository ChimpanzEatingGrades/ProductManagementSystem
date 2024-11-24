/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author davidesfandiary
 */

import java.sql.*;
import javax.swing.JTable;

public class editItems extends javax.swing.JFrame {

    /**
     * Creates new form editSuppliers
     */
    private loggedIn parentPage;
    
    public editItems() {
        initComponents();
        //loadCB();
    }
    
    public editItems(loggedIn parentPage) {
        initComponents();
        //loadItemsCB();
        //itemsCB.setVisible(false);
        parentPage.loadCategoriesCB(itemsCategoryCB);
        //parentPage.loadCategoriesCB(categoriesCB);
        this.parentPage = parentPage;
        
        fillData();
    }
    
    private void fillData(){
        JTable table = parentPage.getItemsTable();
        int row = table.getSelectedRow();

        this.rowName = table.getValueAt(row, 0).toString();
        this.rowCategory = table.getValueAt(row, 1).toString();
        this.rowPrice = table.getValueAt(row, 2).toString();
        this.rowQuantity = table.getValueAt(row, 3).toString();
        this.rowReorder = table.getValueAt(row, 4).toString();
        this.rowDescription = table.getValueAt(row, 5).toString();

        itemsNameTF.setText(this.rowName);
        itemsCategoryCB.getModel().setSelectedItem(this.rowCategory);
        itemsPriceTF.setText(this.rowPrice);
        itemsQuantityTF.setText(this.rowQuantity);
        itemsReorderTF.setText(this.rowReorder);
        itemsDescriptionTF.setText(this.rowDescription);
            
    }

    private String rowName = "";
    private String rowCategory = "";
    private String rowPrice = "";
    private String rowQuantity = "";
    private String rowReorder = "";
    private String rowDescription = "";
    
    private static final String dburl = "jdbc:mysql://localhost:3306/finals";
    private static final String dbusername = "root";
    private static final String dbpassword = "rootroot";

//    public final void loadItemsCB() {
//
//        try {
//        
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
//
//            Statement statement;
//            statement = con.createStatement();
//            ResultSet sqlresult;
//            sqlresult = statement.executeQuery("SELECT * FROM ITEMS");
//            
//            itemsCB.removeAllItems();
//
//            while(sqlresult.next()) {
//                itemsCB.addItem(sqlresult.getString("item_id") + " - " + sqlresult.getString("item_name"));
//            }
//            
//        }    
//            catch (Exception e) {
//            System.out.println("Load Items CB Error: " + e.getMessage());
//        }
//    
//    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        itemsNameTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        itemsCategoryCB = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        itemsPriceTF = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        itemsQuantityTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        itemsReorderTF = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemsDescriptionTF = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        itemsDeleteBTN = new javax.swing.JButton();
        itemsUpdateBTN = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setText("Product Name:");

        itemsNameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsNameTFActionPerformed(evt);
            }
        });

        jLabel7.setText("Category:");

        jLabel8.setText("Unit Price:");

        jLabel9.setText("Quantity:");

        jLabel10.setText("Reorder Level:");

        itemsDescriptionTF.setColumns(20);
        itemsDescriptionTF.setRows(5);
        jScrollPane3.setViewportView(itemsDescriptionTF);

        jLabel5.setText("Description:");

        itemsDeleteBTN.setText("Delete");
        itemsDeleteBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsDeleteBTNActionPerformed(evt);
            }
        });

        itemsUpdateBTN.setText("Update");
        itemsUpdateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsUpdateBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(itemsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel7))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(itemsCategoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(itemsPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(itemsNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemsReorderTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(itemsDeleteBTN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(itemsUpdateBTN)))))
                .addContainerGap(290, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(itemsNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(itemsCategoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(itemsPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(itemsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemsReorderTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemsDeleteBTN)
                    .addComponent(itemsUpdateBTN))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Edit Items Table", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void itemsUpdateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsUpdateBTNActionPerformed
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            Statement statement = con.createStatement();

            String name = itemsNameTF.getText();
            String category = itemsCategoryCB.getSelectedItem().toString();
            String price = itemsPriceTF.getText();
            String quantity = itemsQuantityTF.getText();
            String reorder = itemsReorderTF.getText();
            String description = itemsDescriptionTF.getText();

            String query = "SELECT * FROM Categories WHERE category_name = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, category);

            ResultSet sqlresult;

            sqlresult = preparedStatement.executeQuery();

            String category_id;
            if (sqlresult.next()) {
                category_id = sqlresult.getString("category_id");
            } else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }

            query = "UPDATE Items SET item_name = ?, category_id = ?, unit_price = ?, quantity_on_hand = ?,"
            + "reorder_level = ?, description = ? WHERE item_name = ? AND unit_price = ?";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category_id);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, quantity);
            preparedStatement.setString(5, reorder);
            preparedStatement.setString(6, description);
            preparedStatement.setString(7, this.rowName);
            preparedStatement.setString(8, this.rowPrice);
            preparedStatement.executeUpdate();

            this.setVisible(false);

            System.out.println("1");
            ;
            System.out.println("2");
            parentPage.loadTable();
            System.out.println("3");

            System.out.println("4");

            statement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace(); // This shows the exact line causing the issue
            System.err.println("Items Update Error: " + e.getMessage());
        }

    }//GEN-LAST:event_itemsUpdateBTNActionPerformed

    private void itemsDeleteBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsDeleteBTNActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            Statement statement = con.createStatement();
            System.out.println("Tes1");

            String query = "DELETE FROM Items WHERE item_name = ? AND description = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, this.rowName);
            preparedStatement.setString(2, this.rowDescription);
            preparedStatement.executeUpdate();

            this.setVisible(false);

            System.out.println("Tes3");
            parentPage.loadTable();
            System.out.println("Tes4");
            System.out.println("Tes5");

            statement.close();
            con.close();

        } catch(Exception e) {
            System.err.println("Items Delete Error: " + e.getMessage());
        }
    }//GEN-LAST:event_itemsDeleteBTNActionPerformed

    private void itemsNameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsNameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsNameTFActionPerformed

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
            java.util.logging.Logger.getLogger(editSuppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(editSuppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(editSuppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(editSuppliers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new editSuppliers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> itemsCategoryCB;
    private javax.swing.JButton itemsDeleteBTN;
    private javax.swing.JTextArea itemsDescriptionTF;
    private javax.swing.JTextField itemsNameTF;
    private javax.swing.JTextField itemsPriceTF;
    private javax.swing.JTextField itemsQuantityTF;
    private javax.swing.JTextField itemsReorderTF;
    private javax.swing.JButton itemsUpdateBTN;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
