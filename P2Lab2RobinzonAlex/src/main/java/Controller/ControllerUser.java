package Controller;

import Model.ConexionMongoDB;
import Model.SellerRegiste;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ControllerUser {
    private ConexionMongoDB mongo = new ConexionMongoDB();
    
    //método para registrar USUARIOS en mongo
    public String registerUser(SellerRegiste user){
        Document doc = new Document("Names", user.getNames())
                .append("LastNames", user.getLastNames())
                .append("User", user.getUser())
                .append("DNI", user.getDni())
                .append("Email", user.getEmail())
                .append("Password", user.getPassword())
                .append("Products", new ArrayList<>());
        if (mongo.createDocument(doc)) {
            ObjectId userId = (ObjectId) doc.get("_id"); // Recupera el ObjectId generado
            System.out.println("[INFO] Usuario registrado: " + user.getUser() + ", ID: " + userId.toHexString());
            return userId.toHexString(); // Devuelve el userId
        } else {
            System.out.println("[ERROR] Error al registrar el usuario: " + user.getUser());
            return null;
        }
    }
    
    //método para iniciar sesión
    public boolean login(String userInput, String passwordInput){
        Document filter = new Document("User", userInput);
        List<Document> resultados = mongo.readDocument(filter);

    if (resultados != null && !resultados.isEmpty()) {
        String storedPassword = resultados.get(0).getString("Password");
        if (storedPassword.equals(passwordInput)) {
            System.out.println("[INFO] Inicio de sesion exitoso para: " + userInput);
            return true;
        } else {
            System.out.println("[ERROR] Contrasenia incorrecta");
            return false;
        }
    } else {
        System.out.println("[ERROR] Usuario no encontrado");
        return false;
    }
    }
    
    public void loginAdmin() {
        
    }
    
     // Obtener todos los usuarios registrados
    public List<SellerRegiste> getRegisteredUsers() {
        List<SellerRegiste> users = new ArrayList<>();
        List<Document> documentos = mongo.readDocument(new Document());

        for (Document doc : documentos) {
            SellerRegiste user = new SellerRegiste(
                    doc.getString("Names"),
                    doc.getString("LastNames"),
                    doc.getString("User"),
                    doc.getString("DNI"),
                    doc.getString("Email"),
                    doc.getString("Password")
            );
            users.add(user);
        }
        return users;
    }

}
