/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author viter
 */
public class JPAEntradaDAO {
    
    private EntityManager em;
    
    public JPAEntradaDAO() {
    }
        
    public void salva(Entrada e) {
        
        em = JPAUtil.getEM();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(e);
        et.commit();
        em.close();
    }
    
    public Entrada recupera(Long id) {
        
        em = JPAUtil.getEM();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Entrada e = em.find(Entrada.class, id);
        et.commit();
        em.close();
        return e;
    }
    
    public void deleta(Long id) {
        
        em = JPAUtil.getEM();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Entrada e = em.find(Entrada.class, id);
        em.remove(e);
        et.commit();
        em.close();
    }
    
    public List<Entrada> buscaSobrenome(String snome) {
        String jpqlQuery = "SELECT e FROM Entrada e where e.sobrenome = :sn";
        em = JPAUtil.getEM();
        Query query = em.createQuery(jpqlQuery);
        query.setParameter("sn", snome);
        List<Entrada> e = query.getResultList();
        em.close();
        return e;
    }
    
    public List<Entrada> buscaTudo() {
        String jpqlQuery = "SELECT e FROM Entrada e";
        em = JPAUtil.getEM();
        Query query = em.createQuery(jpqlQuery);
        List<Entrada> e = query.getResultList();
        em.close();
        return e;
    }
    
}
