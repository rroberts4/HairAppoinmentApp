///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            AppointmentConfirmedController.java
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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentConfirmedController implements Initializable {
    @FXML
    private Label appointmentMessageLabel, appointmentDetailsMessageLabel;
    @FXML
    private Button exitButton, newAppointmentButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setAppointmentConfirmationMessageLabels();
    }
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
    public void newAppointmentButtonAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HomeScreenScene.fxml"));
        Stage window = (Stage) newAppointmentButton .getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));



    }
    public void setAppointmentConfirmationMessageLabels(){

        String appointmentMessage = "Your appointment has been booked with " + HomeScreenSceneController.getBarberChoice() +
                "on " + AppointmentCalendarSceneController.getAppointmentDate();
        String appointmentDetails =  "Barber Services: " + HomeScreenSceneController.getServices() +
                "      Time: "  + AppointmentCalendarSceneController.getAppointmentTime() +
                "      Payment Owed: $" + HomeScreenSceneController.getTotal();
        appointmentMessageLabel.setText(appointmentMessage);

        appointmentDetailsMessageLabel.setText(appointmentDetails);

    }
}
