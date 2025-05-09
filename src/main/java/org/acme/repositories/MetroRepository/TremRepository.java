package org.acme.repositories.MetroRepository;

import org.acme.entities.Metro.Linha;
import org.acme.entities.Metro.Trem;
import org.acme.entities.Pessoa.Passageiro;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.acme.repositories.PessoaRepository.PassageiroRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TremRepository implements CrudRepository<Trem> {
    public static Logger logger = LogManager.getLogger(TremRepository.class);
    private final LinhaRepository linhaRepository = new LinhaRepository();
    private List<Trem> trens = new ArrayList<>(List.of());

    @Override
    public void adicionar(Trem object) {
        var query = "INSERT INTO TREMACESSI (DELETED, ORIGEM, DESTINO, LINHA_ID, CAPACIDADEMAXIMA, CAPACIDADEATUAL, OPERANDO) VALUES (?,?,?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getOrigem());
            stmt.setString(3, object.getDestino());
            stmt.setInt(4, object.getLinha().getId());
            stmt.setInt(5, object.getCapacidadeMaxima());
            stmt.setInt(6, object.getCapacidadeAtual());
            stmt.setBoolean(7, true);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Trem adicionado com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Trem object) {
        for (Trem t: trens)
            if (t.getId() == id)
                t = object;
    }

    @Override
    public void remover(Trem object) {
        trens.remove(object);
    }

    @Override
    public void remover(int id) {
        trens.removeIf(t -> t.getId() == id);
    }

    @Override
    public void delete(Trem object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE TREMACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Trem removido com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Trem> listarTodos() {
        var trensDb = new ArrayList<Trem>();
        var query = "SELECT * FROM TREMACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var trem = new Trem();
                trem.setId(result.getInt("id"));
                trem.setDeleted(result.getBoolean("deleted"));
                int linhaId = result.getInt("linha_id");
                Linha linha = linhaRepository.buscarPorId(linhaId).orElse(null);
                trem.setLinha(linha);
                trem.setOrigem(result.getString("origem"));
                trem.setDestino(result.getString("destino"));
                trem.setCapacidadeAtual(result.getInt("capacidadeAtual"));
                trem.setCapacidadeMaxima(result.getInt("capacidadeMaxima"));
                trem.setOperando(result.getBoolean("operando"));
                trensDb.add(trem);
            }
            connection.close();
            return trensDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Trem> listar() {

        var trensDb = new ArrayList<Trem>();
        var query = "SELECT * FROM TREMACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var trem = new Trem();
                trem.setId(result.getInt("id"));
                trem.setDeleted(result.getBoolean("deleted"));
                int linhaId = result.getInt("linha_id");
                Linha linha = linhaRepository.buscarPorId(linhaId).orElse(null);
                trem.setLinha(linha);
                trem.setOrigem(result.getString("origem"));
                trem.setDestino(result.getString("destino"));
                trem.setCapacidadeAtual(result.getInt("capacidadeAtual"));
                trem.setCapacidadeMaxima(result.getInt("capacidadeMaxima"));
                trem.setOperando(result.getBoolean("operando"));
                trensDb.add(trem);
            }
            return trensDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Trem> buscarPorId(int id) {
        var query = "SELECT * from TREMACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var trem = new Trem();
                trem.setId(result.getInt("id"));
                trem.setDeleted(result.getBoolean("deleted"));
                Linha linha = new Linha();
                linha.setNome(result.getString("linha_id"));
                trem.setLinha(linha);
                trem.setCapacidadeAtual(result.getInt("capacidadeAtual"));
                trem.setCapacidadeMaxima(result.getInt("capacidadeMaxima"));
                trem.setOperando(result.getBoolean("operando"));
                return Optional.of(trem);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }

}
