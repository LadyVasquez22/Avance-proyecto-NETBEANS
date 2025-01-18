package Model;

import View.Registration;
import java.util.List;
import javax.swing.JOptionPane;

public class SellerRegiste extends User implements Interface{
    private String names;
    private String lastNames;
    private String dni;
    private String email;
    private List<Products> products; // Lista de productos

    boolean validConfirmation = true;
     
    public SellerRegiste(String names, String lastNames, String user, String dni, String email, String password) {
        super (user, password);
        this.names = names;
        this.lastNames = lastNames;
        this.dni = dni;
        this.email = email;
    }
    
    // Getters y setters...
    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public void getData(){
        JOptionPane.showMessageDialog(null, "Vendedor registrado correctamente \n"+
                "Usuario: "+ getUser()+"\n"+
                "Nombres y apellidos: "+getNames()+" "+getLastNames()+"\n"+
                "Correo: "+ getEmail()+"\n"+
                "Cedula: " + getDni()+"\n");
    }
    
    public boolean validationsRegist(Registration regis){
        System.out.println("[DEPURACION] Iniciando validacion del registro...");
        validConfirmation = true;
        if (getNames().isEmpty()){
            System.out.println("[DEPURACION] Names esta vacio");
            regis.lblErrorNames.setText("*campo obligatorio*");
            validConfirmation = false;
        } else {
            regis.lblErrorNames.setText("");
        }
        
        if (getLastNames().isEmpty()){
            System.out.println("[DEPURACION] LastNames esta vacio");
            regis.lblErrorLastName.setText("*campo obligatorio*");
            validConfirmation = false;
        } else {
            regis.lblErrorLastName.setText("");
        }
        
        if (getUser().isEmpty()){
            System.out.println("[DEPURACION] user esta vacio");
            regis.lblErrorUser.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (getUser().length() < 8 || getUser().length() > 25){
            System.out.println("[DEPURACION] user fue ingresado en un rango fuera de lo permitido.");
            regis.lblErrorUser.setText("*los caracteres deben contener entre (10-25)*");
            validConfirmation = false;
        } else {
            regis.lblErrorUser.setText("");
        }
        
        if (getDni().isEmpty()){
            System.out.println("[DEPURACION] dni está vacio");
            regis.lblErrorDni.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (getDni().length() != 10){
            System.out.println("[DEPURACION] numero de caracteres invalidos");
            regis.lblErrorDni.setText("*debe contener 10 caracteres*");
            validConfirmation = false;
        } else {
            regis.lblErrorDni.setText("");
        }
        
        if (getEmail().isEmpty()){
            System.out.println("[DEPURACION] correo esta vacio");
            regis.lblErrorEmail.setText("*campo obligatorio*");
            validConfirmation = false;
        } else {
            regis.lblErrorEmail.setText("");
        }
        
        if (getPassword().isEmpty()){
            System.out.println("[DEPURACION] la contrasenia esta vacia");
            regis.lblErrorPassword.setText("*campo obligatorio*");
            validConfirmation = false;
        } else if (getPassword().length() < 8 || getPassword().length() > 25){
            System.out.println("[DEPURACION] numero de caracteres de contrasenia invalidos");
            regis.lblErrorPassword.setText("*debe contener entre (8-25) caracteres*");
            validConfirmation = false;
        } else {
            regis.lblErrorPassword.setText("");
        }
        return validConfirmation;
    }
}
