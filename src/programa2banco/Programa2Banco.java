/*
NOME: JAMES RAFAEL EHLERT REINARD
TURMA: TADS 3N
TRABALHO: AGENDA DE CONTATOS
*/
package programa2banco;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Programa2Banco {

    public static int Menu() {
        Scanner leia = new Scanner(System.in);
        int op = 0;
        do {
            System.out.println("---------------------------------------------------");
            System.out.println("              Escolha uma opção:");
            System.out.println(" [1] - para adicionar um novo contato");
            System.out.println(" [2] - para listar os contatos");
            System.out.println(" [3] - para Alterar os dados de um contato");
            System.out.println(" [4] - para excluir um contato ");
            System.out.println("---------------------------------------------------");
            System.out.print("Opção: ");

            op = leia.nextInt();

            while (op != 1 && op != 2 && op != 3 && op != 4) {
                System.out.println("Digite uma opção valida!");
                System.out.print("Opção: ");
                op = leia.nextInt();
            }
        } while ((op < 0) || (op > 4));
        return op;
    }

    public static void main(String[] args) {
        int op = 0;
        String nome = "";
        String telefone = "";
        String varString ;
        int codigo = 0;
        Scanner leia = new Scanner(System.in);
        Connection conectar = null;  // Conexao
        PreparedStatement ps = null; // Operação
        ResultSet rs = null;         // Dados
        String ip = "jdbc:postgresql://localhost:5432/banco";
        String username = "postgres";
        String password = "postgre";
        boolean sair = false; // variavel auxiliar para encerrar o programa

        //Conectar
        try {
            Class.forName("org.postgresql.Driver");
            conectar = DriverManager.getConnection(ip, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("ERRO Banco de Dados");
            System.exit(-1); //Fecha o programa
        }

        do {
            op = Menu();
            while (op != 1 && op != 2 && op != 3 && op != 4) {
                System.out.println("Digite uma opção valida!");
                System.out.print("Opção: ");
                op = leia.nextInt();
            }
            //adicionar dados
            if (op == 1) {
                try {
                    System.out.println("Digite o nome: ");
                    nome = leia.next();
                    System.out.println("Digite o telefone: ");
                    telefone = leia.next();
                    String incluir = "insert into tabela (nome, telefone) values ('" + nome + " ','" + telefone + "')";
                    conectar.createStatement().execute(incluir);
                } catch (SQLException ex) {
                    System.out.println("Erro incluir");
                }
            }
            //listar dados
            if (op == 2) {

                String query = "select * from tabela order by codigo";
                try {
                    ps = conectar.prepareStatement(query,
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        System.out.println("ID  |  Nome  |  Telefone");
                        System.out.println(rs.getInt("codigo") + "---- " + rs.getString("nome") + "-------" + rs.getString("telefone"));
                    }
                } catch (SQLException ex) {
                    System.out.println("Erro Banco de Dados - LISTAR");
                }
            }
            // Alterar dados
            if (op == 3) {
                try {
                    System.out.println("Digite o codigo do contato que deseja alterar:");
                    codigo = leia.nextInt();
                    System.out.println("Oque voce deseja alterar?");
                    System.out.println(" [1] - para alterar o nome");
                    System.out.println(" [2] - para alterar o telefone");
                    System.out.println(" [3] -para alterar a cidade ");
                    op = leia.nextInt();

                    if (op == 1) {
                        System.out.println("Dgite o novo nome: ");
                        nome = leia.next();
                        String alterar = "update tabela set nome='" + nome + "' where codigo=" + codigo;
                        conectar.createStatement().execute(alterar);
                    }

                    if (op == 2) {
                        System.out.println("Dgite o novo telefone: ");
                        telefone = leia.next();
                        String alterar = "update tabela set telefone='" + telefone + "' where codigo=" + codigo;
                        conectar.createStatement().execute(alterar);
                    }
                } catch (SQLException ex) {
                    System.out.println("Erro Alterar");
                }
            }
            // Excluir dados
            if (op == 4) {
                try {
                    System.out.println("Digite o codigo do contato que voce deseja excluir:");
                    codigo = leia.nextInt();
                    String excluir = "delete from tabela where codigo=" + codigo;
                    conectar.createStatement().execute(excluir);
                } catch (SQLException ex) {
                    System.out.println("Erro excluir");
                }
            }
            System.out.print("\nDeseja voltar ao menu? [S/N]: ");
            varString = leia.next();
            // loop para evitar que o usuario digite uma string diferente do permitido, evitando tambem que o programa de erro
            while (!varString.equals("s") && !varString.equals("n")) {
                System.out.println("\nDigite 'S' para voltar ao menu de opções ou 'N' para encerrar o programa. (Obs: utilize letras minusculas!)");
                System.out.print("Deseja voltar ao menu? [S/N]: ");
                varString = leia.next();
            }
            if (varString.equals("s")) {
                sair = false;
            } else {
                sair = true;
            }
        } while (sair != true);
        System.out.println("Programa encerrado!");
    }
}

    

