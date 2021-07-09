///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            TimeOffSceneController.java
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeOffSceneController {
    @FXML
    private TableView<Client> clientTableView;
    @FXML
    private TableColumn<Client,String> clientName;
    @FXML
    private TableColumn<Client,String>  clientEmail;
    @FXML
    private TableColumn<Client,String>  clientPhone;
    @FXML
    private Button confirmButton, exitButton, requestButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label messageLabel, backLabel;

    private String appointmentDate;

    private int hourSlot;

    ObservableList<Client> list = FXCollections.observableArrayList();



    private String[] appointmentSchedulingHours = new String[]{"10:00AM","11:00AM", "12:00PM", "1:00PM","2:00PM","3:00PM","4:00PM","5:00PM","6:00PM","7:00PM","8:00PM","9:00PM"};


    public void setDate(){
        LocalDate selectedDate = datePicker.getValue();
        appointmentDate = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        confirmButton.setVisible(true);
    }

    public void confirmButtonAction(ActionEvent event) {

        datePicker.setDisable(true);


        //check for conflicting appointments and fill table for barber to contact
        if (checkForClientAppointmentConflict()) {

            messageLabel.setText("Below is a list of clients who you need to inform of appointment cancellations");
            clientTableView.setVisible(true);
            fillScheduleConflictTable();
        } else {

            messageLabel.setText("Time Off Request has been Approved.");
        }

        removeDayFromBarberSchedule();

    }

    public void removeDayFromBarberSchedule(){
        for(int i =0; i< 12 ;i++){

            hourSlot = i+9;
            fillSchedulingTimeSlots(hourSlot,appointmentSchedulingHours[i]);
        }
    }


    private void fillScheduleConflictTable() {

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection con = connectNow.getConnection();

            String clientTableQuery ="SELECT hour_slot,concat(firstname,' ',lastname), email, phone FROM hair_clients INNER JOIN hair_appointment ON hair_appointment.client_id WHERE date ='" + appointmentDate +"';";
            ResultSet result = con.createStatement().executeQuery(clientTableQuery);

            while(result.next()){
                if(result.getInt("hour_slot") > 0) {

                    list.add(new Client(result.getString("concat(firstname,' ',lastname)"),
                            result.getString("email"), result.getString("phone")));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
            Logger.getLogger(BarberAppointmentListController.class.getName()).log(Level.SEVERE,null,e);

        }

        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        clientEmail.setCellValueFactory(new PropertyValueFactory<>("clientEmail"));
        clientPhone.setCellValueFactory(new PropertyValueFactory<>("clientPhone"));
        clientTableView.setItems(list);

    }

    private boolean checkForClientAppointmentConflict() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection con = connectNow.getConnection();

        String checkAppointments = "SELECT hour_slot FROM hair_appointment WHERE stylists_id = '" + LoginSceneController.getBarberId() + "' AND date = '" + appointmentDate + "';";

        try{

            Statement statement = con.createStatement();
            ResultSet queryResult = statement.executeQuery(checkAppointments);

            while(queryResult.next()){

                if(queryResult.getInt("hour_slot")  > 0){ return true;}
            }

        } catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return false;
    }

    private void fillSchedulingTimeSlots(int hour, String appointmentTime){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connect = connectNow.getConnection();

        int requestOffId = 1; //requestOffId is a pre-made client Id number in SQL database for time off request


        String insertField = "INSERT INTO hair_appointment (client_id,stylists_id,hair_services,date,time,hour_slot,total) Values('";

        String insertValues =  requestOffId + "','" + LoginSceneController.getBarberId()  + "','" + "----" + "','" + appointmentDate + "','" + appointmentTime   + "','" + 0  + "','" + 0 + "');";

        String appointmentInsertedValues = insertField + insertValues;

        try {
            Statement statement = connect.createStatement();
            statement.executeUpdate(appointmentInsertedValues);
            statement.close();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
    }


    public void backLabelClicked() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("BarberAppointmentList.fxml"));
        Stage window = (Stage) backLabel.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));
    }


    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


    public void requestButtonAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TimeOffScene.fxml"));
        Stage window = (Stage) requestButton.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));
    }
}
