
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.util.*;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author dsesfandiary
 */
public class loggedIn extends javax.swing.JFrame {

    /**
     * Creates new form loggedIn
     */
    
    private static final String dburl = "jdbc:mysql://localhost:3306/finals";
    private static final String dbusername = "root";
    private static final String dbpassword = "rootroot";
    
    public loggedIn() {
        initComponents();
        loadTable();
        loadCategoriesCB(itemsCategoryCB);
        loadItemsCB(transactionsItemCB);
        loadSuppliersCB(pOrdersSuppliersCB);
    }
    
    public final void loadItemsCB(JComboBox CB){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);

            Statement statement;
            statement = con.createStatement();
            ResultSet sqlresult;
            sqlresult = statement.executeQuery("SELECT * FROM Items");
            
            CB.removeAllItems();

            while(sqlresult.next()) {
                CB.addItem(sqlresult.getString("item_name"));
            }
            
        }    
            catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    public final void loadTable() {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            
            Statement statement = con.createStatement();
            ResultSet sqlresult = statement.executeQuery("select * from suppliers");
            
            DefaultTableModel tableModel = new DefaultTableModel(new String[] 
                    {"Name", "Contact Info"}, 0)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                            return false;//This causes all cells to be not editable
                        }
                    };
           
            
            
            
            
            while(sqlresult.next()) {
                tableModel.addRow(new Object[] 
                    {sqlresult.getString("supplier_name"), 
                        sqlresult.getString("contact_info")});
                //suppliersCB.addItem(sqlresult.getString("supplier_id"));
            }
            
            suppliersTable.setModel(tableModel);
  
            
            sqlresult.close();
            statement.close();
            con.close();
            
            checkRestockLevels();
            
        } catch (Exception e) {
            System.err.println("Supplier Error: " + e.getMessage());
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            
            DefaultTableModel tableModel = new DefaultTableModel(new String[] 
                    {"Name", "Category", "Unit Price", "Quantity", "Reorder Level", "Desc."}, 0)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                            return false;//This causes all cells to be not editable
                        }
                    };
            
            itemsTable.setModel(tableModel);
            
            
            
            Statement statement = con.createStatement();
            ResultSet sqlresult = statement.executeQuery("Select * from Items INNER JOIN Categories on Items.category_id = Categories.category_id;");
            
            while(sqlresult.next()) {
                tableModel.addRow(new Object[] 
                    {sqlresult.getString("item_name"), 
                        sqlresult.getString("category_name"), sqlresult.getString("unit_price"), 
                        sqlresult.getString("quantity_on_hand"), sqlresult.getString("reorder_level"), 
                        sqlresult.getString("description")});
                //cmbLastName.addItem(sqlresult.getString("student_id"));
            }
            
   
            
            sqlresult.close();
            statement.close();
            con.close();

            
        } catch (Exception e) {
            System.err.println("Items Error: " + e.getMessage());
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            
            
            Statement statement = con.createStatement();
            ResultSet sqlresult = statement.executeQuery("select * from Transactions INNER JOIN Items on Transactions.item_id = Items.item_id");
            
            DefaultTableModel tableModel = new DefaultTableModel(new String[] 
                    {"Item Name", "Date", "Quantity", "Type", "Notes"}, 0)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                            return false;//This causes all cells to be not editable
                        }
                    };
            
            transactionsTable.setModel(tableModel);
            
            while(sqlresult.next()) {
                tableModel.addRow(new Object[] 
                    {sqlresult.getString("item_name"), 
                        sqlresult.getString("transaction_date"), sqlresult.getString("quantity"), 
                        sqlresult.getString("transaction_type"), sqlresult.getString("notes"), 
                        });
                //cmbLastName.addItem(sqlresult.getString("student_id"));
            }
            
   
            
            sqlresult.close();
            statement.close();
            con.close();

            
        } catch (Exception e) {
            System.err.println("Transactions Error: " + e.getMessage());
        }
        
        
    }
    
    public JComboBox getItemsCategoryCB(){
        return this.itemsCategoryCB;
    }
    
    public JComboBox getPOrdersSuppliersCB(){
        return this.pOrdersSuppliersCB;
    }
    
    public static void executeTransaction(String orderQuan, String type, String item_id, boolean undo){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Items WHERE item_id = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, item_id);

            ResultSet sqlresult;

            sqlresult = preparedStatement.executeQuery();
            
            int itemQuantity;
            int newItemQuantity = 0;
            int orderQuantity = Integer.parseInt(orderQuan);
            
            if (sqlresult.next()) {
                itemQuantity = sqlresult.getInt("quantity_on_hand");
            } 
            else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }

            if (!undo){
                if (type.equalsIgnoreCase("purchase")){
                    newItemQuantity = itemQuantity + orderQuantity;               
                }
                else if (type.equalsIgnoreCase("sale")){
                    newItemQuantity = itemQuantity - orderQuantity;
                }
            }
            else{
                if (type.equalsIgnoreCase("purchase")){
                    newItemQuantity = itemQuantity - orderQuantity;               
                }
                else if (type.equalsIgnoreCase("sale")){
                    newItemQuantity = itemQuantity + orderQuantity;
                }
            }
            
            query = "UPDATE Items SET quantity_on_hand = ?"
                    + "WHERE item_id = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newItemQuantity + "");
            preparedStatement.setString(2, item_id);
            preparedStatement.executeUpdate();
            
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(); // This shows the exact line causing the issue
            System.err.println("Items Update Error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        suppliersTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        suppliersNameTF = new javax.swing.JTextField();
        suppliersContactTF = new javax.swing.JTextField();
        suppliersCreateBTN = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        suppliersTable = new javax.swing.JTable();
        suppliersEditBTN = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        itemsTab = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        itemsNameTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();
        itemsCreateBTN = new javax.swing.JButton();
        itemsEditBTN = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        itemsPriceTF = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        itemsQuantityTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        itemsReorderTF = new javax.swing.JTextField();
        itemsCategoryCB = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemsDescriptionTF = new javax.swing.JTextArea();
        categoriesEditBTN = new javax.swing.JButton();
        transactionsTab = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        transactionsTable = new javax.swing.JTable();
        transactionsItemCB = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        transactionsCreateBTN = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        transactionsEditBTN = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        transactionsNotesTF = new javax.swing.JTextArea();
        transactionsQuantityTF = new javax.swing.JTextField();
        transactionsTypeCB = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        pOrdersSuppliersCB = new javax.swing.JComboBox<>();
        createPOrderBTN = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Supplier Name:");

        jLabel2.setText("Contact Info:");

        suppliersNameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliersNameTFActionPerformed(evt);
            }
        });

        suppliersCreateBTN.setText("Create");
        suppliersCreateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliersCreateBTNActionPerformed(evt);
            }
        });

        suppliersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(suppliersTable);

        suppliersEditBTN.setText("Edit");
        suppliersEditBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliersEditBTNActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Update or delete entries.");

        javax.swing.GroupLayout suppliersTabLayout = new javax.swing.GroupLayout(suppliersTab);
        suppliersTab.setLayout(suppliersTabLayout);
        suppliersTabLayout.setHorizontalGroup(
            suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliersTabLayout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(suppliersTabLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(suppliersNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(suppliersTabLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(suppliersCreateBTN)
                            .addComponent(suppliersContactTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suppliersEditBTN)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(15, 15, 15))
        );
        suppliersTabLayout.setVerticalGroup(
            suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersTabLayout.createSequentialGroup()
                .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliersTabLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(suppliersNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(suppliersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(suppliersContactTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(suppliersCreateBTN))
                    .addGroup(suppliersTabLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(suppliersEditBTN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        tabs.addTab("Suppliers", suppliersTab);

        jLabel4.setText("Product Name:");

        itemsNameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsNameTFActionPerformed(evt);
            }
        });

        jLabel5.setText("Description:");

        itemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Category", "Unit Price", "Quantity", "Reorder Level", "Description"
            }
        ));
        jScrollPane2.setViewportView(itemsTable);

        itemsCreateBTN.setText("Create");
        itemsCreateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsCreateBTNActionPerformed(evt);
            }
        });

        itemsEditBTN.setText("Edit");
        itemsEditBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsEditBTNActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Update or delete entries.");

        jLabel7.setText("Category:");

        jLabel8.setText("Unit Price:");

        jLabel9.setText("Quantity:");

        jLabel10.setText("Reorder Level:");

        itemsDescriptionTF.setColumns(20);
        itemsDescriptionTF.setRows(5);
        jScrollPane3.setViewportView(itemsDescriptionTF);

        categoriesEditBTN.setText("Edit");
        categoriesEditBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesEditBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemsTabLayout = new javax.swing.GroupLayout(itemsTab);
        itemsTab.setLayout(itemsTabLayout);
        itemsTabLayout.setHorizontalGroup(
            itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemsTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(itemsTabLayout.createSequentialGroup()
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, itemsTabLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(itemsNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(itemsTabLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(itemsTabLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(itemsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(itemsTabLayout.createSequentialGroup()
                                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel7))
                                        .addGap(18, 18, 18)
                                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(itemsTabLayout.createSequentialGroup()
                                                .addComponent(itemsCategoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(categoriesEditBTN))
                                            .addComponent(itemsPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE))
                    .addGroup(itemsTabLayout.createSequentialGroup()
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemsReorderTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemsCreateBTN))
                        .addGap(14, 14, 14)))
                .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemsEditBTN)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(15, 15, 15))
        );
        itemsTabLayout.setVerticalGroup(
            itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemsTabLayout.createSequentialGroup()
                .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemsTabLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(itemsNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(itemsCategoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoriesEditBTN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(itemsPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(itemsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemsReorderTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(itemsTabLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemsEditBTN)
                    .addComponent(itemsCreateBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        tabs.addTab("Items", itemsTab);

        transactionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item ", "Date", "Quantity", "Type", "Notes"
            }
        ));
        jScrollPane4.setViewportView(transactionsTable);

        jLabel12.setText("Item:");

        jLabel14.setText("Quantity:");

        jLabel15.setText("Type:");

        jLabel16.setText("Notes:");

        transactionsCreateBTN.setText("Create");
        transactionsCreateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionsCreateBTNActionPerformed(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setText("Update or delete entries.");

        transactionsEditBTN.setText("Edit");
        transactionsEditBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionsEditBTNActionPerformed(evt);
            }
        });

        transactionsNotesTF.setColumns(20);
        transactionsNotesTF.setRows(5);
        jScrollPane5.setViewportView(transactionsNotesTF);

        transactionsTypeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sale", "Purchase" }));

        javax.swing.GroupLayout transactionsTabLayout = new javax.swing.GroupLayout(transactionsTab);
        transactionsTab.setLayout(transactionsTabLayout);
        transactionsTabLayout.setHorizontalGroup(
            transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionsTabLayout.createSequentialGroup()
                .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transactionsTabLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transactionsItemCB, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(transactionsTabLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(transactionsTypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transactionsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transactionsCreateBTN))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transactionsEditBTN)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(15, 15, 15))
        );
        transactionsTabLayout.setVerticalGroup(
            transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactionsTabLayout.createSequentialGroup()
                .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transactionsTabLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(transactionsEditBTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))
                    .addGroup(transactionsTabLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(transactionsItemCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(transactionsQuantityTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(transactionsTypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(transactionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(transactionsCreateBTN)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tabs.addTab("Transactions", transactionsTab);

        createPOrderBTN.setText("Open new order");
        createPOrderBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPOrderBTNActionPerformed(evt);
            }
        });

        jLabel11.setText("Supplier:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createPOrderBTN)
                    .addComponent(pOrdersSuppliersCB, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(518, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pOrdersSuppliersCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(createPOrderBTN)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        tabs.addTab("Purchase Orders", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void suppliersNameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliersNameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suppliersNameTFActionPerformed

    private void suppliersEditBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliersEditBTNActionPerformed
        int row = suppliersTable.getSelectedRow();
        
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row on the table to edit.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            editSuppliers editTab = new editSuppliers(this);
            editTab.setVisible(true);
        }
        
         

    }//GEN-LAST:event_suppliersEditBTNActionPerformed
    
    protected JTable getSuppliersTable(){
        return this.suppliersTable;
    }
    
    protected JTable getItemsTable(){
        return this.itemsTable;
    }
    
    protected JTable getTransactionsTable(){
        return this.transactionsTable;
    }
    
    private void itemsNameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsNameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemsNameTFActionPerformed

    private void itemsEditBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsEditBTNActionPerformed
        int row = itemsTable.getSelectedRow();
        
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row on the table to edit.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            editItems editTab = new editItems(this);
            editTab.setVisible(true);
        }
    }//GEN-LAST:event_itemsEditBTNActionPerformed

    private void transactionsEditBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionsEditBTNActionPerformed
        int row = transactionsTable.getSelectedRow();
        
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row on the table to edit.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            editTransactions editTab = new editTransactions(this);
            editTab.setVisible(true);
        }
    }//GEN-LAST:event_transactionsEditBTNActionPerformed

    private void suppliersCreateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliersCreateBTNActionPerformed
        // TODO add your handling code here:
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            Statement statement = con.createStatement();
            String name = suppliersNameTF.getText();
            String contact = suppliersContactTF.getText();
      
            String query = "insert into suppliers (supplier_name, contact_info) values (?, ?)";
           
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contact);
            preparedStatement.executeUpdate();
            
            suppliersNameTF.setText("");
            suppliersContactTF.setText("");

            loadTable();
            
            statement.close();
            con.close();
            loadSuppliersCB(pOrdersSuppliersCB);
            
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_suppliersCreateBTNActionPerformed

    private void itemsCreateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsCreateBTNActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            Statement statement = con.createStatement();
            
            String name = itemsNameTF.getText();
           
//            String selected = itemsCategoryCB.getSelectedItem().toString();
//            String[] id = selected.split(" - ");
            String price = itemsPriceTF.getText();
            String quantity = itemsQuantityTF.getText();
            String reorder = itemsReorderTF.getText();
            String description = itemsDescriptionTF.getText();
            
            String query = "SELECT * FROM Categories WHERE category_name = ?";
           
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, itemsCategoryCB.getSelectedItem().toString());
            
            ResultSet sqlresult; 
            
            sqlresult = preparedStatement.executeQuery();
            
            String category_id;
            
            if (sqlresult.next()) {
                category_id = sqlresult.getString("category_id");
            } else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }
      
            query = "insert into items (item_name, category_id, unit_price, quantity_on_hand,"
                    + "reorder_level, description) values (?, ?, ?, ?, ?, ?)";
           
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category_id);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, quantity);
            preparedStatement.setString(5, reorder);
            preparedStatement.setString(6, description);
            preparedStatement.executeUpdate();
            
            itemsNameTF.setText("");
            itemsDescriptionTF.setText("");
            itemsPriceTF.setText("");
            itemsQuantityTF.setText("");
            itemsReorderTF.setText("");
          

            loadTable();
            
            statement.close();
            con.close();
            
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_itemsCreateBTNActionPerformed

    private void transactionsCreateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionsCreateBTNActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            
            
            //String selected = transactionsItemCB.getSelectedItem().toString();
            //String[] item_id = selected.split(" - ");
            String item_id = "";
            String quantity = transactionsQuantityTF.getText();
            String type = transactionsTypeCB.getSelectedItem().toString();
            String notes = transactionsNotesTF.getText();
            
            String query = "SELECT * FROM Items WHERE item_name = ?";
           
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, transactionsItemCB.getSelectedItem().toString());
            
            ResultSet sqlresult; 
            
            sqlresult = preparedStatement.executeQuery();
            
            if (sqlresult.next()) {
                item_id = sqlresult.getString("item_id");
            } else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }
      
            query = "INSERT INTO Transactions (item_id, quantity, transaction_type, notes,"
                    + "transaction_date) values (?, ?, ?, ?, now())";
           
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, item_id);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, notes);
            preparedStatement.executeUpdate();
            
            transactionsQuantityTF.setText("");
            
            transactionsNotesTF.setText("");
            
            executeTransaction(quantity, type, item_id, false);

            loadTable();
            
            preparedStatement.close();
            con.close();
            
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_transactionsCreateBTNActionPerformed

    private void suppliersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_suppliersTableMouseClicked

    private void categoriesEditBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoriesEditBTNActionPerformed

        editCategories editTab = new editCategories(this);
        editTab.setVisible(true);
        
    }//GEN-LAST:event_categoriesEditBTNActionPerformed

    private void createPOrderBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPOrderBTNActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
           
            //----------GET SUPPLIER ID
            
            
            String supplier_id = "";
            
            String query = "SELECT * FROM Suppliers WHERE supplier_name = ?";
           
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, pOrdersSuppliersCB.getSelectedItem().toString());
            
            ResultSet sqlresult; 
            
            sqlresult = preparedStatement.executeQuery();
            
            if (sqlresult.next()) {
                supplier_id = sqlresult.getString("supplier_id");
            } else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }
            
            //----------CREATE PURCHASE ORDER
      
            
            query = "INSERT INTO PurchaseOrders (order_date, supplier_id, total_amount)"
                    + " values (now(), ?, 0.00)";
           
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, supplier_id);
            preparedStatement.executeUpdate();
            
            query = "SELECT LAST_INSERT_ID() AS ID;";    
            String purchase_order_id = "";
            Statement statement = con.createStatement();
            sqlresult = statement.executeQuery(query);
            
            //----------GET ORDER ID
            
            if (sqlresult.next()) {
                purchase_order_id = sqlresult.getString("ID");
            } else {
                System.out.println("Item not found in database.");
                return; // Avoid inserting without valid item_id
            }
            
            System.out.println("Order ID is " + purchase_order_id);
         
            
            
            preparedStatement.close();
            con.close();
            System.out.println("creat");
            
            pOrders orderTab = new pOrders(this, purchase_order_id);
            orderTab.setVisible(true);
            
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_createPOrderBTNActionPerformed

    protected JComboBox getCategoriesCB(){
        return this.itemsCategoryCB;
    }
    
    public final void loadCategoriesCB(JComboBox CB) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);

            Statement statement;
            statement = con.createStatement();
            ResultSet sqlresult;
            sqlresult = statement.executeQuery("SELECT * FROM Categories");
            
            CB.removeAllItems();

            while(sqlresult.next()) {
                CB.addItem(sqlresult.getString("category_name"));
            }
            
        }    
            catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    
             
    }
    
    public final void loadSuppliersCB(JComboBox CB) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);

            Statement statement;
            statement = con.createStatement();
            ResultSet sqlresult;
            sqlresult = statement.executeQuery("SELECT * FROM Suppliers");
            
            CB.removeAllItems();

            while(sqlresult.next()) {
                CB.addItem(sqlresult.getString("supplier_name"));
            }
            
        }    
            catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        
        
    }
    
    public void checkRestockLevels() {
        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dburl, dbusername, dbpassword);
            
            String query = "SELECT item_id, item_name, quantity_on_hand, reorder_level FROM Items";
            PreparedStatement preparedStatement = con.prepareStatement(query);   
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean needsRestock = false; 

            // Check the stock of all items
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String item_name = resultSet.getString("item_name");
                int stock = resultSet.getInt("quantity_on_hand");
                int reorderLevel = resultSet.getInt("reorder_level");

               
                if (stock < reorderLevel) {
                    // If stock is below reorder level, show the popup
                    showRestockPopup(itemId, item_name, stock, reorderLevel);
                    needsRestock = true;
                }
            }

            // If no items need restocking
        

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking stock levels: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void showRestockPopup(int itemId, String item_name, int quantity_on_hand, int reorderLevel) {
        String message = "Item ID: " + itemId + "\nItem Name: " + item_name + "\nCurrent Stock: " + quantity_on_hand + "\nReorder Level: " + reorderLevel +
                         "\n\nThe stock level is below the reorder level. Please restock the item.";
        JOptionPane.showMessageDialog(null, message, "Restock Alert", JOptionPane.WARNING_MESSAGE);
    }
    
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
            java.util.logging.Logger.getLogger(loggedIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loggedIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loggedIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loggedIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new loggedIn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton categoriesEditBTN;
    private javax.swing.JButton createPOrderBTN;
    private javax.swing.JComboBox<String> itemsCategoryCB;
    private javax.swing.JButton itemsCreateBTN;
    private javax.swing.JTextArea itemsDescriptionTF;
    private javax.swing.JButton itemsEditBTN;
    private javax.swing.JTextField itemsNameTF;
    private javax.swing.JTextField itemsPriceTF;
    private javax.swing.JTextField itemsQuantityTF;
    private javax.swing.JTextField itemsReorderTF;
    private javax.swing.JPanel itemsTab;
    private javax.swing.JTable itemsTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JComboBox<String> pOrdersSuppliersCB;
    private javax.swing.JTextField suppliersContactTF;
    private javax.swing.JButton suppliersCreateBTN;
    private javax.swing.JButton suppliersEditBTN;
    private javax.swing.JTextField suppliersNameTF;
    private javax.swing.JPanel suppliersTab;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JButton transactionsCreateBTN;
    private javax.swing.JButton transactionsEditBTN;
    private javax.swing.JComboBox<String> transactionsItemCB;
    private javax.swing.JTextArea transactionsNotesTF;
    private javax.swing.JTextField transactionsQuantityTF;
    private javax.swing.JPanel transactionsTab;
    private javax.swing.JTable transactionsTable;
    private javax.swing.JComboBox<String> transactionsTypeCB;
    // End of variables declaration//GEN-END:variables
}
