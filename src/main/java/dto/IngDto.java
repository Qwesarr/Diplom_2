package dto;

import java.util.ArrayList;

public class IngDto {
    private ArrayList<String> ingredients = new ArrayList<>();

    public IngDto() {
    }

    public IngDto(ArrayList<String> ingredients) {                                                                      //Конструктор полный
        this.ingredients = ingredients;
    }

    public IngDto(String ingredient) {                                                                                  //Конструктор полный
        this.ingredients.add(ingredient);
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void addIngredients(String ingredient) {
        ingredients.add(ingredient);
    }

    public void addIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

}
