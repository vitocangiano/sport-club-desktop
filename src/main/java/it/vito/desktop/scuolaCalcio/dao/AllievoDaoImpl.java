package it.vito.desktop.scuolaCalcio.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import it.vito.desktop.scuolaCalcio.model.Allievo;
import it.vito.desktop.scuolaCalcio.service.BaseService;
import it.vito.desktop.scuolaCalcio.service.Response;
import it.vito.desktop.scuolaCalcio.util.Util;





public class AllievoDaoImpl extends BaseService implements AllievoDao{
	 private static SessionFactory sessionFactory = Util.getSessionFactory();
	    private static Transaction transaction = null;
	
	@Override
	public List<Allievo> getAllAllievi() {
		 try (Session session = sessionFactory.getCurrentSession()) {
	            session.beginTransaction();
	            List<Allievo> allievi = session.createQuery("from Allievo", Allievo.class).getResultList();
	            session.getTransaction().commit();
	            return allievi;
	        }
	    }

			 
	@Override
	public Response saveOrUpDate(Allievo allievo) {
		responseReset();
		try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(allievo);
            session.getTransaction().commit();
            response.setMessage("Salvataggio eseguito");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            setError(ex.getMessage());
            
        }
    
		return response;
	}


	@Override
	public Response deliteAllievo(Allievo allievo) {
		responseReset();
		try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            //Allievo alli = session.get(Allievo.class, allievo.getIdAllievo());
            session.delete(allievo);
            session.getTransaction().commit();
            response.setMessage(allievo.getCognome()+" "+allievo.getNome()+ " Eliminato");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            setError(ex.getMessage());
            
        }
    
		return response;
	}


}
