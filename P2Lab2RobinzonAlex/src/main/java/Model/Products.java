package Model;

import View.AddProduct;
import javax.swing.JOptionPane;

public class Products implements Interface {
    private String codeProducts;
    private String nameProduct;
    private String priceUnit;
    private String PSeller;
    private String iva;
    private String stockMin;
    private String stockCurrent;
    private String category;
    
    boolean validConfirmation = true;

    public Products(String codeProducts, String nameProduct, String priceUnit, String PSeller, String iva, String stockMin, String stockCurrent, String category) {
        this.codeProducts = codeProducts;
        this.nameProduct = nameProduct;
        this.priceUnit = priceUnit;
        this.PSeller = PSeller;
        this.iva = iva;
        this.stockMin = stockMin;
        this.stockCurrent = stockCurrent;
        this.category = category;
    }
    
    public String getCodeProducts() {
        return codeProducts;
    }

    public void setCodeProducts(String codeProducts) {
        this.codeProducts = codeProducts;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getPSeller() {
        return PSeller;
    }

    public void setPSeller(String PSeller) {
        this.PSeller = PSeller;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva; 
    }

    public String getStockMin() {
        return stockMin;
    }

    public void setStockMin(String stockMin) {
        this.stockMin = stockMin;
    }

    public String getStockCurrent() {
        return stockCurrent;
    }

    public void setStockCurrent(String stockCurrent) {
        this.stockCurrent = stockCurrent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
    
    @Override
    public void getData() {
        System.out.println("[DEPURACION] Obteniendo datos del producto");
        JOptionPane.showMessageDialog(null, "Productos ingresados \n" +
                "Nombre del producto: "+getNameProduct()+"\n"+
                "Codido del producto: " + getCodeProducts() + "\n" +
                "Precio Unitario: " + getPriceUnit() + "\n" +
                "Precio de venta: " + getPSeller() + "\n" +
                "Categoria: " + getCategory());
    }
    
    public boolean validationsProduct(AddProduct products) {
        System.out.println("[DEPURACION] Iniciando validacion de producto...");
        validConfirmation = true;
        
        if (getCodeProducts().isEmpty()) {
            System.out.println("[DEPURACION] codeProducts esta vacio");
            products.lblErrorCode.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (getCodeProducts().length() != 6) {
            System.out.println("[DEPURACION] codeProducts esta fuera del limite de caracteres");
            products.lblErrorCode.setText("*debe contener únicamente 6 caracteres*");
            validConfirmation = false;
        }else {
            products.lblErrorCode.setText(""); 
        }
        
        if (getNameProduct().isEmpty() || getNameProduct() == null) {
            System.out.println("[DEPURACION] nombreProducto esta vacio");
            products.lblErrorNombreProducto.setText("");
            validConfirmation = false;
        } else if (!getNameProduct().matches("[a-zA-ZÁÉÍÓÚáéíóúÜü ]+")) {
            System.out.println("[DEPURACION] nombreProducto contiene numeros, los cuales son inválidos");
            products.lblErrorNombreProducto.setText("*ingrese únicamente letras*");
            validConfirmation = false;
        } else {
            products.lblErrorNombreProducto.setText("");
        }

        if (getPriceUnit().isEmpty()) {
            System.out.println("[DEPURACION] priceUnit esta vacio");
            products.lblErrorPUnitario.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (!getPriceUnit().matches("\\d+(\\.\\d+)?")) {
            System.out.println("[DEPURACION] priceUnit no es un numero valido");
            products.lblErrorPUnitario.setText("*ingrese un precio*");
            validConfirmation = false;
        } else {
            double newPriceUni = Double.parseDouble(getPriceUnit());
            if (newPriceUni < 0.03 || newPriceUni > 1000) {
                System.out.println("[DEPURACION] priceUnit fuera de rango: " + newPriceUni);
                products.lblErrorPUnitario.setText("*precio no posible*");
                validConfirmation = false;
            } else {
                products.lblErrorPUnitario.setText("");
            }
        }

        if (getPSeller().isEmpty()) {
            System.out.println("[DEPURACION] PSeller esta vacio");
            products.lblErrorPVentas.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (!getPSeller().matches("\\d+(\\.\\d+)?")) {
            System.out.println("[DEPURACION] PSeller no es un numero valido");
            products.lblErrorPVentas.setText("*ingrese un precio*");
            validConfirmation = false;
        } else {
            double newPriceSeller = Double.parseDouble(getPSeller());
            if (newPriceSeller < 0 || newPriceSeller > 1000) {
                System.out.println("[DEPURACION] PSeller fuera de rango: " + newPriceSeller);
                products.lblErrorPVentas.setText("*precio no posible*");
                validConfirmation = false;
            } else {
                products.lblErrorPVentas.setText("");
            }
        }

        if (getIva().equals("IVA")) {
            System.out.println("[DEPURACION] IVA no seleccionado");
            products.lblErrorIVA.setText("*seleccione una opción*");
            validConfirmation = false;
        } else {
            products.lblErrorIVA.setText("");
        }

        if (getStockMin().equals("0")) {
            System.out.println("[DEPURACION] stockMin es 0");
            products.lblErrorStockMin.setText("*ingrese un stock minimo*");
            validConfirmation = false;
        } else {
            double newStockMin = Double.parseDouble(getStockMin());
            if (newStockMin < 1 || newStockMin > 5) {
                System.out.println("[DEPURACION] stockMin fuera de rango: " + newStockMin);
                products.lblErrorStockMin.setText("*stock minimo permitido solo entre (1-5)*");
                validConfirmation = false;
            } else {
                products.lblErrorStockMin.setText("");
            }
        }

        if (getStockCurrent().equals("0")) {
            System.out.println("[DEPURACION] stockCurrent es 0");
            products.lblErrorStockActual.setText("*ingrese stock actual*");
            validConfirmation = false;
        } else {
            double newStockCurrent = Double.parseDouble(getStockCurrent());
            if (newStockCurrent < 5 || newStockCurrent > 500) {
                System.out.println("[DEPURACION] stockCurrent fuera de rango: " + newStockCurrent);
                products.lblErrorStockActual.setText("*actualmente solo hay espacio de 5 a 500 productos*");
                validConfirmation = false;
            } else {
                products.lblErrorStockActual.setText("");
            }
        }

        if (getCategory().equals("Categoria")) {
            System.out.println("[DEPURACION] Categoria no seleccionada");
            products.lblErrorCategory.setText("*seleccione una categoria*");
            validConfirmation = false;
        } else {
            products.lblErrorCategory.setText("");
        }
        
        if (!products.checkProduct.isSelected()){
            System.out.println("[DEPURACION] Producto no seleccionado en el checkbox");
            products.lblErrorProductConfirme.setText("*confirmar seleccion*");
            validConfirmation = false;
        } else {
            System.out.println("[DEPURACION] Producto seleccionado correctamente");
            products.lblErrorProductConfirme.setText("");
        }   
        
        System.out.println("[DEPURACION] Validacion de producto finalizada. Estado: " + validConfirmation);
        return validConfirmation;
    }
}