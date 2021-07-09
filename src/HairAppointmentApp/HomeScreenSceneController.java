///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            HomeScreenController.java
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenSceneController implements Initializable {

    @FXML
    private Button appointmentButton;

    @FXML
    private Label barberServiceLabel,errorMessageLabel, exitLabel, loginLabel, userInfoLabel;

    @FXML
    private VBox userInfoVBox;

    @FXML
    private TextField firstNameTextfield, lastNameTextfield, phoneTextfield, emailTextfield;

    @FXML
    private ComboBox<String> barberComboBox, barberServicesComboBox, addServicesComboBox;

    private  static String timeStamp, barberChoice, addChoice, serviceChoice;
    private static int total, clientId, barberId;
    private static String barberService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //List of all pre-made appointment selection options for the client
        ObservableList<String> barberList = FXCollections.observableArrayList("Timothy 'Rock' ", "Greg T.", "Kyle M.", "Mylo B.", "Lamont 'Bustop'", "Andy C.",   "Ramon 'Red'", "Marcus C.");
        ObservableList<String> serviceList = FXCollections.observableArrayList("Adult Haircut $30", "Kids Haircut $20", "Senior Trim $20");
        ObservableList<String> addServicesList = FXCollections.observableArrayList("No extra services", "Beard Trim $15");

        barberComboBox.setItems(barberList);
        barberServicesComboBox.setItems(serviceList);
        addServicesComboBox.setItems(addServicesList);

    }

    private boolean checkIfUserInfoIsEmpty() {
        if(firstNameTextfield.getText().isEmpty() ||  lastNameTextfield.getText().isEmpty() || phoneTextfield.getText().isEmpty() || emailTextfield.getText().isEmpty()){
            errorMessageLabel.setText("Please make sure to completely fill out all user info before booking");
            return false;
        }
        return true;
    }

    private boolean checkIfUserLeftComboBoxesEmpty() {

        //checks if client made comboBox selection empty and stores a total price value based on their selections

        if(barberComboBox.getSelectionModel().isEmpty() || barberServicesComboBox.getSelectionModel().isEmpty() || addServicesComboBox.getSelectionModel().isEmpty()){
            errorMessageLabel.setText("Please make sure to completely fill out all barber info before booking");
            return false;
        }
        serviceChoice = barberServicesComboBox.getSelectionModel().getSelectedItem();
        addChoice = addServicesComboBox.getSelectionModel().getSelectedItem();

        if(serviceChoice.equals("Adult Haircut $30")){
            barberServiceLabel.setText("Includes fades, tappers, trim or flattop for clients over the age  of 15");
            barberService = "Adult Haircut";
             total = 30;

        }
        if(serviceChoice.equals("Kids Haircut $20")){
            barberServiceLabel.setText("Kids regular hair cuts for clients 15 and younger");
            barberService = "Kid Haircut";
             total = 20;
        }
        if(serviceChoice.equals("Senior Haircut $20")){
            barberServiceLabel.setText("Includes fades, tappers, trim or flattop for clients over the age  of 65");
            barberService = "Senior Haircut";
             total = 20;
        }
        if(addChoice.equals("Beard Trim $15")){
            barberService = barberService + "Beard Trim";
             total = total + 15;
        }

        return true;
        
    }

    public static  String getServices(){
        return barberService;
    }


    public  static int getTotal() {

        return total;
    }

    public void appointmentButtonAction(ActionEvent event) throws Exception {

        if(checkIfUserLeftComboBoxesEmpty() && checkIfUserInfoIsEmpty()) {

            //Store booking time and all client and barber info for upcoming appointment

            LocalDateTime bookingTime = LocalDateTime.now();
            timeStamp = bookingTime.toString();
            setBarberInformation();
            setClientInformation();

        }


    }
    public void setBarberInformation() {

        //Based on the Barber comboBox selection we store the corresponding barberId
        barberChoice = barberComboBox.getSelectionModel().getSelectedItem();

        if (barberChoice.equals("Timothy 'Rock' ")) {
            barberId = 1;

        }
        if (barberChoice.equals("Greg T.")) {
            barberId = 2;

        }
        if (barberChoice.equals("Kyle M.")) {
            barberId = 3;

        }
        if (barberChoice.equals("Mylo B.")) {
            barberId = 4;

        }
        if (barberChoice.equals("Lamont 'Bustop'")) {
            barberId = 5;

        }
        if (barberChoice.equals("Andy C.")) {
            barberId = 6;

        }
        if (barberChoice.equals("Ramon 'Red'")) {
            barberId = 7;

        }
        if (barberChoice.equals("Marcus C.")) {
            barberId = 8;

        }
    }
    public void confirmButtonAction(ActionEvent event)  {
      if(checkIfUserLeftComboBoxesEmpty() == false){
          return;
      }

      userInfoVBox.setVisible(true);
      userInfoLabel.setVisible(true);




    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public TextField getEmailTextfield() {
        return emailTextfield;
    }

    private void setClientInformation() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connect = connectNow.getConnection();



        String firstname = firstNameTextfield.getText();
        String lastname = lastNameTextfield.getText();
        String email = emailTextfield.getText();
        String phoneNumber = phoneTextfield.getText();

        //Query enters hair client's / guest info into the SQL Database

        String insertField = "INSERT INTO hair_clients (firstname,lastname,email,phone,timestamp) Values('";

        String insertValues = firstname + "','" + lastname + "','" + email + "','" + phoneNumber + "','" + timeStamp + "');";

        String clientInsertedValues = insertField + insertValues;

        try {
            Statement statement = connect.createStatement();
            statement.executeUpdate(clientInsertedValues);
            statement.close();
            obtainClientId();

            Parent root = FXMLLoader.load(getClass().getResource("AppointmentCalendarScene.fxml"));
            Stage window = (Stage) appointmentButton.getScene().getWindow();
            window.setScene(new Scene(root, 720, 600));


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
    }
 public void exitLabelClicked(){
     Stage stage = (Stage) exitLabel.getScene().getWindow();
     stage.close();

 }
    public void barberLoginLabelClicked() throws  Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Stage window = (Stage) loginLabel.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));

    }
    public void obtainClientId(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection con = connectNow.getConnection();

        /*
            Query returns a client id based on email of the client and the time appointment request is started
            until the customer chooses an available appointment time they are considered a guest

         */
        String verifyClientId = "SELECT count(1),client_id FROM hair_clients WHERE timestamp = '" + timeStamp +"' AND email ='" + emailTextfield.getText()  +"'";

        try{

            Statement statement = con.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyClientId);

            while(queryResult.next()){

                if(queryResult.getInt("count(1)") == 1){
                    setClientId(queryResult.getInt("client_id"));
                }

            }

        } catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public static int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public static int getBarberId() {
        return barberId;
    }
    public static String getBarberChoice() {
        return barberChoice;
    }
}
