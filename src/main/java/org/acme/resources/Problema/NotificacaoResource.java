package org.acme.resources.Problema;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Problema.NotificacaoCriadaDto;
import org.acme.dtos.Problema.SearchNotificacaoDto;
import org.acme.entities.Problema.Notificacao;
import org.acme.repositories.ProblemaRepository.NotificacaoRepository;
import org.acme.services.ProblemaService.NotificacaoService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/notificacao")
public class NotificacaoResource {
    public static final int PAGE_SIZE = 3;

    private NotificacaoRepository notificacaoRepository = new NotificacaoRepository();
    private final NotificacaoService notificacaoService = new NotificacaoService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notificacao> getNotificacoes() {return notificacaoRepository.listarTodos();}

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchNotificacaoDto searchNotificacoes(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Notificacao> notificacoes = notificacaoRepository.listarTodos();

        var filteredNotificacoes = notificacoes.stream()
                .filter(e -> e.getTitulo().contains(name.orElse("")))
                .filter(e -> e.getConteudo().contains(text.orElse("")))
                .filter(e -> e.getLinha().contains(text.orElse("")))
                .filter(e -> e.getTipoOcorrencia().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getTitulo().compareToIgnoreCase(c1.getTitulo()):
                        (c1, c2) -> c1.getConteudo().compareToIgnoreCase(c2.getConteudo())
                )
                .toList();

        if(filteredNotificacoes.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredNotificacoes.size());

        return start > filteredNotificacoes.size() ?
                null : new SearchNotificacaoDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredNotificacoes.size(),
                filteredNotificacoes.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotificacao(@PathParam("id") int id){
        var notificacao = notificacaoRepository.buscarPorId(id);

        return notificacao.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNotificacao(Notificacao notificacao){
        if(!notificacaoService.validateNotificacao(notificacao))
            return Response.status(Response.Status.BAD_REQUEST).build();

        notificacaoRepository.adicionar(notificacao);
        return Response.status(Response.Status.CREATED)
                .entity(notificacao)
                .build();
    }

    @POST
    @Path("/criar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarNotificacao(NotificacaoCriadaDto dto) {
        if (dto.getTitulo() == null || dto.getConteudo().trim().isEmpty() || dto.getLinha().trim().isEmpty() || dto.getTipoOcorrencia().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Os campos não podem estar vazios ou conter apenas espaços.")
                    .build();
        }


        Notificacao notificacao = new Notificacao();
        notificacao.setTitulo(dto.getTitulo());
        notificacao.setConteudo(dto.getConteudo());
        notificacao.setLinha(dto.getLinha());
        notificacao.setTipoOcorrencia(dto.getTipoOcorrencia());

        notificacaoRepository.adicionar(notificacao);

        return Response.status(Response.Status.CREATED)
                .entity(notificacao)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarNotificacao(@PathParam("id") int id, NotificacaoCriadaDto dto){
        Optional<Notificacao> notificacaoOptional = notificacaoRepository.buscarPorId(id);

        if(notificacaoOptional.isEmpty()) {
            return Response.status(RestResponse.Status.NOT_FOUND).entity("Notificação não encontrada").build();
        }

        Notificacao notificacao = notificacaoOptional.get();

        if (dto.getTitulo() != null && !dto.getTitulo().isEmpty()) {
            notificacao.setTitulo(dto.getTitulo());
        }

        if (dto.getConteudo() != null && !dto.getConteudo().isEmpty()) {
            notificacao.setConteudo(dto.getConteudo());
        }

        if (dto.getLinha() != null && !dto.getLinha().isEmpty()) {
            notificacao.setLinha(dto.getLinha());
        }

        if (dto.getTipoOcorrencia() != null && !dto.getTipoOcorrencia().isEmpty()) {
            notificacao.setTipoOcorrencia(dto.getTipoOcorrencia());
        }

        notificacaoRepository.atualizar(id, notificacao);

        return Response.ok(notificacao).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNotificacao(@PathParam("id") int id){
        var notificacao = notificacaoRepository.buscarPorId(id);

        if (notificacao == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        notificacaoRepository.deleteById(id);
        return Response.noContent().build();
    }
}
