package com.pluralsight;

import com.pluralsight.dao.ShipperDao;
import org.apache.commons.dbcp2.BasicDataSource;
import java.util.Scanner;

public class App {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: java com.pluralsight.App <username> <password>");
            System.exit(1);
        }
        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        ShipperDao dataManagerActor = new ShipperDao(dataSource);

        String[] shipperData = promptNewShipperData().split("\\s+", 2);
        int newId = dataManagerActor.insertShipper(shipperData[0], shipperData[1]);
        System.out.println("Shipper ID: " + newId + " successfully inserted!");

        dataManagerActor.displayShippers();

        String[] updateData = promptChangePhoneNumber().split("\\s+", 2);
        int updateId = Integer.parseInt(updateData[0]);
        String newPhone = updateData[1];
        dataManagerActor.updateShipperPhone(updateId, newPhone);
        System.out.println("Shipper with ID: " + updateId + " phone successfully updated!");

        dataManagerActor.displayShippers();

        Integer idInput = promptDeleteShipper();
        if (idInput == null) {
            System.out.println("\nDummy! Not allowed to delete ID 1-3!");
        } else {
            dataManagerActor.deleteShipper(idInput);
            System.out.println("Shipper with ID: " + idInput + " successfully deleted!");
        }

        dataManagerActor.displayShippers();
    }

    public static String promptNewShipperData() {
        System.out.print("\nEnter a new shipper name and phone (separated by a space): ");
        return input.nextLine().trim();
    }

    public static String promptChangePhoneNumber() {
        System.out.print("\nEnter an existing ID and new phone number (separated by a space): ");
        return input.nextLine().trim();
    }

    public static Integer promptDeleteShipper() {
        System.out.print("\nEnter a shipper ID to delete a shipper (1-3 not allowed): ");
        int idInput = input.nextInt();
        input.nextLine();
        return (idInput <= 3) ? null : idInput;
    }
}