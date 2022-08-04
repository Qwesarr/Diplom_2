package precondition;

import dto.UserDto;

import java.util.ArrayList;

public class TestData {
    private static final String name = "Qwesar";
    private static final String email = "qwesar@list.ru";
    private static final String password = "123456";
    private static final ArrayList<String> ingredients = new ArrayList<>();

    public TestData(){
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add("61c0c5a71d1f82001bdaaa72");
    }
    public String getName(){
        return name;
    };
    public String getEmail(){
        return email;
    };
    public String getPassword(){
        return password;
    };

    public ArrayList<String> getIngredientList(){
        return ingredients;
    };


    public String getIngredient(int index){
        return ingredients.get(index);
    };

}
