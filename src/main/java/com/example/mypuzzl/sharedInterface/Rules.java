package com.example.mypuzzl.sharedInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Rules extends Remote {

    String[] GenerateArrangement(int level, String theme) throws RemoteException;

    int GetGameTime(int level) throws RemoteException;

    boolean CheckCardPair(GameConfiguration game, int position1, int position2) throws RemoteException;

    String test() throws RemoteException;

}
