package Model;

import View.Login;

public class SellerLogin extends User{
    
    boolean validConfirmations = true;

    public SellerLogin(String user, String password) {
        super (user, password);
    }
    
    public boolean validationsLogin(Login login){
        validConfirmations = true;
        
        if (getUser().isEmpty()){
            System.out.println("[DEPURACION] campo usuario vacio");
            login.lblErrorUser.setText("*campo obligatorio*");
            validConfirmations = false;
        } else {
            login.lblErrorUser.setText("");
        }
        
        if (getPassword().isEmpty()){
            System.out.println("[DEPURACION] campo contrasenia vacio");
            login.lblErrorPassword.setText("*campo obligatorio");
            validConfirmations = false;
        } else {
            login.lblErrorPassword.setText("");
        }
        return validConfirmations;
    }

    @Override
    public void getData() {}
}
