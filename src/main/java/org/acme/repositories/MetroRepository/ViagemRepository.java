package org.acme.repositories.MetroRepository;

import org.acme.entities.Metro.Estacao;
import org.acme.entities.Metro.Viagem;
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

public class ViagemRepository implements CrudRepository<Viagem> {
    public static Logger logger = LogManager.getLogger(ViagemRepository.class);
    private final PassageiroRepository passageiroRepository = new PassageiroRepository();
    private final EstacaoRepository estacaoRepository = new EstacaoRepository();


    private List<Viagem> viagens = new ArrayList<>(List.of());

    @Override
    public void adicionar(Viagem object) {
        var query = "INSERT INTO VIAGEMACESSI (DELETED, DURACAO, PASSAGEIRO_ID, ESTACAOORIGEM_ID, ESTACAODESTINO_ID) VALUES (?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setInt(2, object.getDuracao());
            stmt.setInt(3, object.getPassageiro().getId());
            stmt.setInt(4, object.getEstacaoOrigem().getId());
            stmt.setInt(5, object.getEstacaoDestino().getId());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Viagem adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Viagem object) {
        for (Viagem v: viagens)
            if (v.getId() == id)
                v = object;
    }

    @Override
    public void remover(Viagem object) {
        viagens.remove(object);
    }

    @Override
    public void remover(int id) {
        viagens.removeIf(v -> v.getId() == id);
    }

    @Override
    public void delete(Viagem object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE VIAGEMACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Viagem removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    public List<Viagem> listarTodos() {
        var viagensDb = new ArrayList<Viagem>();
        var query = "SELECT * FROM VIAGEMACESSI";

        try (var connection = DatabaseConfig.getConnection()) {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();

            while (result.next()) {
                var viagem = new Viagem();
                viagem.setId(result.getInt("id"));
                viagem.setDeleted(result.getBoolean("deleted"));
                viagem.setDuracao(result.getInt("duracao"));

                // Pegando os IDs das tabelas relacionadas
                int passageiroId = result.getInt("passageiro_id");
                int estacaoOrigemId = result.getInt("estacaoorigem_id");
                int estacaoDestinoId = result.getInt("estacaodestino_id");

                // Buscar nos repositórios (você precisa criar esses métodos)
                Passageiro passageiro = passageiroRepository.buscarPorId(passageiroId).orElse(null);
                Estacao estacaoOrigem = estacaoRepository.buscarPorId(estacaoOrigemId).orElse(null);
                Estacao estacaoDestino = estacaoRepository.buscarPorId(estacaoDestinoId).orElse(null);

                // Setando nos objetos
                viagem.setPassageiro(passageiro);
                viagem.setEstacaoOrigem(estacaoOrigem);
                viagem.setEstacaoDestino(estacaoDestino);

                viagensDb.add(viagem);
            }

            return viagensDb;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }

        return null;
    }


    @Override
    public List<Viagem> listar() {
        var viagensDb = new ArrayList<Viagem>();
        var query = "SELECT * FROM VIAGEMACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var viagem = new Viagem();
                viagem.setId(result.getInt("id"));
                viagem.setDeleted(result.getBoolean("deleted"));
                viagem.setDuracao(result.getInt("duracao"));
                Passageiro passageiro = new Passageiro();
                passageiro.setId(result.getInt("passageiro_id"));
                viagem.setPassageiro(passageiro);
                Estacao estacaoOrigem = new Estacao();
                estacaoOrigem.setId(result.getInt("estacaoorigem_id"));
                viagem.setEstacaoOrigem(estacaoOrigem);
                Estacao estacaoDestino = new Estacao();
                estacaoDestino.setId(result.getInt("estacaodestino_id"));
                viagem.setEstacaoDestino(estacaoDestino);

                viagensDb.add(viagem);
            }
            return viagensDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Viagem> buscarPorId(int id) {
        var query = "SELECT * from VIAGEMACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var viagem = new Viagem();
                viagem.setId(result.getInt("id"));
                viagem.setDeleted(result.getBoolean("deleted"));
                viagem.setDuracao(result.getInt("duracao"));

                // Pegando os IDs das tabelas relacionadas
                int passageiroId = result.getInt("passageiro_id");
                int estacaoOrigemId = result.getInt("estacaoorigem_id");
                int estacaoDestinoId = result.getInt("estacaodestino_id");

                // Buscar nos repositórios (você precisa criar esses métodos)
                Passageiro passageiro = passageiroRepository.buscarPorId(passageiroId).orElse(null);
                Estacao estacaoOrigem = estacaoRepository.buscarPorId(estacaoOrigemId).orElse(null);
                Estacao estacaoDestino = estacaoRepository.buscarPorId(estacaoDestinoId).orElse(null);

                // Setando nos objetos
                viagem.setPassageiro(passageiro);
                viagem.setEstacaoOrigem(estacaoOrigem);
                viagem.setEstacaoDestino(estacaoDestino);

                return Optional.of(viagem);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
