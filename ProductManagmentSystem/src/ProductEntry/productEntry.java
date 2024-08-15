/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProductEntry;

import java.awt.Color;
import java.awt.Font;

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
public class productEntry extends javax.swing.JFrame {

    /**
     * Creates new form productEntry
     */
    //Checking for update Product
    String selectedProductName = "";
    boolean productNameChanged = false;

    public productEntry() {
        initComponents();
        pnlFilterProduct.setVisible(false);

        tableDesign();
        loadDataToComboBox();
        newProduct();
    }

    //Table Design and load Data to Table
    private void tableDesign() {
        tblProductEntry.getTableHeader().setFont(new Font("SEGOE UI", Font.BOLD, 13));
        tblProductEntry.getTableHeader().setOpaque(false);
        tblProductEntry.getTableHeader().setBackground(new Color(102, 0, 102));
        tblProductEntry.getTableHeader().setForeground(new Color(255, 255, 255));
        tblProductEntry.setRowHeight(25);
        tblProductEntry.setOpaque(false);
        tblProductEntry.setShowGrid(false);
        tblProductEntry.setShowHorizontalLines(true);
    }

    private void loadDataToTable() {
        lblErrorMessageSearch.setVisible(false);
        lblErrorMessage.setForeground(new Color(153, 0, 0));

        try {

            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductId,ProductCategoryId,ProductName,UnitPurchasingPrice,UnitSallingPrice FROM product_management_system.product_entry;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            DefaultTableModel tm = (DefaultTableModel) tblProductEntry.getModel();
            tm.setRowCount(0);
            while (rs.next()) {
                //get DataFrom Product Entry Table
                String ProductId = rs.getString("ProductId");
                String ProductCategoryId = rs.getString("ProductCategoryId");
                String ProductName = rs.getString("ProductName");
                String UnitPurchasingPrice = rs.getString("UnitPurchasingPrice");
                String UnitSallingPrice = rs.getString("UnitSallingPrice");

                String ProductCategoryName = "";
                // Get Product Catefory Name
                String sql1 = "SELECT ProductCategory FROM product_management_system.product_category where ProductCategoryId=?;";
                PreparedStatement st1 = con.prepareStatement(sql1);
                st1.setString(1, ProductCategoryId);
                ResultSet rs1 = st1.executeQuery();
                while (rs1.next()) {
                    ProductCategoryName = rs1.getString("ProductCategory");
                }

                //Add Datas to Table
                Vector rowData = new Vector<>();
                rowData.add(ProductId);
                rowData.add(ProductCategoryName);
                rowData.add(ProductName);
                rowData.add(UnitPurchasingPrice + ".00");
                rowData.add(UnitSallingPrice + ".00");
                tm.addRow(rowData);

            }
        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // load data to ComboBox
    private void loadDataToComboBox() {
        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductCategory FROM product_management_system.product_category;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            cmbProductCategory.removeAllItems();
            cmbProductCategory.addItem("Not Selected");
            while (rs.next()) {
                String ProductCategory = rs.getString("ProductCategory");
                cmbProductCategory.addItem(ProductCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Enable Disable Fielsd
    private void enableField() {
        txtProductName.setEnabled(true);
        txtUnitPurchasingPrice.setEnabled(true);
        txtUnitSallingPrice.setEnabled(true);
    }

    private void disableField() {
        txtProductName.setEnabled(false);
        txtUnitPurchasingPrice.setEnabled(false);
        txtUnitSallingPrice.setEnabled(false);
    }

    //Select a Data From Table
    private void selectRowTable(String productId) {
        cmbProductCategory.setEnabled(false);
        enableField();
        lblErrorMessage.setVisible(false);
        //Button enable Disabe
        btnSave.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);

        //Set Data to UI
        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductId,ProductCategoryId,ProductName,UnitPurchasingPrice,UnitSallingPrice FROM product_management_system.product_entry where ProductId = ?;";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, productId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String ProductCategoryId = rs.getString("ProductCategoryId");
                String ProductName = rs.getString("ProductName");
                String UnitPurchasingPrice = rs.getString("UnitPurchasingPrice");
                String UnitSallingPrice = rs.getString("UnitSallingPrice");

                //get Category
                String ProductCategory = "";
                if (productId.equals("0")) {
                    disableField();
                } else {

                    String sql1 = "SELECT ProductCategory FROM product_management_system.product_category where ProductCategoryId=?;";
                    PreparedStatement st1 = con.prepareStatement(sql1);
                    st1.setString(1, ProductCategoryId);
                    ResultSet rs1 = st1.executeQuery();
                    while (rs1.next()) {
                        ProductCategory = rs1.getString("ProductCategory");
                    }
                }

                //Set Data to TextFeils
                txtProductId.setText(productId);
                cmbProductCategory.setSelectedItem(ProductCategory);
                txtProductName.setText(ProductName);
                txtUnitPurchasingPrice.setText(UnitPurchasingPrice);
                txtUnitSallingPrice.setText(UnitSallingPrice);

                // Send data for Check update Fucfction
                selectedProductName = ProductName;
            }
        } catch (SQLException ex) {
            Logger.getLogger(productCategories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Code for New Save Buton
    private void newProduct() {

        loadDataToTable();
        cmbProductCategory.setEnabled(true);
        disableField();
        lblErrorMessage.setVisible(false);
        lblErrorMessageSearch.setVisible(false);

        //Button enble and disabel
        btnSave.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        //Set Product ID
        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT max(ProductId) FROM product_management_system.product_entry;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int maxProductId = rs.getInt("max(ProductId)");
                int newProductId = maxProductId + 1;
                txtProductId.setText(newProductId + "");
            }
        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
        }

        //clear Other Fields
        cmbProductCategory.setSelectedIndex(0);
        txtProductName.setText("");
        txtUnitPurchasingPrice.setText("");
        txtUnitSallingPrice.setText("");

    }

    private void saveProduct() {
        lblErrorMessage.setVisible(false);

        //Get Data Fromm Form
        boolean NumberWrong = false;
        String productCategory = cmbProductCategory.getSelectedItem().toString();
        String productName = txtProductName.getText();
        float unitPurchasingPrice = 0;
        try {
            unitPurchasingPrice = Float.valueOf(txtUnitPurchasingPrice.getText());
        } catch (NumberFormatException e) {
            NumberWrong = true;
        }

        float unitSallingPrice = 0;
        try {
            unitSallingPrice = Float.valueOf(txtUnitSallingPrice.getText());
        } catch (NumberFormatException e) {
            NumberWrong = true;
        }

        //Checking EmptyFields
        if (productCategory.equals("Not Selected") || productName.equals("")) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText("Some Fields are Empty");
        } else {
            if (NumberWrong == true) {
                lblErrorMessage.setVisible(true);
                lblErrorMessage.setText("<html><center>Invalid Value for <br>Purchesing Price or Selling Price</center></html>");
            } else {
                try {
                    //Check The Product is Already Entered
                    Connection con = Database.db.getcConnection();
                    String sql = "SELECT * FROM product_management_system.product_entry where ProductName =?;";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, productName);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        lblErrorMessage.setVisible(true);
                        lblErrorMessage.setText("<html><center>The product Name <br>" + productName + " is alredy Enterd</center></html>");
                    } else {
                        //Get Product Category id to sent to Db
                        int productCategoryId = 0;
                        String sqlSearch = "SELECT ProductCategoryId FROM product_management_system.product_category where ProductCategory = ?;";
                        PreparedStatement stSearch = con.prepareStatement(sqlSearch);
                        stSearch.setString(1, productCategory);
                        ResultSet rsSearch = stSearch.executeQuery();
                        while (rsSearch.next()) {
                            productCategoryId = rsSearch.getInt("ProductCategoryId");
                        }

                        //Send Data to DataBase
                        // Create prepared statement with the sql query
                        String sql1 = "INSERT INTO `product_management_system`.`product_entry` (`ProductCategoryId`, `ProductName`, `UnitPurchasingPrice`, `UnitSallingPrice`) VALUES (?, ?, ?, ?);";
                        PreparedStatement st1 = con.prepareStatement(sql1);
                        st1.setInt(1, productCategoryId);
                        st1.setString(2, productName);
                        st1.setFloat(3, unitPurchasingPrice);
                        st1.setFloat(4, unitSallingPrice);
                        // execute query
                        st1.executeUpdate();

                        //send data to Stock Table
                        String productId = txtProductId.getText();
                        String sql2 = "INSERT INTO `product_management_system`.`product_stock` (`ProductId`) VALUES (?);";
                        PreparedStatement st2 = con.prepareStatement(sql2);
                        st2.setString(1, productId);
                        st2.executeUpdate();
                        //Load Data to Table
                        loadDataToTable();

                        // show success message
                        lblErrorMessage.setVisible(true);
                        lblErrorMessage.setForeground(Color.green);
                        lblErrorMessage.setText("<html><center>The Product <br>" + productName + " is success fully Enterd</center></html>");
                        btnSave.setEnabled(false);

                        //txtCtagory Disable
                        cmbProductCategory.setEnabled(false);
                        disableField();

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // Code for Update and Delete Buttton  Not Complte
    private void updateProduct(String productId) {
        lblErrorMessage.setVisible(false);

        //Get Data Fromm Form
        boolean NumberWrong = false;
        String productCategory = cmbProductCategory.getSelectedItem().toString();
        String productName = txtProductName.getText();
        float unitPurchasingPrice = 0;
        try {
            unitPurchasingPrice = Float.valueOf(txtUnitPurchasingPrice.getText());
        } catch (NumberFormatException e) {
            NumberWrong = true;
        }

        float unitSallingPrice = 0;
        try {
            unitSallingPrice = Float.valueOf(txtUnitSallingPrice.getText());
        } catch (NumberFormatException e) {
            NumberWrong = true;
        }

        //Checking EmptyFields
        if (productCategory.equals("Not Selected") || productName.equals("")) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText("Some Fields are Empty");
        } else {
            if (NumberWrong == true) {
                lblErrorMessage.setVisible(true);
                lblErrorMessage.setText("<html><center>Invalid Value for <br>Purchesing Price or Selling Price</center></html>");
            } else {
                try {
                    Connection con = Database.db.getcConnection();

                    if (productNameChanged == true) {
                        //Check The Product is Already Entered
                        String sql = "SELECT * FROM product_management_system.product_entry where ProductName =?;";
                        PreparedStatement st = con.prepareStatement(sql);
                        st.setString(1, productName);
                        ResultSet rs = st.executeQuery();
                        if (rs.next()) {
                            lblErrorMessage.setVisible(true);
                            lblErrorMessage.setText("<html><center>The product Name <br>" + productName + " is alredy Enterd</center></html>");
                        } else {
                            //Get Product Category id to sent to Db
                            int productCategoryId = 0;
                            String sqlSearch = "SELECT ProductCategoryId FROM product_management_system.product_category where ProductCategory = ?;";
                            PreparedStatement stSearch = con.prepareStatement(sqlSearch);
                            stSearch.setString(1, productCategory);
                            ResultSet rsSearch = stSearch.executeQuery();
                            while (rsSearch.next()) {
                                productCategoryId = rsSearch.getInt("ProductCategoryId");
                            }

                            //Send Data to DataBase
                            // Create prepared statement with the sql query
                            String sql1 = "UPDATE `product_management_system`.`product_entry` SET `ProductCategoryId` = ?, `ProductName` = ?, `UnitPurchasingPrice` = ?, `UnitSallingPrice` = ? WHERE (`ProductId` = '" + productId + "');";
                            PreparedStatement st1 = con.prepareStatement(sql1);
                            st1.setInt(1, productCategoryId);
                            st1.setString(2, productName);
                            st1.setFloat(3, unitPurchasingPrice);
                            st1.setFloat(4, unitSallingPrice);
                            // execute query
                            st1.executeUpdate();

                            //Load Data to Table
                            loadDataToTable();

                            // show success message
                            lblErrorMessage.setVisible(true);
                            lblErrorMessage.setForeground(Color.green);
                            lblErrorMessage.setText("<html><center>The Product <br>" + productName + " is success fully Updated</center></html>");
                            btnUpdate.setEnabled(false);
                            btnDelete.setEnabled(false);

                            //txtCtagory Disable
                            cmbProductCategory.setEnabled(false);
                            disableField();

                        }
                    } else {
                        //Get Product Category id to sent to Db
                        int productCategoryId = 0;
                        String sqlSearch = "SELECT ProductCategoryId FROM product_management_system.product_category where ProductCategory = ?;";
                        PreparedStatement stSearch = con.prepareStatement(sqlSearch);
                        stSearch.setString(1, productCategory);
                        ResultSet rsSearch = stSearch.executeQuery();
                        while (rsSearch.next()) {
                            productCategoryId = rsSearch.getInt("ProductCategoryId");
                        }

                        //Send Data to DataBase
                        // Create prepared statement with the sql query
                        String sql1 = "UPDATE `product_management_system`.`product_entry` SET `ProductCategoryId` = ?, `ProductName` = ?, `UnitPurchasingPrice` = ?, `UnitSallingPrice` = ? WHERE (`ProductId` = '" + productId + "');";
                        PreparedStatement st1 = con.prepareStatement(sql1);
                        st1.setInt(1, productCategoryId);
                        st1.setString(2, productName);
                        st1.setFloat(3, unitPurchasingPrice);
                        st1.setFloat(4, unitSallingPrice);
                        // execute query
                        st1.executeUpdate();

                        //Load Data to Table
                        loadDataToTable();

                        // show success message
                        lblErrorMessage.setVisible(true);
                        lblErrorMessage.setForeground(Color.green);
                        lblErrorMessage.setText("<html><center>The Product <br>" + productName + " is success fully Updated</center></html>");
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);

                        //txtCtagory Disable
                        cmbProductCategory.setEnabled(false);
                        disableField();

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void deleteProduct(String productId) {
        try {
            // create connection to database
            Connection con = Database.db.getcConnection();
            // Create prepared statement with the sql query
            PreparedStatement st1 = con.prepareStatement("DELETE FROM `product_management_system`.`product_entry` WHERE (`ProductId` = ?);");
            st1.setString(1, productId);
            // execute query
            st1.executeUpdate();

            //Load Data to Table
            loadDataToTable();

            // show success message
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setForeground(Color.red);
            lblErrorMessage.setText("<html><center>The Product <br>" + " is success fully Deleted</center></html>");
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);

            //txtCtagory Disable
            cmbProductCategory.setEnabled(false);
            disableField();
        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Code for Searh data Fron datble
    private void searchProduct(int seachType, String search) {
        //Error Message ForGroung
        lblErrorMessageSearch.setVisible(false);
        lblErrorMessage.setForeground(new Color(153, 0, 0));

        try {
            Connection con = Database.db.getcConnection();
            String sql = "";
            if (seachType == 0) {
                sql = "SELECT ProductId,ProductCategoryId,ProductName,UnitPurchasingPrice,UnitSallingPrice FROM product_management_system.product_entry where ProductId like ? ;";
            } else if (seachType == 1) {
                sql = "SELECT ProductId,ProductCategoryId,ProductName,UnitPurchasingPrice,UnitSallingPrice FROM product_management_system.product_entry where ProductName like ? ;";
            } else if (seachType == 2) {
                sql = "SELECT ProductId,ProductCategoryId,ProductName,UnitPurchasingPrice,UnitSallingPrice FROM product_management_system.product_entry where ProductCategoryId like ? ;";
            }
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, search + "%");
            ResultSet rs = st.executeQuery();
            DefaultTableModel tm = (DefaultTableModel) tblProductEntry.getModel();
            tm.setRowCount(0);
            while (rs.next()) {
                //get DataFrom Product Entry Table
                String ProductId = rs.getString("ProductId");
                String ProductCategoryId = rs.getString("ProductCategoryId");
                String ProductName = rs.getString("ProductName");
                String UnitPurchasingPrice = rs.getString("UnitPurchasingPrice");
                String UnitSallingPrice = rs.getString("UnitSallingPrice");

                String ProductCategoryName = "";
                // Get Product Catefory Name
                String sql1 = "SELECT ProductCategory FROM product_management_system.product_category where ProductCategoryId=?;";
                PreparedStatement st1 = con.prepareStatement(sql1);
                st1.setString(1, ProductCategoryId);
                ResultSet rs1 = st1.executeQuery();
                while (rs1.next()) {
                    ProductCategoryName = rs1.getString("ProductCategory");
                }

                //Add Datas to Table
                Vector rowData = new Vector<>();
                rowData.add(ProductId);
                rowData.add(ProductCategoryName);
                rowData.add(ProductName);
                rowData.add(UnitPurchasingPrice + ".00");
                rowData.add(UnitSallingPrice + ".00");
                tm.addRow(rowData);
            }
//            } else {
//                lblErrorMessageSearch.setVisible(true);
//                lblErrorMessageSearch.setText("There Are Dont Have Any Catogory Like you Serach");
//            }
        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
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

        btnGrpSearch = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbProductCategory = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtUnitPurchasingPrice = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnAddNewCatagory = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtUnitSallingPrice = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblErrorMessage = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductEntry = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lblErrorMessageSearch = new javax.swing.JLabel();
        pnlSearchProduct = new javax.swing.JPanel();
        cmbSearchType = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        btnClearSearch = new javax.swing.JButton();
        radioSearchProduct = new javax.swing.JRadioButton();
        btnFilterProduct = new javax.swing.JRadioButton();
        pnlFilterProduct = new javax.swing.JPanel();
        cmbFilterType = new javax.swing.JComboBox<>();
        cmbFilter = new javax.swing.JComboBox<>();
        btnClearFilter = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Product Managment System");

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Product Entry");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Product Entry");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Enter the new product Details Here");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Product Id ");

        txtProductId.setEditable(false);
        txtProductId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductId.setText("jTextField2");
        txtProductId.setFocusable(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Product Category");

        cmbProductCategory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbProductCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbProductCategory.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cmbProductCategoryPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Product Name");

        txtProductName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductName.setText("jTextField3");
        txtProductName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductNameKeyReleased(evt);
            }
        });

        txtUnitPurchasingPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUnitPurchasingPrice.setText("jTextField3");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Unit Purchasing price");

        btnAddNewCatagory.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnAddNewCatagory.setText("+");
        btnAddNewCatagory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewCatagoryActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Unit Salling price");

        txtUnitSallingPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUnitSallingPrice.setText("jTextField3");

        btnNew.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblErrorMessage.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblErrorMessage.setForeground(new java.awt.Color(153, 0, 51));
        lblErrorMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorMessage.setText("Error Message");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbProductCategory, 0, 174, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddNewCatagory))
                    .addComponent(txtUnitSallingPrice, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtUnitPurchasingPrice, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtProductName)
                    .addComponent(txtProductId))
                .addGap(59, 59, 59))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbProductCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddNewCatagory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtUnitPurchasingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtUnitSallingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblErrorMessage)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew)
                    .addComponent(btnSave)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addGap(45, 45, 45))
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tblProductEntry.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblProductEntry.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product Id", "Product Catagory", "Product Name", "Purchasing Price (Rs.)", "Salling (Rs.)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductEntry.setSelectionBackground(new java.awt.Color(102, 102, 102));
        tblProductEntry.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblProductEntry.setShowGrid(false);
        tblProductEntry.setShowHorizontalLines(true);
        tblProductEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductEntryMouseClicked(evt);
            }
        });
        tblProductEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProductEntryKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductEntry);
        if (tblProductEntry.getColumnModel().getColumnCount() > 0) {
            tblProductEntry.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Product List");

        lblErrorMessageSearch.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblErrorMessageSearch.setForeground(new java.awt.Color(153, 0, 51));
        lblErrorMessageSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorMessageSearch.setText("Error Message Search");

        cmbSearchType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbSearchType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Product Id", "Product Name" }));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        btnClearSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClearSearch.setText("Clear Search");
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSearchProductLayout = new javax.swing.GroupLayout(pnlSearchProduct);
        pnlSearchProduct.setLayout(pnlSearchProductLayout);
        pnlSearchProductLayout.setHorizontalGroup(
            pnlSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearSearch)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        pnlSearchProductLayout.setVerticalGroup(
            pnlSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearSearch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        radioSearchProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnGrpSearch.add(radioSearchProduct);
        radioSearchProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        radioSearchProduct.setSelected(true);
        radioSearchProduct.setText("Search Product");
        radioSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioSearchProductActionPerformed(evt);
            }
        });

        btnFilterProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnGrpSearch.add(btnFilterProduct);
        btnFilterProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFilterProduct.setText("Fillter Product");
        btnFilterProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterProductActionPerformed(evt);
            }
        });

        cmbFilterType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbFilterType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Not Selected", "Catagory" }));
        cmbFilterType.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cmbFilterTypePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        cmbFilter.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbFilter.setEnabled(false);
        cmbFilter.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cmbFilterPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        btnClearFilter.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnClearFilter.setText("Clear Filter");
        btnClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFilterProductLayout = new javax.swing.GroupLayout(pnlFilterProduct);
        pnlFilterProduct.setLayout(pnlFilterProductLayout);
        pnlFilterProductLayout.setHorizontalGroup(
            pnlFilterProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbFilterType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFilterProductLayout.setVerticalGroup(
            pnlFilterProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilterProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbFilterType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearFilter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorMessageSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlFilterProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(radioSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrorMessageSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioSearchProduct)
                    .addComponent(btnFilterProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        newProduct();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveProduct();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String productId = txtProductId.getText();
        updateProduct(productId);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(rootPane, "Are You Sure, Do you want to Delete", "Delete Product Category", 0, 3);
        if (option == 0) {
            String productId = txtProductId.getText();
            deleteProduct(productId);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblProductEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductEntryMouseClicked
        // TODO add your handling code here:
        int row = tblProductEntry.getSelectedRow();
        String productId = tblProductEntry.getValueAt(row, 0).toString();
        selectRowTable(productId);
    }//GEN-LAST:event_tblProductEntryMouseClicked

    private void tblProductEntryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductEntryKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductEntryKeyReleased

    private void cmbProductCategoryPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cmbProductCategoryPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
        String productCategory = cmbProductCategory.getSelectedItem().toString();
        if (productCategory.equals("Not Selected")) {
            newProduct();
            disableField();
            btnSave.setEnabled(false);
        } else {
            enableField();
            btnSave.setEnabled(true);
        }
    }//GEN-LAST:event_cmbProductCategoryPopupMenuWillBecomeInvisible

    private void radioSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioSearchProductActionPerformed
        // TODO add your handling code here:
        if (radioSearchProduct.isSelected()) {
            loadDataToTable();
            pnlFilterProduct.setVisible(false);
            pnlSearchProduct.setVisible(true);
        } else {
            loadDataToTable();
            pnlFilterProduct.setVisible(true);
            pnlSearchProduct.setVisible(false);
        }
    }//GEN-LAST:event_radioSearchProductActionPerformed

    private void btnFilterProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterProductActionPerformed
        // TODO add your handling code here:
        if (radioSearchProduct.isSelected()) {
            loadDataToTable();
            pnlFilterProduct.setVisible(false);
            pnlSearchProduct.setVisible(true);
        } else {
            loadDataToTable();
            pnlFilterProduct.setVisible(true);
            pnlSearchProduct.setVisible(false);
        }
    }//GEN-LAST:event_btnFilterProductActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        int searchType = cmbSearchType.getSelectedIndex();
        String search = txtSearch.getText();

        if (search.equals("")) {
            loadDataToTable();
        } else {
            searchProduct(searchType, search);

        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        // TODO add your handling code here:
        txtSearch.setText("");
        loadDataToTable();
    }//GEN-LAST:event_btnClearSearchActionPerformed

    private void cmbFilterTypePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cmbFilterTypePopupMenuWillBecomeInvisible
        // TODO add your handling code here:
        if (cmbFilterType.getSelectedIndex() == 0) {
            cmbFilter.setEnabled(false);
            cmbFilter.removeAllItems();
        } else if (cmbFilterType.getSelectedIndex() == 1) {
            try {
                cmbFilter.setEnabled(true);
                Connection con = Database.db.getcConnection();
                String sql = "SELECT ProductCategory FROM product_management_system.product_category ;";
                PreparedStatement st = con.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                cmbFilter.removeAllItems();
                while (rs.next()) {
                    String ProductCategory = rs.getString("ProductCategory");
                    cmbFilter.addItem(ProductCategory);
                }
            } catch (SQLException ex) {
                Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cmbFilterTypePopupMenuWillBecomeInvisible

    private void cmbFilterPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cmbFilterPopupMenuWillBecomeInvisible
        // TODO add your handling code here:

        String search = cmbFilter.getSelectedItem().toString();
        String searchId = "";

        try {
            Connection con = Database.db.getcConnection();
            String sql = "SELECT ProductCategoryId FROM product_management_system.product_category where ProductCategory like ? ;";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, search + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                searchId = rs.getString("ProductCategoryId");

            }

        } catch (SQLException ex) {
            Logger.getLogger(productEntry.class.getName()).log(Level.SEVERE, null, ex);
        }

        searchProduct(2, searchId);
    }//GEN-LAST:event_cmbFilterPopupMenuWillBecomeInvisible

    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed
        // TODO add your handling code here:
        cmbFilterType.setSelectedIndex(0);
        cmbFilter.setEnabled(false);
        cmbFilter.removeAllItems();
        loadDataToTable();
    }//GEN-LAST:event_btnClearFilterActionPerformed

    private void btnAddNewCatagoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewCatagoryActionPerformed
        // TODO add your handling code here:

        productCategories addCategory = new productCategories();
        addCategory.show();
        this.dispose();
    }//GEN-LAST:event_btnAddNewCatagoryActionPerformed

    private void txtProductNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductNameKeyReleased
        // TODO add your handling code here:
        String productName = txtProductName.getText();

        if (productName.equals(selectedProductName)) {
            productNameChanged = false;
        } else {
            productNameChanged = true;
        }
    }//GEN-LAST:event_txtProductNameKeyReleased

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
            java.util.logging.Logger.getLogger(productEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(productEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(productEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(productEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new productEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewCatagory;
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnDelete;
    private javax.swing.JRadioButton btnFilterProduct;
    private javax.swing.ButtonGroup btnGrpSearch;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbFilter;
    private javax.swing.JComboBox<String> cmbFilterType;
    private javax.swing.JComboBox<String> cmbProductCategory;
    private javax.swing.JComboBox<String> cmbSearchType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblErrorMessage;
    private javax.swing.JLabel lblErrorMessageSearch;
    private javax.swing.JPanel pnlFilterProduct;
    private javax.swing.JPanel pnlSearchProduct;
    private javax.swing.JRadioButton radioSearchProduct;
    private javax.swing.JTable tblProductEntry;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUnitPurchasingPrice;
    private javax.swing.JTextField txtUnitSallingPrice;
    // End of variables declaration//GEN-END:variables
}
