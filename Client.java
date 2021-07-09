///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            Client.java
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

public class Client {
    static  String clientName;
    static  String clientEmail;
    static  String clientPhone;

    public Client(String name, String email, String phone){
        this.clientName = name;
        this.clientEmail = email;
        this.clientPhone = phone;

    }

    public static String getClientName() {
        return clientName;
    }

    public static void setClientName(String clientName) {
        Client.clientName = clientName;
    }

    public static String getClientEmail() {
        return clientEmail;
    }

    public static void setClientEmail(String clientEmail) {
        Client.clientEmail = clientEmail;
    }

    public static String getClientPhone() {
        return clientPhone;
    }

    public static void setClientPhone(String clientPhone) {
        Client.clientPhone = clientPhone;
    }
}
