package demo.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4.
 */
@Repository
@Transactional
public class UserDao {

    @Autowired
    private SessionFactory _sessionFactory;

    public Session getSession() {
        return _sessionFactory.getCurrentSession();
    }

    public void save(User user) {
        getSession().save(user);
        return;
    }

    public void delete(User user) {
        getSession().delete(user);
        return;
    }

    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return getSession().createQuery("from User").list();
    }

    public User getByEmail(String email) {
        return (User) getSession().createQuery(
                "from User where email = :email")
                .setParameter("email", email)
                .uniqueResult();
    }

    public User getById(long id) {
        return (User) getSession().load(User.class, id);
    }

    public void update(User user) {
        getSession().update(user);
        return;
    }
}
