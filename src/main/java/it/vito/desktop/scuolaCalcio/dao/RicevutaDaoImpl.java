package it.vito.desktop.scuolaCalcio.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.mysql.cj.Query;

import it.vito.desktop.scuolaCalcio.model.Ricevuta;
import it.vito.desktop.scuolaCalcio.service.BaseService;
import it.vito.desktop.scuolaCalcio.service.Response;
import it.vito.desktop.scuolaCalcio.util.Util;

public class RicevutaDaoImpl extends BaseService implements RicevutaDao{
	 private static SessionFactory sessionFactory = Util.getSessionFactory();
	    private static Transaction transaction = null;
	@Override
	public List<Ricevuta> getAllRicevute() {
		 try (Session session = sessionFactory.getCurrentSession()) {
	            session.beginTransaction();
	            List<Ricevuta> ricevute = session.createQuery("from Ricevuta", Ricevuta.class).getResultList();
	            session.getTransaction().commit();
	            return ricevute;
	        }
	    }

	@Override
	public Response saveOrUpDate(Ricevuta ricevuta) {
		responseReset();
		try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(ricevuta);
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
	public Response deliteRicevuta(Ricevuta ricevuta) {
		responseReset();
		try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.delete(ricevuta);
            session.getTransaction().commit();
            response.setMessage(ricevuta.getData()+" "+ricevuta.getNote()+ " Eliminato");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            setError(ex.getMessage());
            
        }
    
		return response;
	}

	@Override
	public List<Ricevuta> getMaxNumber() {
		Integer result;
	try (Session session = sessionFactory.getCurrentSession()) {
		 transaction = session.beginTransaction();
		String sqlQuery = "select max(r.numero) from ricevuta r "; 
		org.hibernate.query.Query<Ricevuta> q=session.createQuery("select max(numero) from Ricevuta");
		List a = q.list();
		return a;
	} catch (Exception e) {
		// TODO: handle exception
	}
		return null;
	}

	@Override
	public List<Ricevuta> getAllIdRicevute(Integer idAllievo) throws Exception {
		List<Ricevuta> list = new ArrayList<>();
		try (Session session = sessionFactory.getCurrentSession()) {
			 transaction = session.beginTransaction();
			String sqlQuery = "from Ricevuta r where r.idAllievo="+idAllievo; 
			org.hibernate.query.Query<Ricevuta> q=session.createQuery(sqlQuery);
			list = q.list();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
			return list;
		}

}
