///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            AppointmentCalendarSceneController.java
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class AppointmentCalendarSceneController implements Initializable {
    @FXML
    private Label timeMessageLabel, backLabel;
    @FXML
    private Button tenAMButton, elevenAMButton, twelvePMButton, onePMButton, twoPMButton,
            threePMButton, fourPMButton, fivePMButton, sixPMButton, sevenPMButton,
            eightPMButton, ninePMButton;
    @FXML
            private DatePicker datePicker;

    @FXML
    private Button confirmButton;
    public Button[] timeButtons;
    private static String appointmentDate, appointmentTime;
    private int hourSlot;


    @Override
    public void initialize(URL location, ResourceBundle resources){


    initializeButtonArray();

    }
    public void initializeButtonArray() {

        timeButtons = new Button[12];
        timeButtons[0] = tenAMButton;
        timeButtons[1] = elevenAMButton;
        timeButtons[2] = twelvePMButton;
        timeButtons[3] = onePMButton;
        timeButtons[4] = twoPMButton;
        timeButtons[5] = threePMButton;
        timeButtons[6] = fourPMButton;
        timeButtons[7] = fivePMButton;
        timeButtons[8] = sixPMButton;
        timeButtons[9] = sevenPMButton;
        timeButtons[10] = eightPMButton;
        timeButtons[11] = ninePMButton;
    }

    public void setDate(){
        LocalDate selectedDate = datePicker.getValue();
        appointmentDate = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        confirmButton.setVisible(true);
    }

    public void backLabelClicked() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("HomeScreenScene.fxml"));
        Stage window = (Stage) backLabel.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));
    }
    public void confirmButtonAction(ActionEvent event){



        timeMessageLabel.setVisible(true);
        checkBarberSchedule();

    }
    private void checkBarberSchedule(){
        int barbershopHours = 9;

        for(int i=0; i < timeButtons.length; i++){
            barbershopHours = barbershopHours + i;
           boolean result = checkBarberHourSlot(timeButtons[i],barbershopHours);
            timeButtons[i].setDisable(result);



        }
    }
    private boolean checkBarberHourSlot(Button timeButton, int hour){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection con = connectNow.getConnection();

        String verifyTime = "SELECT count(1) FROM hair_appointment WHERE hour_slot = '" + hour +"' AND date ='" + appointmentDate  +"'";

        try{

            Statement statement = con.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyTime);



            while(queryResult.next()){

                if(queryResult.getInt("count(1)") != 1){
                    return false;







                }

            }
            return true;

        } catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return true;
    }


    private void createAppointment(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connect = connectNow.getConnection();




        String insertField = "INSERT INTO hair_appointment (client_id,stylists_id,hair_services,date,time,hour_slot,total) Values('";

        String insertValues = HomeScreenSceneController.getClientId()+ "','" + HomeScreenSceneController.getBarberId()  +
                "','" + HomeScreenSceneController.getServices() + "','" +
                appointmentDate + "','" + appointmentTime   + "','" +
                hourSlot  + "','" + HomeScreenSceneController.getTotal() + "');";

        String appointmentInsertedValues = insertField + insertValues;

        try {
            Statement statement = connect.createStatement();
            statement.executeUpdate(appointmentInsertedValues);
            statement.close();

            Parent root = FXMLLoader.load(getClass().getResource("AppointmentConfirmed.fxml"));
            Stage window = (Stage) confirmButton.getScene().getWindow();
            window.setScene(new Scene(root, 720, 600));


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
    }

    public static String getAppointmentDate() {
        return appointmentDate;
    }

    public static String getAppointmentTime() {
        return appointmentTime;
    }

    public void tenAMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "10:00AM";
        hourSlot = 10;
        createAppointment();
    }

    public void elevenAMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "11:00AM";
        hourSlot = 11;
        createAppointment();

    }
    public void twelvePMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "12:00PM";
        hourSlot = 12;
        createAppointment();

    }
    public void onePMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "1:00PM";
        hourSlot = 13;
        createAppointment();

    }
    public void twoPMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "2:00PM";
        hourSlot = 14;
        createAppointment();

    }
    public void threePMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "3:00PM";
        hourSlot = 15;
        createAppointment();

    }
    public void fourPMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "4:00PM";
        hourSlot = 16;
        createAppointment();

    }
    public void fivePMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "5:00PM";
        hourSlot = 17;
        createAppointment();

    }
    public void sixPMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "6:00PM";
        hourSlot = 18;
        createAppointment();

    }
    public void sevenPMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "7:00PM";
        hourSlot = 19;
        createAppointment();

    }
    public void eightPMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "8:00PM";
        hourSlot = 20;
        createAppointment();

    }
    public void ninePMButtonAction(ActionEvent event)throws Exception{
        appointmentTime = "9:00PM";
        hourSlot = 21;
        createAppointment();

    }

    }

