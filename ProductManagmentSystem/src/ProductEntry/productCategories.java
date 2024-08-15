/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProductEntry;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ammar
 */
public class productCategories extends javax.swing.JFrame {

    /**
     * Creates new form productCategories
     */
    public productCategories() {
        initComponents();
        tableDesign();
        newProductCategory();
    }

//Table Design and load Data to Table
    private void tableDesign() {
        tblProductCategories.getTableHeader().setFont(new Font("SEGOE UI", Font.BOLD, 13));
        tblProductCategories.getTableHeader().setOpaque(false);
        tblProductCategories.getTableHeader().setBackground(new Color(102, 0, 102));
        tblProductCategories.getTableHeader().setForeground(new Color(255, 255, 255));
        tblProductCategories.setRowHeight(25);
        tblProductCategories.setOpaque(false);
        tblProductCategories.setShowGrid(false);
        tblProductCategories.setShowHorizontalLines(true);
    }

    private void loadDataToTable() {
        //Error Message ForGroung
        lblErrorMessageSearch.setVisible(false);
        lblErrorMesssage.setForeground(new Color(153, 0, 0));

        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductCategoryId,ProductCategory FROM product_management_system.product_category;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            DefaultTableModel tm = (DefaultTableModel) tblProductCategories.getModel();
            tm.setRowCount(0);
            while (rs.next()) {
                String categoryId = rs.getString("ProductCategoryId");
                String category = rs.getString("ProductCategory");

                //Add Data to Table
                Vector rowData = new Vector<>();
                rowData.add(categoryId);
                rowData.add(category);
                tm.addRow(rowData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//Select a Data From Table
    private void selectRowTable(String categoryId) {
        txtCategory.setEnabled(true);
        lblErrorMesssage.setVisible(false);
        //Button enable Disabe
        btnSave.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);

        //Set Data to UI
        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductCategoryId,ProductCategory FROM product_management_system.product_category where ProductCategoryId =?;";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, categoryId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String ProductCategory = rs.getString("ProductCategory");

                //Set Data to TextFeils
                txtCategoryId.setText(categoryId);
                txtCategory.setText(ProductCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Code for New Save Buton
    private void newProductCategory() {
        loadDataToTable();
        txtCategory.setEnabled(true);
        //Error Message Boxes Visible False
        lblErrorMesssage.setVisible(false);
        lblErrorMessageSearch.setVisible(false);

        //Button enble and disabel
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        //Set Product Catagury ID
        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT max(ProductCategoryId) FROM product_management_system.product_category;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int maxProductCategoryId = rs.getInt("max(ProductCategoryId)");
                int newProductCategoryId = maxProductCategoryId + 1;
                txtCategoryId.setText(newProductCategoryId + "");
            }
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
        }

        //clear Other Fields
        txtCategory.setText("");
        txtCategory.requestFocus();
    }

    private void saveProductCategory() {
        lblErrorMesssage.setVisible(false);
        String ProductCategory = txtCategory.getText();
        if (ProductCategory.equals("")) {
            lblErrorMesssage.setVisible(true);
            lblErrorMesssage.setText("Enter the New Product Catagory Name");
        } else {
            //Check The Product id name Already Entered
            try {
                Connection con = Database.db.getcConnection();
                String sql = "SELECT * FROM product_management_system.product_category where ProductCategory =?;";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, ProductCategory);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    lblErrorMesssage.setVisible(true);
                    lblErrorMesssage.setText("<html><center>The product Category Name <br>" + ProductCategory + " is alredy Enterd</center></html>");
                } else {

                    //Send Data to DataBase
                    // Create prepared statement with the sql query
                    String sql1 = "INSERT INTO `product_management_system`.`product_category` (`ProductCategory`) VALUES (?);";
                    PreparedStatement st1 = con.prepareStatement(sql1);
                    st1.setString(1, ProductCategory);
                    // execute query
                    st1.executeUpdate();

                    //Load Data to Table
                    loadDataToTable();

                    // show success message
                    lblErrorMesssage.setVisible(true);
                    lblErrorMesssage.setForeground(Color.green);
                    lblErrorMesssage.setText("<html><center>The Product Category <br>" + ProductCategory + " is success fully Enterd</center></html>");
                    btnSave.setEnabled(false);

                    //txtCtagory Disable
                    txtCategory.setEnabled(false);
                }
            } catch (SQLException ex) {
                Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

// Code for Update and Delete Buttton  
    private void updateProductCategory(String categoryId) {
        lblErrorMesssage.setVisible(false);
        String ProductCategory = txtCategory.getText();
        if (ProductCategory.equals("")) {
            lblErrorMesssage.setVisible(true);
            lblErrorMesssage.setText("Enter the Product Catagory Name");
        } else {
            //Check The Product id name Already Entered
            try {
                Connection con = Database.db.getcConnection();
                String sql = "SELECT * FROM product_management_system.product_category where ProductCategory =?;";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, ProductCategory);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    lblErrorMesssage.setVisible(true);
                    lblErrorMesssage.setText("<html><center>The product Category Name <br>" + ProductCategory + " is alredy Enterd</center></html>");
                } else {

                    //Send Data to DataBase
                    // Create prepared statement with the sql query
                    String sql1 = "UPDATE `product_management_system`.`product_category` SET `ProductCategory` = ? WHERE (`ProductCategoryId` = '" + categoryId + "');";
                    PreparedStatement st1 = con.prepareStatement(sql1);
                    st1.setString(1, ProductCategory);
                    // execute query
                    st1.executeUpdate();

                    //Load Data to Table
                    loadDataToTable();

                    // show success message
                    lblErrorMesssage.setVisible(true);
                    lblErrorMesssage.setForeground(Color.green);
                    lblErrorMesssage.setText("<html><center>The Product Category <br>" + ProductCategory + " is success fully Updated</center></html>");
                    btnUpdate.setEnabled(false);
                    btnDelete.setEnabled(false);

                    //txtCtagory Disable
                    txtCategory.setEnabled(false);
                }
            } catch (SQLException ex) {
                Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void deleteProductCategory(String categoryId) {
        try {
            // create connection to database
            Connection con = Database.db.getcConnection();
            // Create prepared statement with the sql query
            PreparedStatement st1 = con.prepareStatement("DELETE FROM `product_management_system`.`product_category` WHERE (`ProductCategoryId` = ?);");
            st1.setString(1, categoryId);
            // execute query
            st1.executeUpdate();

            //Load Data to Table
            loadDataToTable();

            // show success message
            lblErrorMesssage.setVisible(true);
            lblErrorMesssage.setForeground(Color.red);
            lblErrorMesssage.setText("<html><center>The Product Category <br>" + " is success fully Deleted</center></html>");
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);

            //txtCtagory Disable
            txtCategory.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

// Code for Searh data Fron datble
    private void searchProductCategory(int seachType, String search) {
        //Error Message ForGroung
        lblErrorMessageSearch.setVisible(false);
        lblErrorMesssage.setForeground(new Color(153, 0, 0));

        try {
            Connection con = Database.db.getcConnection();
            String sql = "";
            if (seachType == 0) {
                sql = "SELECT ProductCategoryId,ProductCategory FROM product_management_system.product_category where ProductCategoryId like ? ;";
            } else {
                sql = "SELECT ProductCategoryId,ProductCategory FROM product_management_system.product_category where ProductCategory like ? ;";
            }
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, search + "%");
            ResultSet rs = st.executeQuery();
            DefaultTableModel tm = (DefaultTableModel) tblProductCategories.getModel();
            tm.setRowCount(0);
            if (rs.next()) {
                String categoryId = rs.getString("ProductCategoryId");
                String category = rs.getString("ProductCategory");

                //Add Data to Table
                Vector rowData = new Vector<>();
                rowData.add(categoryId);
                rowData.add(category);
                tm.addRow(rowData);
            } else {
                lblErrorMessageSearch.setVisible(true);
                lblErrorMessageSearch.setText("There Are Dont Have Any Catogory Like you Serach");
            }
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductCategories = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblErrorMessageSearch = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtSearchBox = new javax.swing.JTextField();
        cmbSearchType = new javax.swing.JComboBox<>();
        btnClearSearch = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        txtCategoryId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        lblErrorMesssage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 0, 102));

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Product Managment System");

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Product Categories");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tblProductCategories.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblProductCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "<html><center>Product <br> Categories Id</center></html>", "Product Categories"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductCategories.setSelectionBackground(new java.awt.Color(102, 102, 102));
        tblProductCategories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductCategoriesMouseClicked(evt);
            }
        });
        tblProductCategories.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProductCategoriesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductCategories);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Product Categories List");

        lblErrorMessageSearch.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblErrorMessageSearch.setForeground(new java.awt.Color(153, 0, 0));
        lblErrorMessageSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorMessageSearch.setText("Error Message");

        txtSearchBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBoxKeyReleased(evt);
            }
        });

        cmbSearchType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbSearchType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Category Id", "Category" }));

        btnClearSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClearSearch.setText("Clear");
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearSearch)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearSearch))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorMessageSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrorMessageSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Product Categories Entry");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Enter the new product Category Details");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Category ID (AI)");

        txtCategoryId.setEditable(false);
        txtCategoryId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCategoryId.setFocusable(false);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Product Category");

        txtCategory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCategory.setText("jTextField3");

        btnNew.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblErrorMesssage.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblErrorMesssage.setForeground(new java.awt.Color(153, 0, 0));
        lblErrorMesssage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorMesssage.setText("Error Messaqge");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCategoryId))
                                    .addComponent(jSeparator1)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCategory))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnNew)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSave)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnUpdate)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDelete))
                                    .addComponent(jSeparator3)
                                    .addComponent(jSeparator2)
                                    .addComponent(lblErrorMesssage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 9, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCategoryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                .addComponent(lblErrorMesssage)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jPanel4.setBackground(new java.awt.Color(102, 0, 102));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("This Product Categories are Going to be in Product Category List in Product Entry Form");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(192, 192, 192))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        newProductCategory();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveProductCategory();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String categoryId = txtCategoryId.getText();
        updateProductCategory(categoryId);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(rootPane, "Are You Sure, Do you want to Delete", "Delete Product Category", 0, 3);
        if (option == 0) {
            String categoryId = txtCategoryId.getText();
            deleteProductCategory(categoryId);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    //============================================================ Code for Table select Event ----------------------------------------//
    private void tblProductCategoriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductCategoriesMouseClicked
        // TODO add your handling code here:
        int row = tblProductCategories.getSelectedRow();
        String categoryId = tblProductCategories.getValueAt(row, 0).toString();
        selectRowTable(categoryId);
    }//GEN-LAST:event_tblProductCategoriesMouseClicked

    private void tblProductCategoriesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductCategoriesKeyReleased
        // TODO add your handling code here:
        //Have to code to Key Prese Arrow change Search
    }//GEN-LAST:event_tblProductCategoriesKeyReleased

    // ====================================================== Code for Search section ------------------------------------------------//
    private void txtSearchBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBoxKeyReleased
        // TODO add your handling code here:
        int searchType = cmbSearchType.getSelectedIndex();
        String search = txtSearchBox.getText();

        if (search.equals("")) {
            loadDataToTable();
        } else {
            searchProductCategory(searchType, search);

        }

    }//GEN-LAST:event_txtSearchBoxKeyReleased

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        // TODO add your handling code here:
        txtSearchBox.setText("");
        loadDataToTable();
    }//GEN-LAST:event_btnClearSearchActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        productEntry entry = new productEntry();
        entry.show();
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(productCategories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(productCategories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(productCategories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(productCategories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new productCategories().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbSearchType;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblErrorMessageSearch;
    private javax.swing.JLabel lblErrorMesssage;
    private javax.swing.JTable tblProductCategories;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtCategoryId;
    private javax.swing.JTextField txtSearchBox;
    // End of variables declaration//GEN-END:variables
}
