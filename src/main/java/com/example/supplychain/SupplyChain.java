package com.example.supplychain;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;


import java.io.IOException;

public class SupplyChain extends Application {

    public static final int width = 700, height = 600, headerBar = 50;
    Pane bodyPane = new Pane();
    public static int bodyWidth, bodyHeight;
    Login login = new Login();
    ProductDetails productDetails = new ProductDetails();

    Button globalLoginButton = new Button("Log In");
    Label customerEmailLabel = null;


    String customerEmail = null;
    private GridPane loginPage(){
        Label emailLabel = new Label("Email");
        Label passwordLabel = new Label("Password");
        Label messageLabel = new Label("I'm message");

        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String email = emailTextField.getText();
                String password = passwordField.getText();

            //    messageLabel.setText(email+ " $$" + password);
                if(login.customerLogin(email,password)){
                    messageLabel.setText("Login Successful");
                    customerEmail = email;
                    globalLoginButton.setVisible(false);
                    customerEmailLabel.setText("Welcome: " + customerEmail);
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getAllProducts());

                }
                else {
                    messageLabel.setText("Login Failed");
                }
            }
        });
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),bodyPane.getMinHeight());


        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(emailLabel,0 ,0 );
        gridPane.add(emailTextField,1,0);
        gridPane.add(passwordLabel,0,1);
        gridPane.add(passwordField,1,1);
        gridPane.add(loginButton,0,2);
        gridPane.add(messageLabel,1,2);
    return gridPane;
    }

    private GridPane headerBar(){
        TextField searchText = new TextField();
        Button searchButton = new Button("Search");

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String productName = searchText.getText();

                //clear body and put this new pane in the body
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getProductsByName(productName));

            }
        });
        customerEmailLabel = new Label("Welcome User ");
        globalLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
                globalLoginButton.setDisable(true);
                customerEmailLabel.setText("Welcome " + customerEmail);
            }
        });
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),bodyPane.getMaxHeight());
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //gridPane.setStyle("fx=background: #C0CBCB");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(searchText,0,0);
        gridPane.add(searchButton,1,0);
        gridPane.add(globalLoginButton, 2,0);
        gridPane.add(customerEmailLabel,3,0);
        return gridPane;
    }
    private GridPane footerBar(){
        Button addToCart = new Button("Add to cart");
        Button buyNowButton = new Button("Buy Now");
        Label messageLabel = new Label("");

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),bodyPane.getMaxHeight());
        gridPane.setVgap(5);
        gridPane.setHgap(50);
        //gridPane.setStyle("fx=background: #C0CBCB");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(headerBar+height);

        gridPane.add(addToCart,0,0);
        gridPane.add(buyNowButton,1,0);
        gridPane.add(messageLabel, 2, 0);
       // Label messageLabel = new Label("Done");
    buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Product selectedProduct = productDetails.getSelectedProduct();
            if(Order.placeOrder(customerEmail,selectedProduct)){
                messageLabel.setText("Ordered");

            }
            else {
                messageLabel.setText("Order Failed");
            }

        }
    });
        return gridPane;
    }
    private Pane createContent(){
        Pane root = new Pane();
        root.setPrefSize(width,height+2*headerBar+10);

        bodyPane.setMinSize(width,height);
        bodyPane.setTranslateY(headerBar);

        //bodyPane.getChildren().addAll(loginPage());
        bodyPane.getChildren().addAll(productDetails.getAllProducts());
        root.getChildren().addAll(headerBar(),bodyPane,footerBar());
        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SupplyChain.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("Supply Chain management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}