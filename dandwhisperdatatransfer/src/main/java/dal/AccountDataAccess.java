package dal;

import dtos.AccountDto;
import dtos.CharacterDto;
import dtos.CharacterPermissionsDto;
import dtos.LoginDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDataAccess extends DataAccess {


    public AccountDto login(String username) {
        AccountDto accountDto = null;
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT [id], [username], [gamemaster] FROM [DanDWhisper].[Account] WHERE [username] = ?");
            statement.setString(1, username);

            rs = statement.executeQuery();

            while (rs.next()) {
                int userIdOutput = rs.getInt("id");
                String userNameOutput = rs.getString("username");
                boolean gameMasterOutput = rs.getBoolean("gamemaster");

                CharacterPermissionsDto characterPermissionsDto = getCharacterPermissions(userIdOutput);

                accountDto = new AccountDto(userIdOutput, userNameOutput, gameMasterOutput, characterPermissionsDto.getCharacterPermissions());

                System.out.println("user id = " + userIdOutput);
                System.out.println("username = " + userNameOutput);
                System.out.println("gamemaster = " + gameMasterOutput);
                System.out.println("character permissions = " + characterPermissionsDto.getCharacterPermissions());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if(statement!= null){
                    statement.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return accountDto;

    }

    public boolean registerUser(String username, String password) {
        boolean success = false;
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(" INSERT INTO  [DanDWhisper].[Account] ([username], [encrypted_password])" + " values (?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            // set timeout to 30 sec.
            statement.setQueryTimeout(30);

            statement.execute();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if(statement!= null){
                    statement.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return success;
    }

    public boolean getUserAlreadyExists(String username){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        boolean exists = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT [username] FROM [DanDWhisper].[Account] WHERE [username] = ?");
            statement.setString(1, username);

            rs = statement.executeQuery();

            while (rs.next()) {
                String userNameOutput = rs.getString("username");

                if(!userNameOutput.isEmpty())
                    exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if(statement!= null){
                    statement.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return exists;
    }

    public LoginDto getCredentials(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        LoginDto loginDto = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT [username], [encrypted_password] FROM [DanDWhisper].[Account] WHERE [username] = ?");
            statement.setString(1, username);

           rs = statement.executeQuery();

            while (rs.next()) {
                String usernameOutput = rs.getString("username");
                String passwordOutput = rs.getString("encrypted_password");

                loginDto = new LoginDto(usernameOutput, passwordOutput);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if(statement!= null){
                    statement.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return loginDto;
    }

    public CharacterPermissionsDto getCharacterPermissions(int accountId) {
        Connection connection = null;
        PreparedStatement statement = null;
        CharacterPermissionsDto characterPermissionsDto = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT [charactername] FROM [DanDWhisper].[AccountCharacterPermissions] WHERE [accountid] = ?");
            statement.setInt(1, accountId);

            rs = statement.executeQuery();
            List<CharacterDto> characterList = new ArrayList<>();
            while(rs.next()) {
                String characterNameOutput = rs.getString("charactername");
                characterList.add(new CharacterDto(characterNameOutput));
            }
            characterPermissionsDto = new CharacterPermissionsDto(characterList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if(statement!= null){
                    statement.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return characterPermissionsDto;
    }
}
