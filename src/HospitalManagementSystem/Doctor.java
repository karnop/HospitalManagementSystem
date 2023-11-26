package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor
{

    private final Connection connection;

    // constructor
    public Doctor(Connection connection)
    {
        this.connection = connection;
    }

    // methods

    // viewing doctor details
    public void viewDoctors()
    {
        try
        {
            String query = "select * from doctors;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors : ");
            System.out.println("+------------+--------------------------+--------------------------+");
            System.out.println("| Doctor ID  | Name                     | Specialization           |");
            System.out.println("+------------+--------------------------+--------------------------+");
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12s|%-26s|%-26s|\n", id, name, specialization);
                System.out.println("+------------+--------------------------+--------------------------+");

            }
        }
        catch (SQLException e)
        {
            System.out.println("Error when viewing doctor details : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
    }

    // patient search
    public boolean getDoctorById(int id)
    {
        try
        {
            String query = "select * from doctors where id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
        catch (SQLException e)
        {
            System.out.println("Error when viewing doctor details : ");
            System.out.println("-> " + e.getErrorCode());
            System.out.println("-> " + e.getMessage());
        }
        return false;
    }

}
