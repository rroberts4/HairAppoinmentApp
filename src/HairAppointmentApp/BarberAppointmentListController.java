///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            BarberAppointmentListController.java
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BarberAppointmentListController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Invoice> appointmentTableView;
    @FXML
    private TableColumn<Invoice,String> columnDate;
    @FXML
    private TableColumn<Invoice,String>  columnTime;
    @FXML
    private TableColumn<Invoice,String>  columnName;
    @FXML
    private TableColumn<Invoice,String>  columnService;
    @FXML
    private TableColumn<Invoice,String>  columnTotal;
    @FXML
    private Label exitLabel;
    @FXML
    private Button logoutButton, timeOffButton, confirmButton;

    String chosenDate, todaysDate;

    ObservableList<Invoice> list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        todaysDate = getTodaysDate();
        removePastDatesFromDatePicker();
        fillTableWithTodaysAppointments();




    }

    public  void fillTableWithTodaysAppointments(){

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection con = connectNow.getConnection();

            //Query returns all barbers appointments for today
            ResultSet result = con.createStatement().executeQuery("SELECT hair_appointment.date,hair_appointment.hour_slot,concat(firstname,' ',lastname), hair_services,concat('$',total,'.00') FROM hair_clients INNER JOIN hair_appointment\n" +
                    "ON hair_appointment.client_id = hair_clients.client_id AND hair_appointment.date = '" + todaysDate +"' AND stylists_id = '" + LoginSceneController.getBarberId() +
                    "' order by hour_slot;");

            while(result.next()) {
                /*
                With the Query being ordered by hour_slot Time Off Request will always
                be shown first in the event of a scheduling conflict we will only show
                the Barber his time off request, and not show their cancelled appointments
                 */

                if(result.getString("concat(firstname,' ',lastname)").equals("Barber's Time Off")){

                    list.add(new Invoice (result.getString("date"),"ALL DAY",result.getString("concat(firstname,' ',lastname)"),
                            result.getString("hair_services"),result.getString("concat('$',total,'.00')")));
                    break;
                } else{
                    list.add(new Invoice (result.getString("date"),result.getString("time"),result.getString("concat(firstname,' ',lastname)"),
                            result.getString("hair_services"),result.getString("concat('$',total,'.00')")));

                }

            }

        } catch(SQLException e) {
            e.printStackTrace();
            e.getCause();
            Logger.getLogger(BarberAppointmentListController.class.getName()).log(Level.SEVERE,null,e);

        }
        columnDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("invoiceTime"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        columnService.setCellValueFactory(new PropertyValueFactory<>("barberService"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        appointmentTableView.setItems(list);
    }
    public String getTodaysDate(){

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(currentDate);
    }

    public void removePastDatesFromDatePicker(){

        datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
    }



    public void setDate(){

        LocalDate selectedDate = datePicker.getValue();
        chosenDate = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
       confirmButton.setDisable(false);

    }
    public void confirmAction(ActionEvent event)throws Exception{
        confirmButton.setDisable(true);
        fillTableWithBarberAppointments();
    }

    public void exitLabelClicked(){
        Stage stage = (Stage) exitLabel.getScene().getWindow();
        stage.close();

    }

    public void logoutAction() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Stage window = (Stage) logoutButton.getScene().getWindow();
        window.setScene(new Scene(root, 720, 600));
    }

    public void fillTableWithBarberAppointments() throws Exception {
        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection con = connectNow.getConnection();

            //Query returns all barbers appointments for requested day
            ResultSet result = con.createStatement().executeQuery("SELECT hair_appointment.date,hair_appointment.time,concat(firstname,' ',lastname), hair_services,concat('$',total,'.00') FROM hair_clients,hair_appointment\n" +
                    "WHERE hair_appointment.client_id = hair_clients.client_id AND hair_appointment.date = '" + chosenDate + "' AND stylists_id = '" + LoginSceneController.getBarberId() +
                    "'order by hour_slot;");

            while (result.next()) {
                /*
                With the Query being ordered by hour_slot Time Off Request will always
                be shown first in the event of a scheduling conflict we will only show
                the Barber his time off request, and not show their cancelled appointments
                 */

                if (result.getString("concat(firstname,' ',lastname)").equals("Barber's Time Off")) {

                    list.add(new Invoice(result.getString("date"), "ALL DAY", result.getString("concat(firstname,' ',lastname)"),
                            result.getString("hair_services"), result.getString("concat('$',total,'.00')")));
                    break;
                } else {
                    list.add(new Invoice(result.getString("date"), result.getString("time"), result.getString("concat(firstname,' ',lastname)"),
                            result.getString("hair_services"), result.getString("concat('$',total,'.00')")));

                }


            }
        }catch(Exception e){
                e.printStackTrace();
                e.getCause();
                Logger.getLogger(BarberAppointmentListController.class.getName()).log(Level.SEVERE, null, e);

            }


            columnDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
            columnTime.setCellValueFactory(new PropertyValueFactory<>("invoiceTime"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            columnService.setCellValueFactory(new PropertyValueFactory<>("barberService"));
            columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));


            appointmentTableView.setItems(list);


        }


    public void timeOffAction(ActionEvent event) throws Exception  {
        Parent root = FXMLLoader.load(getClass().getResource("TimeOffScene.fxml"));
        Stage window =(Stage) timeOffButton.getScene().getWindow();
        window.setScene(new Scene(root, 720,600));
    }

}
