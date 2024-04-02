package com.example.mypuzzl.server;

import com.example.mypuzzl.sharedInterface.GameConfiguration;
import com.example.mypuzzl.sharedInterface.Rules;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.rmi.RemoteException;

public class RulesImpl implements Rules {

    @Override
    public String[] GenerateArrangement(int level, String theme) throws RemoteException {

        System.out.println("GenerateArrangement");

        JSONParser parser = new JSONParser();
        int emount = (level == 1) ? 4 : ((level == 2) ? 6 : 8);

        try (FileReader reader = new FileReader("C:\\Users\\darya\\Desktop\\MyPuzzl\\src\\main\\java\\com\\example\\mypuzzl\\server\\arrangements.json")) {

            JSONObject rootJson = (JSONObject) parser.parse(reader);
            JSONArray mass = (JSONArray) rootJson.get((level == 1) ? "first" : ((level == 2) ? "second" : "third"));
            int variant = (int) (Math.random() * mass.size());

            JSONArray numArrangement = (JSONArray) mass.get(variant);

//			System.out.println(numArrangement.size());

            String[] arrangement = new String[emount];

            for (int i = 0; i < numArrangement.size(); i++) {
                String path = String.format("%s-%d.jpg", theme, numArrangement.get(i));
                System.out.println("path = " + path);
                arrangement[i] = path;
            }


            return (arrangement);

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

        return null;
    }

    @Override
    public int GetGameTime(int level) throws RemoteException {
        return (level == 1) ? 40 : ((level == 2) ? 35 : 30);
    }

    @Override
    public boolean CheckCardPair(GameConfiguration game, int positionF, int positionS) throws RemoteException {

        String[] arrangement = game.getArrangement();
        System.out.println("CheckCardPair: " + arrangement[positionF].equals(arrangement[positionS]));

        return arrangement[positionF].equals(arrangement[positionS]);
    }

    @Override
    public String test() throws RemoteException {
        return "remote object has been created";
    }


}
