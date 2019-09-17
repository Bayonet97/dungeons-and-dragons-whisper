package messaging.responses;

import java.util.List;

public class GetAllCharactersResponse extends ServerResponse {
    private List<String> characters;

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }
}
