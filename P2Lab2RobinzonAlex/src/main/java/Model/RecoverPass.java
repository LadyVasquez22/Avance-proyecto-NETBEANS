package Model;

import View.RecoverPassword;
import java.util.ArrayList;
import org.bson.Document;

public class RecoverPass {
    private String dniRecover;
    private String emailRecover;
    ConexionMongoDB mongo = new ConexionMongoDB();
    private String user;
    private String password;
    
    private boolean isValid = true;

    public RecoverPass(String dniRecover, String emailRecover) {
        this.dniRecover = dniRecover;
        this.emailRecover = emailRecover;
    }

    public String getDniRecover() {
        return dniRecover;
    }

    public void setDniRecover(String dniRecover) {
        this.dniRecover = dniRecover;
    }

    public String getEmailRecover() {
        return emailRecover;
    }

    public void setEmailRecover(String emailRecover) {
        this.emailRecover = emailRecover;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
     public boolean searchAccount() {
        Document filter = new Document("DNI", dniRecover).append("Email", emailRecover);
        ArrayList<Document> resultados = mongo.searchDocument(filter);

        if (resultados != null && !resultados.isEmpty()) {
            Document userDoc = resultados.get(0);
            user = userDoc.getString("User");
            password = userDoc.getString("Password");
            return true;
        } else {
            return false;
        }
    }   
     
    public boolean validationsRecover(RecoverPassword recover){
        isValid = true;
        if (getDniRecover().isEmpty() || getDniRecover() == null){
            System.out.println("[DEPURACION] el campo dni esta vacio");
            recover.lblErrorCedula.setText("*campo obligatorio*");
            isValid = false;
        } else {
            recover.lblErrorCedula.setText("");
        }
        
        if (getEmailRecover().isEmpty() || getEmailRecover() == null){
            System.out.println("[DEPURACION] el campo email esta vacio");
            recover.lblErrorEmail.setText("*campo obligatorio*");
            isValid = false;
        } else {
            recover.lblErrorEmail.setText("");
        }
        return isValid;
    }
}
