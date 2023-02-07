package facade;

import entities.Person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonFacade {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    private jakarta.persistence.EntityManager entityManager;
    public Person createPerson(Person p){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    public List<Person> getAllPersons(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p from Person p", Person.class);
        List<Person> persons = tq.getResultList();
        return persons;
    }

    public Person getPerson(Long id){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        return p;
    }



    public Person updatePerson(Person p){
        Person result = null;
        EntityManager em = emf.createEntityManager();
        Person found = em.find(Person.class, p.getId());
        if(found == null){
            throw new IllegalArgumentException("No Person with this ID: ");
        }
        try{
            em.getTransaction().begin();
            result = em.merge(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        System.out.println("BEFORE UPDATE: " + p);
        System.out.println("AFTER UPDATE: " + result);
        return result;
    }

    public Person deletePerson(Long id){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if(p == null){
            throw new IllegalArgumentException("No Person with this ID: ");
        }
        try{
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    public static void main(String[] args)
    {
        PersonFacade pf = new PersonFacade();
//        Person newbie = new Person("Niko", 25);
//        pf.createPerson(newbie);
//        System.out.println("The person gets this new id: " + newbie.getId());
        System.out.println("Before Any Action");
        pf.getAllPersons().forEach((p)-> System.out.println(p));
//        System.out.println("GET SINGLE PERSON BY ID: ");
//        System.out.println(pf.getPerson(1L));
//        Person p = pf.getPerson(1L);
//        System.out.println(p);
//        p.setName("Chris");
//        pf.updatePerson(p);
        pf.deletePerson(4L);
        System.out.println("After Delete");
        pf.getAllPersons().forEach((person)-> System.out.println(person));

    }
}
