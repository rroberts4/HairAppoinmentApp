///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            LoginSceneController.java
// Date:            June 2021
//
// Author:          Ryan Jordan Roberts
/*
 * This Application allows customers to make appointments with hair stylists.
 * Users are able to select from a number barbers and hair services
 * Customers are able to make appointments in an available time slot for specific dates.
 * Barbers are able to login and see a list of set appointments.
 * Barbers are also able to request days off in which Customers can't make future appointments.
 * Any Scheduling conflicts created by a request off are reported to the Barber upon request.
 * Login and Appointment info are stored using a MYSQL Database.

 */
///////////////////////////////////////////////////////////////////////////////

package HairAppointmentApp;


import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;



public class LoginSceneController {

    @FXML
    private Button loginButton, exitButton;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    private static int barberId;


    public void checkIfLoginInfoIsCorrect() throws Exception{

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection con = connectNow.getConnection();

        //Query to determine if Login info is in SQL Database
        String verifyLogin = "SELECT count(1),stylists_id FROM hair_stylists WHERE username = '" + usernameTextField.getText() +"' AND password ='" + passwordTextField.getText()  +"'";

        try{

            Statement statement = con.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){

                if(queryResult.getInt("count(1)") == 1){
                    setBarberId(queryResult.getInt("stylists_id"));

                }else{
                    messageLabel.setText("Invalid Login. Please Try Again.");
                    return;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        //After verifying Login send Barber to Appointment Scene
        Parent root = FXMLLoader.load(getClass().getResource("BarberAppointmentList.fxml"));
        Stage window = (Stage) loginButton.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));
    }


    public void loginButtonAction(ActionEvent event) throws Exception {

        if(!checkIfUserLoginInfoIsEmpty()) {
            checkIfLoginInfoIsCorrect();
        }
    }
        public boolean checkIfUserLoginInfoIsEmpty(){
        if(!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()){
            return false;

        } else{
            messageLabel.setText("Please enter username and password!");
            return true;
        }

    }

    public void demoButtonAction(ActionEvent event)throws Exception  {
        setBarberId(2);
        Parent root = FXMLLoader.load(getClass().getResource("BarberAppointmentList.fxml"));
        Stage window = (Stage) loginButton.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));

    }
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }

    public static void setBarberId(int barberId) {
        LoginSceneController.barberId = barberId;
    }
    public static int getBarberId() {
        return barberId;
    }
}

