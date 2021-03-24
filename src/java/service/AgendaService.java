/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.StringReader;
import java.net.URI;
import java.util.List;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import persistencia.Entrada;
import persistencia.JPAEntradaDAO;

/**
 *
 * @author viter
 */
@Path("agenda")
public class AgendaService {
    
    private final JsonBuilderFactory factory;
    
    @Context
    UriInfo uriInfo;

    public AgendaService() {
        factory = Json.createBuilderFactory(null);
    }

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public void create(@FormParam("nome") String nome, @FormParam("sobrenome") String snome, @FormParam("mail") String mail, @FormParam("zap") String zap) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        Entrada e = new Entrada();
        e.setNome(nome);
        e.setSobrenome(snome);
        e.setMail(mail);
        e.setZap(zap);
        dao.salva(e);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createJson(String ent) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader jsonReader = factory.createReader(new StringReader(ent));
        JsonObject json = jsonReader.readObject();
        Entrada e = new Entrada();
        e.setNome(json.getString("nome"));
        e.setSobrenome(json.getString("sobrenome"));
        e.setMail(json.getString("mail"));
        e.setZap(json.getString("zap"));
        dao.salva(e);
        URI uri = uriInfo.getAbsolutePathBuilder().path("entrada/" + String.valueOf(e.getId())).build();
        Response resp = Response.created(uri).build();
        return resp;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Entrada entity) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        dao.salva(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        dao.deleta(id);
    }

    @GET
    @Path("entrada/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Entrada find(@PathParam("id") Long id) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        return dao.recupera(id);
    }

    @GET
    @Path("entrada/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject findJson(@PathParam("id") Long id) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        Entrada e = dao.recupera(id);
        if (e == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        JsonObjectBuilder builder = factory.createObjectBuilder();
        JsonObject obj = builder.add("id", e.getId())
                .add("nome", e.getNome())
                .add("sobrenome", e.getSobrenome())
                .add("mail", e.getMail())
                .add("zap", e.getZap())
                .build();
        return obj;
    }

    @GET
    @Path("entrada/{id}")
    @Produces({MediaType.TEXT_PLAIN})
    public String findplain(@PathParam("id") Long id) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        return dao.recupera(id).toString();
    }

    @GET
    @Path("todos")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Entrada> findAll() {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        return dao.buscaTudo();
    }

    @GET
    @Path("sobrenomes/{sn}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Entrada> findAll(@PathParam("sn") String sn) {
        JPAEntradaDAO dao = new JPAEntradaDAO();
        return dao.buscaSobrenome(sn);
    }

}
