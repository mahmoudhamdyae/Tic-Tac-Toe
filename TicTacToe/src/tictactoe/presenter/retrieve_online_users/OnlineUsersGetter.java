/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.presenter.retrieve_online_users;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import tictactoe.model.User;
import tictactoe.presenter.auth_server.NetworkResponse;
import tictactoe.utils.Constants;
import tictactoe.utils.Validation;

/**
 *
 * @author Mohammed
 */

public class OnlineUsersGetter implements Runnable {
    private final DataInputStream dataInputStream;
    private final User user;
    private final ServerResponse callback;
    private final PrintStream printStream;
    private final String type;
    private ArrayList<String> onlineUsers;
    boolean state =true;

    public OnlineUsersGetter( boolean state,DataInputStream dataInputStream, User user,
                           String type, PrintStream printStream, ServerResponse callback , ArrayList<String> onlineUsers) {

        this.dataInputStream = dataInputStream;
        this.user = user;
        this.callback = callback;
        this.printStream = printStream;
        this.type = type;
        this.state=state;
        this.onlineUsers = onlineUsers;
        
        
    }

 
    @Override
    public void run() {
        try {
            while (state) {
            printStream.println(type);
            printStream.println(user.getUserName());
                if (dataInputStream.readLine().equals(Constants.SERVER_STOP)) {
                        // Server Stopped
                        System.out.println("Server Stopped");
                    } else {
                        // Server is Running
                        System.out.println("Server is Running");
                        String remoteUserName = dataInputStream.readLine();
                        if (remoteUserName.equals(user.getUserName())) {
                            // Server returned to this user
                            
                            String message = dataInputStream.readLine();
                            if(message.equals(type)) {
                                int noOfUsers = Integer.parseInt(dataInputStream.readLine());
                                onlineUsers = new ArrayList<String>();
                                for (int i = 0; i < noOfUsers; i++) {
      
                                    String userName = dataInputStream.readLine();
                                    onlineUsers.add(dataInputStream.readLine());
                                    System.out.println("userName: " + userName);
                                }
                                if(onlineUsers.equals(null)){
                                callback.onError("Empty list of online users");
                                }else{
                                
                                callback.onSuccess();
                                }
                            }
                        }
                    }
                
            }
        } catch (IOException ex) {
            callback.onError(ex.getMessage());
        }
    }
    
}





