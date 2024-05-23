package com.zirtoshka.zirtoshka.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class dbController {
    private SessionFactory factory;
    private StandardServiceRegistry registry;
    private Session session;
    //TODO: fix sout

    public dbController() {
        try {
            this.registry = new StandardServiceRegistryBuilder().configure().build();
            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();
            this.factory = metadata.getSessionFactoryBuilder().build();
//            this.factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(HitRRResult.class).buildSessionFactory();
            this.createSession();
        } catch (Exception e) {
            System.out.println("db error");
        }
    }

    //    private void createSession() {this.session = factory.openSession();}
    private void createSession() {
        this.session = this.factory.getCurrentSession();
    }


    public List<HitResult> getUserHits(String sessionId) {
        if (sessionId == null) return null;
        createSession();
        this.session.beginTransaction();
        String sqlRequest = "from HitResult hit where hit.sessionId= :sessionId AND hit.removed=false";
//        List<HitResult> results = this.session.createQuery("from HitResult hit", HitResult.class).getResultList();

        List<HitResult> results = this.session.createQuery(sqlRequest, HitResult.class).setParameter("sessionId", sessionId).getResultList();
        this.session.getTransaction().commit();
        System.out.println("get hits from db: " + results.size());
        return results;
    }

    public void addHitResult(HitResult hitresult) {
        if (hitresult == null) return;
        createSession();
        this.session.beginTransaction();
        System.out.println(hitresult);
        System.out.println(hitresult);
        this.session.persist(hitresult);
//        this.session.save(hitresult);
        this.session.getTransaction().commit();
    }

    //TODO: fix DB props and create DB local and helios
    public void markUserHitsRemoved(String sessionId) {
        if (sessionId == null) return;
        createSession();
        this.session.beginTransaction();
        this.session.createQuery("update HitResult set removed=true where sessionId= :sessionId").setParameter("sessionId", sessionId).executeUpdate();
        this.session.getTransaction().commit();
    }


}
