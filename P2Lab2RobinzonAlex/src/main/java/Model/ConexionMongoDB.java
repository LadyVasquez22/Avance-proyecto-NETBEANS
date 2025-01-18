package Model;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class ConexionMongoDB {

    private final MongoClient mongoClient;
    private MongoDatabase database;
    private final String databaseName = "P2Lab2Robinzon";
    private final String collectionNameUser = "Usuarios";
    private final String collectionNameProducts = "ProductsAdd";
    private final String collectionNameCarProducts = "CarritoDeCompras";

    public ConexionMongoDB() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase(databaseName);
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionNameUser)) {
            database.createCollection(collectionNameUser);
        }

        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionNameProducts)) {
            database.createCollection(collectionNameProducts);
        }
        
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionNameCarProducts)) {
            database.createCollection(collectionNameCarProducts);
        }
    }

    public MongoDatabase getDataBase() {
        return database;
    }

    public MongoCollection<Document> getCollectionInv() {
        return database.getCollection("ProductsAdd");
    }

    public MongoDatabase createConnection() {
        try {
            database = getDataBase();
            return database;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createDocument(Document document) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection collection = db.getCollection(collectionNameUser);
                collection.insertOne(document);
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean saveCarBuys(Document doc) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionNameCarProducts);
                collection.insertOne(doc);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createDocumentProducts(Document document) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection collection = db.getCollection(collectionNameProducts);
                collection.insertOne(document);
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Document> readDocument(Document document) {
        List<Document> results = new ArrayList<>();
        try {
            MongoDatabase db = createConnection();
            MongoCollection<Document> collection = db.getCollection(collectionNameUser);
            results = collection.find(document).into(results);
            return results;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean deleteProductFromUser(String userId, String productId) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionNameUser);
            Document filter = new Document("_id", new ObjectId(userId));
            Document update = new Document("$pull", new Document("Products", new Document("Código", productId)));
            collection.updateOne(filter, update);
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProductByUser(String userId, String codigo, Document updatedProduct) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionNameUser);
            Document filter = new Document("_id", new ObjectId(userId))
                    .append("Products.Código", codigo); // Busca por userId y código del producto
            Document update = new Document("$set", new Document("Products.$.Código", updatedProduct.getString("Código"))
                    .append("Products.$.NombreP", updatedProduct.getString("NombreP"))
                    .append("Products.$.Precio Unitario", String.valueOf(updatedProduct.getString("Precio Unitario")))
                    .append("Products.$.Precio Venta", String.valueOf(updatedProduct.getString("Precio Venta")))
                    .append("Products.$.IVA", updatedProduct.getString("IVA"))
                    .append("Products.$.Stock Mínimo", String.valueOf(updatedProduct.getString("Stock Mínimo")))
                    .append("Products.$.Stock Actual", String.valueOf(updatedProduct.getString("Stock Actual")))
                    .append("Products.$.Categoria", updatedProduct.getString("Categoria")));
            collection.updateOne(filter, update);
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateProduct(Bson filter, Document doc) {
        try{
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionNameProducts);
                collection.updateOne(filter, new Document("$set",doc));
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addProductToUser(String userId, Document product) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionNameUser);
                Document filter = new Document("_id", new ObjectId(userId));
                Document update = new Document("$push", new Document("Products", product));
                collection.updateOne(filter, update);
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Document> searchDocument(Document filtro) {
        MongoDatabase db = createConnection();
        try {
            MongoCollection<Document> collection = db.getCollection(collectionNameUser);
            ArrayList<Document> resultados = new ArrayList<>();
            for (Document doc : collection.find(filtro)) {
                resultados.add(doc);
            }
            return resultados;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Document> searchProduct(Bson filtro) {
        MongoDatabase db = createConnection();
        try {
            MongoCollection<Document> collection = db.getCollection(collectionNameProducts);
            ArrayList<Document> resultados = new ArrayList<>();
            for (Document doc : collection.find(filtro)) {
                resultados.add(doc);
            }
            return resultados;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Document> searchProductByUser(String userId, String codigo) {

        try {
            MongoCollection<Document> collection = database.getCollection(collectionNameUser);
            Document filter = new Document("_id", new ObjectId(userId));

            // Buscando el producto por código dentro de la lista de productos del usuario
            Document query = new Document("Products.Código", codigo);
            Document userDoc = collection.find(filter).first();  //asegurar de que el filtro incluya el código del producto

            ArrayList<Document> resultados = new ArrayList<>();
            if (userDoc != null) {
                List<Document> products = (List<Document>) userDoc.get("Products");
                if (products != null) {
                    for (Document product : products) {
                        if (product.getString("Código").equals(codigo)) {
                            resultados.add(product); // Este añade el producto a los resultados si coincide el código
                        }
                    }
                }
            }
            return resultados;
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DefaultTableModel loadDataTable(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("La variable UserID no puede ser vacia o nula");
        }

        ObjectId objectId = new ObjectId(userId);
        MongoCollection<Document> collection = database.getCollection(collectionNameUser);
        Document filter = new Document("_id", new ObjectId(userId));
        Document userDoc = collection.find(filter).first();

        if (userDoc == null) {
            throw new IllegalArgumentException("Usuario no encontrado en la base de datos.");
        }

        List<Document> products = (List<Document>) userDoc.get("Products");
        if (products == null) {
            products = new ArrayList<>(); // Inicializa como un array vacío si es null
        }

        String[] columnNames = {"Código", "NombreP", "Precio Unitario", "Precio Venta", "IVA", "Stock Mínimo", "Stock Actual", "Categoria"};
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);

        for (Document product : products) {
            Object[] row = {
                product.getString("Código"),
                product.getString("NombreP"),
                product.getString("Precio Unitario"),
                product.getString("Precio Venta"),
                product.getString("IVA"),
                product.getString("Stock Mínimo"),
                product.getString("Stock Actual"),
                product.getString("Categoria")
            };
            tdm.addRow(row);
        }
        return tdm;
    }

    public List<Document> getAllProductsFromStore() {
        List<Document> allProducts = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(collectionNameProducts);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                allProducts.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return allProducts;
    }

}
