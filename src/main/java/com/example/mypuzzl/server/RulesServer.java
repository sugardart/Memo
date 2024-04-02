package com.example.mypuzzl.server;

import com.example.mypuzzl.sharedInterface.Rules;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RulesServer {

    public static void main(String[] args) {
        try {

            RulesImpl obj = new RulesImpl();
            Rules stub = (Rules) UnicastRemoteObject.exportObject(obj, 0);

            // в лекции было getRegistry (rmi реестр дб запущен до сервера) - для тестирования используем createRegistry(запуск реестра)
            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("gameRules", stub);

            System.out.println("Server started");
//			System.out.print("\n"+stub.getInfo());

//			JSONParser parser = new JSONParser();
//			
//			try (FileReader reader = new FileReader("src/server/arrangements.json")){
//				
//				JSONObject rootJson = (JSONObject)parser.parse(reader);
//				}

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
        }

    }

}
