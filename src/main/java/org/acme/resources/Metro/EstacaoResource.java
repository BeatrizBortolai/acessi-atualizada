package org.acme.resources.Metro;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Metro.SearchEstacaoDto;
import org.acme.dtos.Pessoa.SearchColaboradorDto;
import org.acme.entities.Metro.Estacao;
import org.acme.entities.Pessoa.Colaborador;
import org.acme.repositories.MetroRepository.EstacaoRepository;
import org.acme.repositories.PessoaRepository.ColaboradorRepository;
import org.acme.services.MetroService.EstacaoService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/estacao")
public class EstacaoResource {
    public static final int PAGE_SIZE = 3;
    private EstacaoRepository estacaoRepository = new EstacaoRepository();
    private final EstacaoService estacaoService = new EstacaoService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Estacao> getEstacoes() { return estacaoRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchEstacaoDto searchEstacoes(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Estacao> estacoes = estacaoRepository.listarTodos();

        var filteredEstacoes = estacoes.stream()
                .filter(e -> e.getNome().contains(name.orElse("")))
                .filter(e -> e.getLocalizacao().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getNome().compareToIgnoreCase(c1.getNome()):
                        (c1, c2) -> c1.getLocalizacao().compareToIgnoreCase(c2.getLocalizacao())
                )
                .toList();

        if(filteredEstacoes.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredEstacoes.size());

        return start > filteredEstacoes.size() ?
                null : new SearchEstacaoDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredEstacoes.size(),
                filteredEstacoes.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstacao(@PathParam("id") int id){
        var estacao = estacaoRepository.buscarPorId(id);

        return estacao.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEstacao(Estacao estacao){
        if(!estacaoService.validateEstacao(estacao))
            return Response.status(Response.Status.BAD_REQUEST).build();

        estacaoRepository.adicionar(estacao);
        return Response.status(Response.Status.CREATED)
                .entity(estacao)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEstacao(@PathParam("id") int id){
        var estacao = estacaoRepository.buscarPorId(id);

        if (estacao == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        estacaoRepository.deleteById(id);
        return Response.noContent().build();
    }
}
