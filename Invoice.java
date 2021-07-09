///////////////////////////////////////////////////////////////////////////////
//
// Title:           HairAppointmentApp
// Main Class File: HairAppointmentApp.Main.java
// File:            Invoice.java
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

public class Invoice {
    static  String invoiceDate;
   private static  String invoiceTime;
    static  String clientName;
    static  String barberService;
    static String totalPrice;






    public Invoice(String date, String time, String name, String service, String price){
        invoiceDate = date;
        invoiceTime = time;
        clientName = name;
        barberService = service;
        totalPrice = price;

    }

    public static String getInvoiceDate() {
        return invoiceDate;
    }

    public static void setInvoiceDate(String date) {
        invoiceDate = date;
    }

    public static String getInvoiceTime() {
        return invoiceTime;
    }

    public static void setInvoiceTime(String time) {
        invoiceTime = time;
    }

    public static String getClientName() {
        return clientName;
    }

    public static void setClientName(String name) {
        clientName = name;
    }

    public static String getBarberService() {
        return barberService;
    }

    public static void setBarberService(String service) {
        barberService = service;
    }

    public static String getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(String price) {
        totalPrice = price;
    }
}
