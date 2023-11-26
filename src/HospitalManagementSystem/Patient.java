package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient
{
    private final Connection connection;
    private final Scanner scanner;

    // constructor
    public Patient(Connection connection, Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }

    // methods
    // adding a patient
    public void addPatient()
    {
        System.out.print("Enter Patient name: ");
        String name = scanner.next();

        System.out.print("Enter Patient gender: ");
        String gender = scanner.next();

        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();

        try
        {
            String query = "insert into patients (name, age, gender) values(?, ?, ?); ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0)
            {
                System.out.println("Patient added Successfully !");
            }
            else
            {
                System.out.println("Patient was not added due to some internal error!");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error when adding a patient : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
    }

    // viewing a patient's details
    public void viewPatients()
    {
        try
        {
            String query = "select * from patients;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients : ");
            System.out.println("+------------+--------------------------+----------+----------------+");
            System.out.println("| Patient ID | Name                     | Age      | Gender         |");
            System.out.println("+------------+--------------------------+----------+----------------+");
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                System.out.printf("|%-12s|%-26s|%-10s|%-16s|\n", id, name, age, gender);
                System.out.println("+------------+--------------------------+----------+----------------+");

            }
        }
        catch (SQLException e)
        {
            System.out.println("Error when viewing patient details : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
    }

    // patient search
    public boolean getPatientById(int id)
    {
        try
        {
            String query = "select * from patients where id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
        catch (SQLException e)
        {
            System.out.println("Error when viewing patient details : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
        return false;
    }

}
