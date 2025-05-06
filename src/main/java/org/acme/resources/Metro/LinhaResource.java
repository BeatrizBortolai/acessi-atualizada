package org.acme.resources.Metro;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Metro.SearchLinhaDto;
import org.acme.dtos.Pessoa.SearchColaboradorDto;
import org.acme.entities.Metro.Estacao;
import org.acme.entities.Metro.Linha;
import org.acme.entities.Pessoa.Colaborador;
import org.acme.repositories.MetroRepository.LinhaRepository;
import org.acme.repositories.PessoaRepository.ColaboradorRepository;
import org.acme.services.MetroService.LinhaService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/linha")
public class LinhaResource {
    public static final int PAGE_SIZE = 3;
    private LinhaRepository linhaRepository = new LinhaRepository();
    private final LinhaService linhaService = new LinhaService();

    private String listaDeNomes(List<Estacao> estacoes) {
        return estacoes.stream()
                .map(Estacao::getNome)
                .collect(Collectors.joining(", "));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Linha> getLinhas() { return linhaRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchLinhaDto searchLinhas(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Linha> linhas = linhaRepository.listarTodos();

        var filteredLinhas = linhas.stream()
                .filter(e -> e.getNome().contains(name.orElse("")))
                .filter(e -> (e.getEstacoes().contains(text.orElse(""))))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> listaDeNomes(c2.getEstacoes())
                                .compareToIgnoreCase(listaDeNomes(c1.getEstacoes())) :
                        (c1, c2) -> listaDeNomes(c1.getEstacoes())
                                .compareToIgnoreCase(listaDeNomes(c2.getEstacoes()))
                )
                .toList();

        if(filteredLinhas.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredLinhas.size());

        return start > filteredLinhas.size() ?
                null : new SearchLinhaDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredLinhas.size(),
                filteredLinhas.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLinha(@PathParam("id") int id){
        var linha = linhaRepository.buscarPorId(id);

        return linha.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLinha(Linha linha){
        if(!linhaService.validateLinha(linha))
            return Response.status(Response.Status.BAD_REQUEST).build();

        linhaRepository.adicionar(linha);
        return Response.status(Response.Status.CREATED)
                .entity(linha)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLinha(@PathParam("id") int id){
        var linha = linhaRepository.buscarPorId(id);

        if (linha == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        linhaRepository.deleteById(id);
        return Response.noContent().build();
    }
}
