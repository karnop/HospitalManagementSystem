package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement
{
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "tictactoe";

    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error when connecting to database: ");
            System.out.println("-> " + e.getMessage());
        }

        // making connection
        try
        {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while(true)
            {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View all Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");

                System.out.print("Enter choice : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> {
                        // add patient
                        patient.addPatient();
                        System.out.println();
                    }
                    case 2 -> {
                        // view all patients
                        patient.viewPatients();
                        System.out.println();
                    }
                    case 3 -> {
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                    }
                    case 4 -> {
                        // Book Appointment
                        bookAppointment(connection, scanner, patient, doctor);
                        System.out.println();
                    }
                }

                System.out.print("Exit? (y/n) : ");
                String chr = scanner.next();
                if(chr.equalsIgnoreCase("n"))   return;
                //clearing console
                clearConsole();
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error when making connection : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
    }

    private static void clearConsole()
    {
       for (int i=1; i<100; i++)
           System.out.println();
    }

    // booking an appointment
    public static void bookAppointment(Connection connection, Scanner scanner, Patient patient, Doctor doctor)
    {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();

        System.out.print("enter appointment date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();

        boolean patientExists = patient.getPatientById(patientId);
        boolean doctorExists = doctor.getDoctorById(doctorId);
        if (patientExists && doctorExists)
        {
            if(doctorAvailable(connection, doctorId, appointmentDate))
            {
                try
                {
                    String query = "insert into appointments(patient_id, doctor_id, appointment_date) values (?, ?, ?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if(rowsAffected>0)
                    {
                        System.out.println("Appointment Booked! ");
                    }
                    else
                    {
                        System.out.println("Failed to book appointment!");
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("Error when making appointment : ");
                    System.out.println("-> " + e.getErrorCode());
                    System.out.println("-> " + e.getMessage());
                }
            }

            else
            {
                System.out.println("Doctor is not available on this date!!");
            }
        }
        else
        {
            if(!patientExists)
                System.out.println("Patient id doesn't exists");
            if(!doctorExists)
                System.out.println("Doctor id doesn't exists");
        }

    }

    private static boolean doctorAvailable(Connection connection, int doctorId, String appointmentDate)
    {
        try
        {
            String query = "select count(*) from appointments where doctor_id = ? and appointment_date = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int count = resultSet.getInt(1);
                return count == 0;
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error when making appointment : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
        return false;
    }
}
