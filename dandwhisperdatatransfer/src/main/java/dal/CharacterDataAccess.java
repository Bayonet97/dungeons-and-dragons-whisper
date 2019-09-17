package dal;

import dtos.CharacterDto;
import dtos.CharacterPermissionsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterDataAccess extends DataAccess{
    public List<CharacterDto> getAllCharacters() {

        List<CharacterDto> characters = new ArrayList<>();
        try(Connection connection = getConnection()){
            try(PreparedStatement  statement = connection.prepareStatement("SELECT [charactername] FROM [DanDWhisper].[Character]")) {
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()) {
                        String characterNameOutput = rs.getString("charactername");
                        characters.add(new CharacterDto(characterNameOutput));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return characters;
    }
}
