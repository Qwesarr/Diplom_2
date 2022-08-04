package precondition;

import java.util.ArrayList;

public class TestData {
    private static final String NAME = "Qwesar";
    private static final String EMAIL = "qwesar@list.ru";
    private static final String PASSWORD = "123456";
    private static final ArrayList<String> INGREDIENTS = new ArrayList<>();

    public TestData(){
        INGREDIENTS.add("61c0c5a71d1f82001bdaaa6d");
        INGREDIENTS.add("61c0c5a71d1f82001bdaaa6f");
        INGREDIENTS.add("61c0c5a71d1f82001bdaaa72");
    }
    public String getName(){
        return NAME;
    }
    public String getEmail(){
        return EMAIL;
    }
    public String getPassword(){
        return PASSWORD;
    }

    public ArrayList<String> getIngredientList(){
        return INGREDIENTS;
    }

    public String getIngredient(int index){
        return INGREDIENTS.get(index);
    }
}
