package com.mycompany.p2lab2robinzonalex;

import Controller.ControllerSaleStore;
import Controller.ControllerStore;
import Controller.ControllerUser;
import Model.ConexionMongoDB;
import Model.Products;
import View.AddProduct;
import View.FactureClients;
import View.Login;
import View.MainMenu;
import View.RecoverPassword;
import View.Registration;
import View.SaleStore;

public class P2Lab2RobinzonAlex {
    public static void main(String[] args) {
        AddProduct addProduct = new AddProduct();
        Login login = new Login();
        Registration regis = new Registration();
        ControllerUser  userController = new ControllerUser();
        RecoverPassword recover = new RecoverPassword();
        Products productModel = new Products("", "", "", "", "", "", "","");
        SaleStore storeMenuSecond = new SaleStore();
        ConexionMongoDB mongo = new ConexionMongoDB();
        MainMenu menu = new MainMenu();
        FactureClients factura = new FactureClients();
        ControllerSaleStore controlSaleStore = new ControllerSaleStore(storeMenuSecond, mongo);
        ControllerStore control = new ControllerStore(addProduct, productModel, 
                login, regis, userController, recover, storeMenuSecond, controlSaleStore, menu, factura);
        control.startViewLogin();
    }
}
