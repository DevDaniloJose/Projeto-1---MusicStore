package client;

import java.util.HashMap;

public class Hehe {

    public static void main(String[] args) {
        String[] animals = {"gato", "cachorro", "capivara", "leao", "capivara"};

        HashMap<String, String> map = new HashMap();


        for (int i = 0; i < animals.length; i++) {
            String currentAnimal = animals[i];

            if  (map.containsKey(currentAnimal)) {
                System.out.printf("The repeated animal is: {%s}", currentAnimal) ;
            }

            map.put(currentAnimal, String.valueOf(i));


        }

    }

}
