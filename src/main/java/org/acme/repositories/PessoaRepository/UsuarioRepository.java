package org.acme.repositories.PessoaRepository;

import com.google.gson.GsonBuilder;
import org.acme.entities.Pessoa.Usuario;
import org.acme.extensions.LocalDateTimeGsonAdapter;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository implements CrudRepository<Usuario> {
    public static Logger logger = LogManager.getLogger(UsuarioRepository.class);

    List<Usuario> usuarios = new ArrayList<>(List.of());

    @Override
    public void adicionar(Usuario object) {
        var query = "INSERT INTO USUARIOACESSI (DELETED, NOME, EMAIL, SENHA) VALUES (?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getNome());
            stmt.setString(3, object.getEmail());
            stmt.setString(4, object.getSenha());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Usuário adicionado com sucesso!");
        }
        catch(SQLException e){
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Usuario usuario) {
        var query = "UPDATE USUARIOACESSI SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (var conn = DatabaseConfig.getConnection()) {
            var stmt = conn.prepareStatement(query);
            // Definindo os parâmetros para o PreparedStatement
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, id); // Aqui passamos o id para atualizar o usuário específico

            // Executando o update e verificando quantas linhas foram afetadas
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(Usuario object) {
        usuarios.remove(object);
    }

    @Override
    public void remover(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }

    @Override
    public void delete(Usuario object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        //var query = "DELETE FROM COLECAO WHERE ID = ?";
        var query = "UPDATE USUARIOACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Usuário removido com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        var usuariosDb = new ArrayList<Usuario>();
        var query = "SELECT * FROM USUARIOACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nome"));
                usuario.setEmail(result.getString("email"));
                usuario.setSenha(result.getString("senha"));
                usuariosDb.add(usuario);
            }
            return usuariosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {

        var usuariosDb = new ArrayList<Usuario>();
        var query = "SELECT * FROM USUARIOACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nome"));
                usuario.setEmail(result.getString("email"));
                usuario.setSenha(result.getString("senha"));
                usuariosDb.add(usuario);
            }
            return usuariosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        var query = "SELECT * from USUARIOACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nome"));
                usuario.setEmail(result.getString("email"));
                usuario.setSenha(result.getString("senha"));
                return Optional.of(usuario);
            }
        }
        catch(SQLException e){
            logger.error(e);
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        var query = "SELECT * from USUARIOACESSI WHERE EMAIL = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            var result = stmt.executeQuery();
            if(result.next()){
                var usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nome"));
                usuario.setEmail(result.getString("email"));
                usuario.setSenha(result.getString("senha"));
                return Optional.of(usuario);
            }
        }
        catch(SQLException e){
            logger.error(e);
        }
        return Optional.empty();
    }

    public void exportar() {
        // fazer um teste antes de exportação de uma string simples
        var guid = UUID.randomUUID().toString();
        var conteudo = "Este texto será o conteudo que será exportado para o meu arquivo";
        var caminho = "./reports/"+
                //LocalDateTime.now() +
                guid +
                "_usuarios.txt";
        try{
            var file = new File(caminho);
            if(!file.exists())
                file.createNewFile();
            var writer = new FileWriter(file);
            writer.write(conteudo);
            writer.close();
            System.out.println("Arquivo exportado com sucesso");
        }
        catch (IOException e){
            System.out.println("Erro ao exportar o arquivo");
            throw new RuntimeException(e);
        }
    }

    public void importar(String c){
        String caminho = "./reports/" + c;
        try{
            var file = new File(caminho);
            var reader = new FileReader(file);
            var conteudo = "";
            while (reader.ready())
                conteudo += (char) reader.read();
            System.out.println(conteudo);
        }
        catch (IOException e){
            System.out.println("Erro ao importar o arquivo");
        }
    }

    public void exportarArquivoGrande()
    {
        var guid = UUID.randomUUID().toString();
        var caminho = "./reports/" + guid + "_usuarios.txt";
        try{
            var newFile = new File(caminho);
            if(!newFile.exists()){
                var writer = new BufferedWriter(
                        new FileWriter(newFile));
                var conteudoGrande = new StringBuilder();
                for(int i = 0; i<10000000; i++)
                    conteudoGrande.append("Linha " + i + "\n");

                writer.write(conteudoGrande.toString());

                writer.close();
                System.out.println("Arquivo exportado com sucesso");
            }
        }
        catch (IOException e){
            System.out.println("Erro ao exportar o arquivo");
        }
    }

    public void exportarParaJson(){
        var guid = UUID.randomUUID().toString();
        var caminho = "./reports/"+
                //LocalDateTime.now() +
                guid +
                "_usuarios.json";
        try{
            var gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter())
                    .setPrettyPrinting()
                    .create();
            var json = gson.toJson(listar());
            var file = new File(caminho);
            var fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        }
        catch (Exception e){
            logger.error("Erro ao exportar arquivo", e);
            System.out.println("Erro ao exportar o arquivo");
        }
    }

    public void importarDeJson(String arquivo){
        try{
            var gson = new  GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter())
                    .create();
            var caminho = "./reports/" + arquivo;
            var reader = new FileReader(caminho);
            var usuariosDoJson = gson.fromJson(reader, Usuario[].class);
            for(var u: usuariosDoJson)
                adicionar(u);
        }
        catch (Exception e){
            logger.error("Erro ao importar arquivo", e);
            System.out.println("Erro ao importar o arquivo");
        }
    }
}
