package org.acme.resources.Pessoa;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Pessoa.SearchUsuarioDto;
import org.acme.dtos.Pessoa.UsuarioAtualizacaoDto;
import org.acme.dtos.Pessoa.UsuarioCadastroDto;
import org.acme.dtos.Pessoa.UsuarioLoginDto;
import org.acme.entities.JwtUtil;
import org.acme.entities.Pessoa.Usuario;
import org.acme.repositories.PessoaRepository.UsuarioRepository;
import org.acme.services.PessoaService.UsuarioService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Path("/usuario")
public class UsuarioResource {
    public static final int PAGE_SIZE = 3;

    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final UsuarioService usuarioService = new UsuarioService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getUsuarios() {return usuarioRepository.listarTodos();}

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchUsuarioDto searchUsuarios(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Usuario> usuarios = usuarioRepository.listarTodos();

        var filteredUsuarios = usuarios.stream()
                .filter(e -> e.getEmail().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getNome().compareToIgnoreCase(c1.getNome()):
                        (c1, c2) -> c1.getEmail().compareToIgnoreCase(c2.getEmail())
                        )
                .toList();

        if(filteredUsuarios.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredUsuarios.size());

        return start > filteredUsuarios.size() ?
            null : new SearchUsuarioDto(page,
            direction.orElse("asc"),
            PAGE_SIZE,
            filteredUsuarios.size(),
            filteredUsuarios.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id){
        var usuario = usuarioRepository.buscarPorId(id);

        return usuario.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUsuario(Usuario usuario){
        if(!usuarioService.validateUsuario(usuario))
            return Response.status(Response.Status.BAD_REQUEST).build();

        usuarioRepository.adicionar(usuario);
        return Response.status(Response.Status.CREATED)
                .entity(usuario)
                .build();
    }

    @POST
    @Path("/cadastro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuario(UsuarioCadastroDto dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nome não pode estar vazio ou conter apenas espaços.")
                    .build();
        }

        if (!dto.getSenha().equals(dto.getConfirmaSenha())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("As senhas não coincidem.")
                    .build();
        }

        if (usuarioRepository.buscarPorEmail(dto.getEmail()).isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("E-mail já cadastrado.")
                    .build();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        usuarioRepository.adicionar(usuario);

        return Response.status(Response.Status.CREATED)
                .entity(usuario)
                .build();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UsuarioLoginDto dto) {
        if (dto.getEmail() == null || dto.getSenha() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email e senha são obrigatórios.").build();
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(dto.getEmail()).orElse(null);

        if (usuario == null || !usuario.getSenha().trim().equals(dto.getSenha().trim())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas.").build();
        }

        if (usuario.isDeleted()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Conta desativada. Contate o suporte.")
                    .build();
        }

        // Simples resposta (poderia gerar token JWT aqui)
        String token = JwtUtil.gerarToken(String.valueOf(usuario.getId()));
        return Response.ok(Collections.singletonMap("token", token)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarUsuario(@HeaderParam("Authorization") String authHeader,
                                     @PathParam("id") int id,
                                     UsuarioAtualizacaoDto dto) {
        try {
            // 1. Extrair e validar token JWT
            String idDoTokenStr = JwtUtil.validarToken(authHeader);
            int idDoToken = Integer.parseInt(idDoTokenStr);

            // 2. Verificar se o ID no token bate com o ID da URL
            if (idDoToken != id) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Você não tem permissão para atualizar este usuário.")
                        .build();
            }

            // 3. Buscar usuário e aplicar atualizações
            Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(id);
            if (usuarioOptional.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuário não encontrado")
                        .build();
            }

            Usuario usuario = usuarioOptional.get();

            if (dto.getNome() != null && !dto.getNome().isEmpty()) {
                usuario.setNome(dto.getNome());
            }

            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                usuario.setEmail(dto.getEmail());
            }

            if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
                if (dto.getSenha().equals(dto.getConfirmaSenha())) {
                    usuario.setSenha(dto.getSenha()); // de preferência, aplique hash aqui
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Senhas não coincidem")
                            .build();
                }
            }

            usuarioRepository.atualizar(id, usuario);
            return Response.ok(usuario).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token inválido ou expirado")
                    .build();
        }
    }


    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") int id){
        var usuario = usuarioRepository.buscarPorId(id);

        if (usuario == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        usuarioRepository.deleteById(id);
        return Response.noContent().build();
    }
}
