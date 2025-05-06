package org.acme.resources.Problema;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Pessoa.SearchColaboradorDto;
import org.acme.dtos.Problema.SearchConversaDto;
import org.acme.entities.Pessoa.Colaborador;
import org.acme.entities.Problema.Conversa;
import org.acme.repositories.PessoaRepository.ColaboradorRepository;
import org.acme.repositories.ProblemaRepository.ConversaRepository;
import org.acme.services.ProblemaService.ConversaService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/conversa")
public class ConversaResource {
    public static final int PAGE_SIZE = 3;
    private ConversaRepository conversaRepository = new ConversaRepository();
    private final ConversaService conversaService = new ConversaService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversa> getConversas() { return conversaRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchConversaDto searchConversas(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Conversa> conversas = conversaRepository.listarTodos();

        var filteredConversas = conversas.stream()
                .filter(e -> e.getAssunto().contains(name.orElse("")))
                .filter(e -> e.getMensagem().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getAssunto().compareToIgnoreCase(c1.getAssunto()):
                        (c1, c2) -> c1.getMensagem().compareToIgnoreCase(c2.getMensagem())
                )
                .toList();

        if(filteredConversas.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredConversas.size());

        return start > filteredConversas.size() ?
                null : new SearchConversaDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredConversas.size(),
                filteredConversas.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConversa(@PathParam("id") int id){
        var conversa = conversaRepository.buscarPorId(id);

        return conversa.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addConversa(Conversa conversa){
        if(!conversaService.validateConversa(conversa))
            return Response.status(Response.Status.BAD_REQUEST).build();

        conversaRepository.adicionar(conversa);
        return Response.status(Response.Status.CREATED)
                .entity(conversa)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteConversa(@PathParam("id") int id){
        var conversa = conversaRepository.buscarPorId(id);

        if (conversa == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        conversaRepository.deleteById(id);
        return Response.noContent().build();
    }
}
