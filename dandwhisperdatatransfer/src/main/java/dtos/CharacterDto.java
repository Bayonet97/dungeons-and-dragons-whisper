package dtos;

public class CharacterDto {
    private String name;


    public CharacterDto(String name){
        this.name = name;
    }
    public String getName(){ return name;}


    public String toString(){
        return name;
    }
}
