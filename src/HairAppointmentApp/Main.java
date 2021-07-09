///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            Main.java
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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("HomeScreenScene.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 720, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
