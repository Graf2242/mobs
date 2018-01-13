package Account;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;

public class HAccountsDAO {
    private SessionFactory sessionFactory;

    public HAccountsDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HAccountDataSet get(Long id) throws SQLException {
        Session session = sessionFactory.openSession();
        return session.load(HAccountDataSet.class, id);
    }

    public HAccountDataSet get(String login) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<HAccountDataSet> cq = cb.createQuery(HAccountDataSet.class);
        Root<HAccountDataSet> root = cq.from(HAccountDataSet.class);
        cq.select(root);
        cq.where(cb.equal(root.get("login"), login));
        HAccountDataSet dataSet = session.createQuery(cq).uniqueResult();
        session.close();

        return dataSet;
    }

    @SuppressWarnings("UnusedReturnValue")
    public HAccountDataSet writeAccount(String name, String pass) throws SQLException {
        HAccountDataSet accountDataSet = new HAccountDataSet(name, pass);

        Session session = sessionFactory.openSession();
        session.save(accountDataSet);
        session.close();
        return get(name);
    }
}
