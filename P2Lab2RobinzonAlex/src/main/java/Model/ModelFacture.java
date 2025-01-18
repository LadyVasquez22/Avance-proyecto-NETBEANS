package Model;

public class ModelFacture implements Interface{
    private String lastNames;
    private String names;
    private String addres;
    private String dni;
    private String email;
    private String cellPhone;
    //validaciones de las entradas
    public boolean validConfirmation = true;

    public ModelFacture(String lastNames, String names, String addres, String dni, String email, String cellPhone) {
        this.lastNames = lastNames;
        this.names = names;
        this.addres = addres;
        this.dni = dni;
        this.email = email;
        this.cellPhone = cellPhone;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
    
    public boolean validationsFacture() {
        validConfirmation = true;
        
        return validConfirmation;
    }
    //productos comprados y el precio total
    //nombre del cliente
    @Override 
    public void getData() {
        
    }
}
