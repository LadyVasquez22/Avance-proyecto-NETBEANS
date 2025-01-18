package Model;

public class ModelProductCar {
    private String nombreProducto;
    private String categoria;
    private int cantidad;
    private double precioVenta;

    public ModelProductCar(String nombreProducto, String categoria, int cantidad, double precioVenta) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
}
