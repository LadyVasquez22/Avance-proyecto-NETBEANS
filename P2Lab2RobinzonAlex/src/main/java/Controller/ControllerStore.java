package Controller;

import Model.ConexionMongoDB;
import Model.Products;
import Model.RecoverPass;
import Model.SellerLogin;
import Model.SellerRegiste;
import View.AddProduct;
import View.FactureClients;
import View.Login;
import View.MainMenu;
import View.RecoverPassword;
import View.Registration;
import View.SaleStore;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

public class ControllerStore implements ActionListener {

    private ConexionMongoDB mongo = new ConexionMongoDB();
    private Products productModel = new Products("", "", "", "", "", "", "", "");
    private SellerLogin loginSeller = new SellerLogin("", "");
    private SellerRegiste registSeller = new SellerRegiste("", "", "", "", "", "");
    private Login login = new Login();
    private Registration regis;
    private AddProduct addProduct;
    private ControllerUser userController;
    private RecoverPassword recover;
    private SaleStore store;
    private ControllerSaleStore controlSaleStore;
    private MainMenu menu;
    private FactureClients facture;

    private String currentUserId;

    public ControllerStore(AddProduct addProduct, Products productModel, Login login, Registration regis,
            ControllerUser userController, RecoverPassword recover, SaleStore store,
            ControllerSaleStore controlSaleStore, MainMenu menu, FactureClients facture) {
        this.addProduct = addProduct;
        this.productModel = productModel;
        this.login = login;
        this.regis = regis;
        this.userController = userController;
        this.recover = recover;
        this.store = store;
        this.controlSaleStore = controlSaleStore;
        this.menu = menu;
        this.facture = facture;

        cleanDataAddProducts();
        cleanValidations();
        //configuracion de botones
        this.addProduct.btnCreate.addActionListener(this);
        this.addProduct.btnDelete.addActionListener(this);
        this.addProduct.btnRead.addActionListener(this);
        this.addProduct.btnReturn.addActionListener(this);
        this.addProduct.btnUpdate.addActionListener(this);
        this.addProduct.btnLimpiar.addActionListener(this);
        this.addProduct.btnInventario.addActionListener(this);
        
        this.login.btnLogin.addActionListener(this);
        this.login.btnExit.addActionListener(this);
        
        this.controlSaleStore.storeMenuSecond.itemAlacena.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemCarne.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemEmbutidos.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemFrutas.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemLegumbres.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemRecarga.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.btnBuscarProducto.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemAseoPersonal.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemAseoHogar.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemMascotas.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.itemLacteos.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.btnAddToCar.addActionListener(this);
        this.controlSaleStore.storeMenuSecond.btnCancelar.addActionListener(this);
        this.login.lblRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starRegist();
                login.setVisible(false);
            }
        });
        this.login.lblOlvidarContra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starRecover();
            }
        });
        this.regis.btnMenu.addActionListener(this);
        this.regis.btnRegistrations.addActionListener(this);
        
        this.recover.btnBuscarCuenta.addActionListener(this);
        this.recover.btnVolver.addActionListener(this);
        this.store.btnVolver.addActionListener(this);
        this.menu.btnAddProduct.addActionListener(this);
        this.menu.btnFacturas.addActionListener(this);
        this.menu.btnInventarioVenta.addActionListener(this);
        this.menu.btnSalir.addActionListener(this);
        this.facture.btnVolver.addActionListener(this);

        addProduct.tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = addProduct.tabla.getSelectedRow();
                addProduct.txtCode.setEnabled(false);
                try {
                    if (row != -1) {
                        addProduct.txtCode.setEnabled(false);
                        String code = addProduct.tabla.getValueAt(row, 0).toString();
                        String nameP = addProduct.tabla.getValueAt(row, 1).toString();
                        String PUnitario = addProduct.tabla.getValueAt(row, 2).toString();
                        String PVentas = addProduct.tabla.getValueAt(row, 3).toString();
                        String IVA = addProduct.tabla.getValueAt(row, 4).toString();
                        String stockMin = addProduct.tabla.getValueAt(row, 5).toString();
                        String stockActual = addProduct.tabla.getValueAt(row, 6).toString();
                        String category = addProduct.tabla.getValueAt(row, 7).toString();

                        //forma de hacer que no haya errores en caso de los campos vacios, si no valida esta parte siempre saldrá un error al elegir la fila
                        if (code != null) {
                            addProduct.txtCode.setText(code.toString());
                        }
                        if (nameP != null) {
                            addProduct.txtNombreProducto.setText(nameP);
                        }
                        if (PUnitario != null) {
                            addProduct.txtPVUnitario.setText(PUnitario.toString());
                        }
                        if (PVentas != null) {
                            addProduct.txtPVenta.setText(PVentas.toString());
                        }
                        if (IVA != null) {
                            addProduct.selIva.setSelectedItem(IVA.toString());
                        }
                        if (stockMin != null) {
                            addProduct.SpStockMin.setValue(Integer.parseInt(stockMin.toString()));
                        }
                        if (stockActual != null) {
                            addProduct.spStockActual.setValue(Integer.parseInt(stockActual.toString()));
                        }
                        if (category != null) {
                            addProduct.selCategory.setSelectedItem(category.toString());
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addProduct, "Error al seleccionar datos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void loadTable(String userId) {
        DefaultTableModel table = mongo.loadDataTable(userId);
        addProduct.tabla.setModel(table);
    }

    public void starRegist() {
        regis.setVisible(true);
    }

    public void starRecover() {
        recover.setVisible(true);
        login.setVisible(false);
    }

    public void cleanDataAddProducts() {
        addProduct.SpStockMin.setValue(0);
        addProduct.checkProduct.setSelected(false);
        addProduct.selCategory.setSelectedIndex(0);
        addProduct.selIva.setSelectedIndex(0);
        addProduct.spStockActual.setValue(0);
        addProduct.txtCode.setText("");
        addProduct.txtPVUnitario.setText("");
        addProduct.txtPVenta.setText("");
        addProduct.txtNombreProducto.setText("");
        addProduct.txtCode.setEnabled(true);
    }

    public void cleanValidations() {
        addProduct.lblErrorCategory.setText("");
        addProduct.lblErrorNombreProducto.setText("");
        addProduct.lblErrorCode.setText("");
        addProduct.lblErrorIVA.setText("");
        addProduct.lblErrorPUnitario.setText("");
        addProduct.lblErrorPVentas.setText("");
        addProduct.lblErrorProductConfirme.setText("");
        addProduct.lblErrorStockActual.setText("");
        addProduct.lblErrorStockMin.setText("");
    }

    public void cleanDatRegistrations() {
        regis.txtNames.setText("");
        regis.txtLastNames.setText("");
        regis.txtUser.setText("");
        regis.txtDni.setText("");
        regis.txtEmail.setText("");
        regis.password.setText("");
        regis.rbtnConfirmeRegistration.setSelected(false);
        addProduct.txtCode.setEnabled(true);
        loadTable(currentUserId);
    }

    public void cleanTable() {
        DefaultTableModel modelo = (DefaultTableModel) addProduct.tabla.getModel();
        modelo.setRowCount(0);
    }

    public void startViewLogin() {
        login.setVisible(true);
    }

    public void btnReturnMain() {
        menu.setVisible(true);
        addProduct.setVisible(false);
        regis.setVisible(false);
    }

    public void btnExit() {
        login.setVisible(false);
    }

    public void saveProducts() {

        String codigo = addProduct.txtCode.getText().trim();

        // Verificar si ya existe un documento con la misma cédula
        Document filtro = new Document("Código", codigo);
        ArrayList<Document> resultados = mongo.searchProductByUser(currentUserId, codigo);

        System.out.println("Filtro para búsqueda: " + filtro.toJson());

        if (resultados != null && !resultados.isEmpty()) {
            cleanDataAddProducts();
            cleanValidations();
            JOptionPane.showMessageDialog(addProduct, "Ya existe un registro con la cédula ingresada.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return; // Detener el proceso de guardado
        }

        productModel.setCodeProducts(addProduct.txtCode.getText().trim());
        productModel.setNameProduct(addProduct.txtNombreProducto.getText().trim());
        productModel.setIva(addProduct.selIva.getSelectedItem().toString());
        productModel.setPriceUnit(addProduct.txtPVUnitario.getText().trim());
        productModel.setPSeller(addProduct.txtPVenta.getText().trim());
        productModel.setStockMin(addProduct.SpStockMin.getValue().toString());
        productModel.setStockCurrent(addProduct.spStockActual.getValue().toString());
        productModel.setCategory(addProduct.selCategory.getSelectedItem().toString());

        //String codeProducts, String priceUnit, String PSeller, String iva, String stockMin, String stockCurrent, String category
        Products products = new Products(productModel.getCodeProducts(), productModel.getNameProduct(), productModel.getPriceUnit(),
                productModel.getPSeller(), productModel.getIva(), productModel.getStockMin(),
                productModel.getStockCurrent(), productModel.getCategory());

        if (products.validationsProduct(addProduct)) {
            Document productDoc = new Document("Código", productModel.getCodeProducts())
                    .append("NombreP", productModel.getNameProduct())
                    .append("Precio Unitario", productModel.getPriceUnit())
                    .append("Precio Venta", productModel.getPSeller())
                    .append("IVA", productModel.getIva())
                    .append("Stock Mínimo", productModel.getStockMin())
                    .append("Stock Actual", productModel.getStockCurrent())
                    .append("Categoria", productModel.getCategory());
            if (mongo.addProductToUser(currentUserId, productDoc)) {
                mongo.createDocumentProducts(productDoc);
                loadTable(currentUserId);
                JOptionPane.showMessageDialog(addProduct, "Datos guardados correctamente.");
                products.getData();
            }
            
        } else {
            JOptionPane.showMessageDialog(addProduct, "Por favor, corrija los campos inválidos.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateProductsUsers() {
        productModel.setCodeProducts(addProduct.txtCode.getText().trim());
        productModel.setNameProduct(addProduct.txtNombreProducto.getText().trim());
        productModel.setPriceUnit(addProduct.txtPVUnitario.getText().trim());
        productModel.setPSeller(addProduct.txtPVenta.getText().trim());
        productModel.setIva(addProduct.selIva.getSelectedItem().toString());
        productModel.setStockMin(addProduct.SpStockMin.getValue().toString());
        productModel.setStockCurrent(addProduct.spStockActual.getValue().toString());
        productModel.setCategory(addProduct.selCategory.getSelectedItem().toString());

        // Crear un Document con los datos actualizados del producto
        Document updatedProductDoc = new Document("Código", productModel.getCodeProducts())
                .append("NombreP", productModel.getNameProduct())
                .append("Precio Unitario", productModel.getPriceUnit())
                .append("Precio Venta", productModel.getPSeller())
                .append("IVA", productModel.getIva())
                .append("Stock Mínimo", productModel.getStockMin())
                .append("Stock Actual", productModel.getStockCurrent())
                .append("Categoria", productModel.getCategory());

        // Actualizar el producto en la base de datos
        if (mongo.updateProductByUser(currentUserId, productModel.getCodeProducts(), updatedProductDoc)) {
            // Actualizar el producto en la colección ProductsAdd
            Bson filter = Filters.eq("Código", productModel.getCodeProducts());
            if (mongo.updateProduct(filter, updatedProductDoc)) {
                loadTable(currentUserId);
                cleanDataAddProducts();
                cleanValidations();
                JOptionPane.showMessageDialog(addProduct, "Producto actualizado.");
            
                // Llamar al método cargarTabla del ControllerSaleStore
                controlSaleStore.cargarTabla();
            } else {
                JOptionPane.showMessageDialog(addProduct, "Error al actualizar el producto en ProductsAdd.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(addProduct, "Error al actualizar el producto de los usuarios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        addProduct.txtCode.setEnabled(true);
    }

    public void lookForProducts() {
        String codeProducts = JOptionPane.showInputDialog(addProduct, "Ingrese el código del producto a buscar: ");
        if (codeProducts == null || codeProducts.isEmpty()) {
            JOptionPane.showMessageDialog(addProduct, "Debe ingresar un código válido.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscar el producto utilizando el método searchProductByUser
        ArrayList<Document> resultados = mongo.searchProductByUser(currentUserId, codeProducts);

        cleanTable();
        if (resultados != null && !resultados.isEmpty()) {
            DefaultTableModel modelTable = (DefaultTableModel) addProduct.tabla.getModel();
            for (Document doc : resultados) {
                modelTable.addRow(new Object[]{
                    doc.getString("Código"),
                    doc.getString("NombreP"),
                    doc.getString("Precio Unitario"),
                    doc.getString("Precio Venta"),
                    doc.getString("IVA"),
                    doc.getString("Stock Mínimo"),
                    doc.getString("Stock Actual"),
                    doc.getString("Categoria")
                });
            }
        } else {
            JOptionPane.showMessageDialog(addProduct, "No se encontraron datos para el código ingresado.",
                    "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void deleteProducts() {
        int rowSelected = addProduct.tabla.getSelectedRow();

        if (rowSelected == -1) {
            JOptionPane.showMessageDialog(addProduct, "Debe seleccionar un registro de la tabla para eliminar.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //obtener la cedula seleccionada
        String codigo = addProduct.tabla.getValueAt(rowSelected, 0).toString();
        //confirmar eliminacion
        int confirmation = JOptionPane.showConfirmDialog(addProduct,
                "¿Está seguro de que desea eliminar el registro con código " + codigo + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (mongo.deleteProductFromUser(currentUserId, codigo)) {
                JOptionPane.showMessageDialog(addProduct, "Registro eliminado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable(currentUserId);
            } else {
                JOptionPane.showMessageDialog(addProduct, "Error al eliminar el registro. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        cleanDataAddProducts();
        cleanValidations();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == controlSaleStore.storeMenuSecond.btnBuscarProducto) {
            System.out.println("BUSCANDO PRODUCTO...");
            controlSaleStore.lookForProductsSecondMenu("");
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemRecarga) {
            System.out.println("MENU DE RECARGA SELECCIONADO");
            controlSaleStore.cargarTabla();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemAlacena) {
            System.out.println("MENU DE ALACENA SELECCIONADO");
            controlSaleStore.llenarTablaAlacena();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemCarne) {
            System.out.println("MENU DE CARNES SELECCIONADO");
            controlSaleStore.llenarTablaCarnes();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemEmbutidos) {
            System.out.println("MENU DE EMBUTIDOS SELECCIONADO");
            controlSaleStore.llenarTablaEmbutidos();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemFrutas) {
            System.out.println("MENU DE FRUTAS SELECCIONADO");
            controlSaleStore.llenarTablaFrutas();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemLegumbres) {
            System.out.println("MENU DE LEGUMBRES SELECCIONADO");
            controlSaleStore.llenarTablaLegumbres();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemAseoPersonal) {
            System.out.println("MENU DE ASEO PERSONAL SELECCIONADO");
            controlSaleStore.llenarTablaAseoPersonal();
        }
        if (e.getSource() ==controlSaleStore.storeMenuSecond.itemAseoHogar) {
            System.out.println("MENU DE ASEO DEL HOGAR SELECCIONADO");
            controlSaleStore.llenarTablaAseodelHogar();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemMascotas) {
            System.out.println("MENU DE MASCOTAS SELECCIONADO");
            controlSaleStore.llenarTablaProductosMascotas();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.itemLacteos) {
            System.out.println("MENU DE LACTEOS SELECCIONADO");
            controlSaleStore.llenarTablaLacteos();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.btnAddToCar) {
            System.out.println("");
            controlSaleStore.addProductToCar();
        }
        if (e.getSource() == controlSaleStore.storeMenuSecond.btnCancelar) {
            
        }
        if (e.getSource() == regis.btnRegistrations) { //boton de registro
            registerUser();
        }

        if (e.getSource() == login.btnLogin) { //boton de iniciar sesión
            loginUser();
            
        }
        if (e.getSource() == login.btnExit) { //boton de salida
            btnExit();
        }

        if (e.getSource() == recover.btnVolver) {
            btnReturnMain();
            recover.dispose();
        }

        if (e.getSource() == addProduct.btnCreate) { //guarda los productos ingresados en tabla
            saveProducts();
        }

        if (e.getSource() == addProduct.btnDelete) { //eliminar una parte de la fila
            deleteProducts();
        }

        if (e.getSource() == addProduct.btnLimpiar) { //limpiar validaciones y campos
            cleanDataAddProducts();
            cleanValidations();
        }

        if (e.getSource() == addProduct.btnRead) { //buscar
            lookForProducts();
        }

        if (e.getSource() == addProduct.btnReturn) { //volver a la pagina principal
            btnReturnMain();
        }

        if (e.getSource() == addProduct.btnUpdate) { //actualizar
            updateProductsUsers();
        }

        if (e.getSource() == regis.btnMenu) { //volver al menu
            btnReturnMain();
            regis.dispose();
        }

        if (e.getSource() == recover.btnBuscarCuenta) { //busca cuenta perdida
            recoverAccount();
        }

        if (e.getSource() == addProduct.btnInventario) {
            controlSaleStore.cargarTabla();
            addProduct.setVisible(false);
            controlSaleStore.storeMenuSecond.setVisible(true);

        }

        if (e.getSource() == store.btnVolver) {
            menu.setVisible(true);
            store.setVisible(false);
        }
        if (e.getSource() == menu.btnAddProduct) {
            login.setVisible(false);
            menu.setVisible(false);
            addProduct.setVisible(true);
        }
        if (e.getSource() == menu.btnFacturas) {
            login.setVisible(false);
            menu.setVisible(false);
            facture.setVisible(true);
        }
        if (e.getSource() ==menu.btnInventarioVenta) {
            controlSaleStore.storeMenuSecond.setVisible(true);
            login.setVisible(false);
            menu.setVisible(false);
        }
        if ( e.getSource() == menu.btnSalir) {
            menu.setVisible(false);
            login.setVisible(true);
        }
        if (e.getSource() == facture.btnVolver) {
            facture.setVisible(false);
            menu.setVisible(true);
        }
    }

    public void cleanDatRegistrations(String userId) {
        loadTable(userId); // Llama al método para cargar la tabla con el userId
        // Otras operaciones de limpieza...
    }

    public void registerUser() {
        registSeller.setNames(regis.txtNames.getText().trim());
        registSeller.setLastNames(regis.txtLastNames.getText().trim());
        registSeller.setUser(regis.txtUser.getText().trim());
        registSeller.setDni(regis.txtDni.getText().trim());
        registSeller.setEmail(regis.txtEmail.getText().trim());
        registSeller.setPassword(String.valueOf(regis.password.getPassword()).trim());

        if (registSeller.validationsRegist(regis)) {
            String userId = userController.registerUser(registSeller);
            if (userId != null) {
                JOptionPane.showMessageDialog(regis, "Usuario registrado correctamente.");
                cleanDatRegistrations(userId); // Propaga el userId
                registSeller.getData();
                regis.setVisible(false);
                login.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(regis, "Error al registrar el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(regis, "El usuario no ha podido ser registrado. Por favor, corrija los campos inválidos.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loginUser() {
        loginSeller.setUser(login.txtUser.getText().trim());
        loginSeller.setPassword(new String(login.password.getPassword()).trim());

        if (loginSeller.validationsLogin(login)) {
            if (userController.login(loginSeller.getUser(), loginSeller.getPassword())) {
                Document filtro = new Document("User", loginSeller.getUser());
                ArrayList<Document> resultados = mongo.searchDocument(filtro);

                if (resultados != null && !resultados.isEmpty()) {
                    currentUserId = resultados.get(0).getObjectId("_id").toString();
                    JOptionPane.showMessageDialog(login, "Inicio de sesión exitoso.");
                    menu.setVisible(true);
                    login.setVisible(false);
                    loadTable(currentUserId);
                } else {
                    JOptionPane.showMessageDialog(login, "Usuario no encontrado.",
                            "Error de inicio de sesión", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(login, "Credenciales incorrectas.",
                        "Error de inicio de sesión", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(login, "Por favor, corrija los campos inválidos.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recoverAccount() {
        String dni = recover.txtCedulaRecuperada.getText().trim();
        String email = recover.txtEmailRecuperado.getText().trim();

        RecoverPass recoverPass = new RecoverPass(dni, email);

        if (recoverPass.validationsRecover(recover)) {
            boolean result = recoverPass.searchAccount();

            if (result) {
                recover.txtAreaDatosRecuperados.setText("Usuario: " + recoverPass.getUser() + "\nContraseña: " + recoverPass.getPassword());
            } else {
                recover.txtAreaDatosRecuperados.setText("No se encontró ninguna cuenta con los datos proporcionados.");
            }
        }
    }
}
