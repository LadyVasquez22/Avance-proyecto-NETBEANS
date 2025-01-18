package Controller;

import Model.ConexionMongoDB;
import Model.ModelProductCar;
import View.SaleStore;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

//PRUEBA SI FUNCIONA EL SUBIR ARCHIVOS 
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//EEEEEEEEEEEEEEEEEEEEEEEEEEEE
public class ControllerSaleStore {

    SaleStore storeMenuSecond;
    ConexionMongoDB mongo;
    
    private List<ModelProductCar> carrito = new ArrayList<>();

    public ControllerSaleStore(SaleStore storeMenuSecond, ConexionMongoDB mongo) {
        this.storeMenuSecond = storeMenuSecond;
        this.mongo = mongo;
        
        mongo.createConnection();
        mongo.getCollectionInv();
        cargarTabla();
        
        
        DefaultTableModel tableModelCarrito = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Nombre Producto", "Categoria", "Cantidad Compra", "Precio de la Venta" }
        );
        storeMenuSecond.TablaCarrito.setModel(tableModelCarrito);
    }
    
    public void cargarTabla() {
        List<Document> products = mongo.getAllProductsFromStore();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"NombreP", "Categoria", "Stock Actual", "Precio Unitario", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que solo la columna "Cantidad" sea editable
                return column == 4;
            }
        };
        storeMenuSecond.tabla.setModel(tableModel);
        tableModel.setRowCount(0); // Limpiar la tabla

        for (Document product : products) {
            String nombreP = product.getString("NombreP");
            String categoria = product.getString("Categoria");
            String stockActual = product.get("Stock Actual") != null ? String.valueOf(product.get("Stock Actual")) : "0";
            String precioUnitario = product.get("Precio Unitario") != null ? String.valueOf(product.get("Precio Unitario")) : "0";

            Object[] row = {
                nombreP,
                categoria,
                stockActual,
                precioUnitario,
                0 // Inicialmente, la cantidad es 0
            };
            tableModel.addRow(row);
        }
    }

    public void limpiarTabla() {
        DefaultTableModel tableModel = (DefaultTableModel) storeMenuSecond.tabla.getModel();
        tableModel.setRowCount(0);
    }

    //ARREGLAR DE AQUÍ EN ADELANTE
    public void AjustarCategoria(String categoria) { //busca en el documento de mongo donde estén almacenados todos los productos
        //CREO UN FOR CON EL VALOR NUMERO DE PRODUCTOS, IF
        Bson filter = Filters.eq("Categoria", categoria);
        try (MongoCursor<Document> cursor = mongo.createConnection().getCollection("ProductsAdd")
                .find(filter)
                .iterator()) {
            DefaultTableModel tabla = (DefaultTableModel) storeMenuSecond.tabla.getModel();
            tabla.setRowCount(0);

            while (cursor.hasNext()) {
                Document producto = cursor.next();
                String nombreP = producto.getString("NombreP");
                String categoriaProducto = producto.getString("Categoria");
                String stockMinimo = producto.get("Stock Actual") != null ? String.valueOf(producto.get("Stock Actual")) : "0";
                String precioUnitario = producto.get("Precio Unitario") != null ? String.valueOf(producto.get("Precio Unitario")) : "0";
                String cantidad = producto.get("Cantidad") != null ? String.valueOf(producto.get("Cantidad")) : "0";

                String[] fila = {
                    nombreP,
                    categoriaProducto,
                    stockMinimo,
                    precioUnitario,
                    cantidad
                };
                tabla.addRow(fila);
                System.out.println(" [DEPURACION] Aniadido a la tabla: " + Arrays.toString(fila)); // 
            }
        } catch (Exception e) {
            e.printStackTrace();
           
        }
    }

    public void lookForProductsSecondMenu(String nombreProducto) {
        nombreProducto = storeMenuSecond.txtProducto.getText();
        
        if (nombreProducto.isEmpty() || nombreProducto == null) {
            JOptionPane.showMessageDialog(storeMenuSecond, "No se ha encontrado un Producto con tal nombre.");
        }
        
        Bson filter = Filters.eq("NombreP", nombreProducto);
        System.out.println("[DEPURACION] Buscando el producto: " + nombreProducto);
        ArrayList<Document> resultados = mongo.searchProduct(filter);
        limpiarTabla();

        if (resultados != null && !resultados.isEmpty()) {
            DefaultTableModel table = (DefaultTableModel) storeMenuSecond.tabla.getModel();
            for (Document doc : resultados) {
                String nombreP = doc.getString("NombreP");
                String categoria = doc.getString("Categoria");
                String stockActual = doc.get("Stock Actual") != null ? String.valueOf(doc.get("Stock Actual")) : "0";
                String precioUnitario = doc.get("Precio Unitario") != null ? String.valueOf(doc.get("Precio Unitario")) : "0";

                table.addRow(new Object[]{
                    nombreP,
                    categoria,
                    stockActual,
                    precioUnitario,
                    0
                });
            }
        } else {
            JOptionPane.showMessageDialog(storeMenuSecond, "No se ha encontrado un Producto con tal nombre.");
        }
    }
    
    public void addProductToCar() {
    DefaultTableModel tableModel = (DefaultTableModel) storeMenuSecond.tabla.getModel();
    int selectedRow = storeMenuSecond.tabla.getSelectedRow();

    if (selectedRow != -1) {
        String nombreP = (String) tableModel.getValueAt(selectedRow, 0);
        String categoria = (String) tableModel.getValueAt(selectedRow, 1);
        int stockActual = Integer.parseInt(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        double precioUnitario = Double.parseDouble(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        Object cantidadObj = tableModel.getValueAt(selectedRow, 4);

        if (cantidadObj == null || cantidadObj.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(storeMenuSecond, "Por favor, ingrese una cantidad válida.");
            return;
        }

        int cantidad = Integer.parseInt(String.valueOf(cantidadObj));

        if (cantidad > 0 && cantidad <= stockActual) {
            // Calcular el precio total de la venta
            double precioTotal = cantidad * precioUnitario;

            // Verificar si el producto ya está en el carrito
            boolean productoExistente = false;
            for (ModelProductCar producto : carrito) {
                if (producto.getNombreProducto().equals(nombreP)) {
                    // Actualizar la cantidad del producto existente
                    producto.setCantidad(producto.getCantidad() + cantidad);
                    producto.setPrecioVenta(producto.getCantidad() * precioUnitario);
                    productoExistente = true;
                    break;
                }
            }

            if (!productoExistente) {
                // Crear un objeto del carrito
                ModelProductCar productoCarrito = new ModelProductCar(nombreP, categoria, cantidad, precioTotal);

                // Añadirlo a la lista del carrito
                carrito.add(productoCarrito);

                // Mostrar el producto añadido en la tabla
                DefaultTableModel tableCarrito = (DefaultTableModel) storeMenuSecond.TablaCarrito.getModel();
                tableCarrito.addRow(new Object[]{
                    productoCarrito.getNombreProducto(),
                    productoCarrito.getCategoria(),
                    productoCarrito.getCantidad(),
                    productoCarrito.getPrecioVenta()
                });
            } else {
                // Actualizar la tabla del carrito
                DefaultTableModel tableCarrito = (DefaultTableModel) storeMenuSecond.TablaCarrito.getModel();
                for (int i = 0; i < tableCarrito.getRowCount(); i++) {
                    if (tableCarrito.getValueAt(i, 0).equals(nombreP)) {
                        tableCarrito.setValueAt(cantidad, i, 2);
                        tableCarrito.setValueAt(cantidad * precioUnitario, i, 3);
                        break;
                    }
                }
            }

            // Guardar el producto en la colección CarritoDeCompras
                Document doc = new Document("NombreP", nombreP)
                        .append("Categoria", categoria)
                        .append("Cantidad", cantidad)
                        .append("Precio Unitario", precioUnitario)
                        .append("Precio Total", precioTotal);
                if (mongo.saveCarBuys(doc)) {
                    System.out.println("[DEPURACION] el carrito fue aniadido correctamente a la base de datos");
                } else {
                    System.out.println("[DEPURACION] el producto fue aniadido al carrito pero no a la base de datos.");
               }

                // Actualizar el stock en la base de datos
                int nuevoStock = stockActual - cantidad;
                Bson filter = Filters.eq("NombreP", nombreP);
               Bson updateOperation = new Document("$set", new Document("Stock Actual", nuevoStock));
               mongo.createConnection().getCollection("ProductsAdd").updateOne(filter, updateOperation);

                // Actualizar el stock en la tabla
                tableModel.setValueAt(nuevoStock, selectedRow, 2);

                JOptionPane.showMessageDialog(storeMenuSecond, "Producto aniadido al carrito. Precio total: " + precioTotal);
            } else {
                JOptionPane.showMessageDialog(storeMenuSecond, "Cantidad inválida. Debe ser mayor que 0 y menor o igual al stock actual. \n"
                        + "EDITE el campo cantidad pedido y presione ENTER antes de añadir al carrito.");
                JOptionPane.showMessageDialog(storeMenuSecond, cantidadObj);;
            }
        } else {
            JOptionPane.showMessageDialog(storeMenuSecond, "Seleccione un producto para añadir al carrito.");
        }
    }

    
    public void llenarTablaCarnes() {
        String categoria = "Carnes";
        AjustarCategoria(categoria);
    }

    public void llenarTablaAlacena() {
        String categoria = "Alacena";
        AjustarCategoria(categoria);
    }

    public void llenarTablaFrutas() {
        String categoria = "Frutas";
        AjustarCategoria(categoria);
    }

    public void llenarTablaLegumbres() {
        String categoria = "Legumbres";
        AjustarCategoria(categoria);

    }

    public void llenarTablaEmbutidos() {
        String categoria = "Embutidos";
        AjustarCategoria(categoria);

    }
    
    public void llenarTablaAseoPersonal() {
        String categoria = "Aseo Personal";
        AjustarCategoria(categoria);
    }
    
    public void llenarTablaAseodelHogar() {
        String categoria = "Aseo del hogar";
        AjustarCategoria(categoria);
    }
    
    public void llenarTablaProductosMascotas() {
        String categoria = "Productos para mascota";
        AjustarCategoria(categoria);
    }
    
    public void llenarTablaLacteos() {
        String categoria = "Lacteos";
        AjustarCategoria(categoria);
    }
}