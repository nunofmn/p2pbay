package client.menu;

import core.model.Item;
import core.model.NetworkContent;
import core.model.UserProfile;
import core.network.PeerConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by EngSoftware on 05-11-2014.
 */
public class LoginMenu {

    public LoginMenu(){

    }

    public void display(PeerConnection peercore) throws NumberFormatException, Exception{

        String user, escolha, pass;
        NetworkContent userProfile;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Bem vindo a' sua loja P2P  - Leiloes");


        while(true) {
            System.out.println("Pressione a tecla pretendida:");
            System.out.println("1 - Login");
            System.out.println("2 - Criar utilizador");
            System.out.println("3 - Sair");
            escolha = br.readLine();
            if (escolha.equals("1")) {
                System.out.println("Por favor digite o seu username");
                user = br.readLine();

                userProfile = peercore.get(user);
                if (userProfile != null && userProfile.contentType().equals("User")) {
                    System.out.println("digite a sua password");
                    pass = br.readLine();
                    if (((UserProfile) userProfile).login(pass)) {
                        System.out.println("Login  - Sucesso!");
                        System.out.println("Login  - Mostrar mais menu agora!");
                        ActionMenu action = new ActionMenu();
                        action.display(peercore, user, ((UserProfile)userProfile));
                    } else {
                        System.out.println("*Login  - password errada !");
                    }
                } else {
                    System.out.println("utilizador não encontrado!");
                    continue;
                }
            } else if (escolha.equals("2")) {
                System.out.println("Formulario de registo novo utilizador:");
                while (true) {
                    System.out.println("Digite o seu username pretendido:");
                    user = br.readLine();
                    userProfile = peercore.get(user);
                    if (userProfile != null) {
                        System.out.println("Nome de utilizador ja em uso, escolha um diferente:");
                        continue;
                    }

                    System.out.println("Digite a sua password:");
                    pass = br.readLine();
                    if(user.isEmpty() || pass.isEmpty()){
                        System.out.println("Username e password não podem estar vazios!");
                        continue;
                    }

                    peercore.store(user, new UserProfile(pass));
                    break;
                }
            } else if (escolha.equals("3")){
                System.exit(0);
            }

            System.out.println("P2PBay - Coming Soon");
        }
    }

}
