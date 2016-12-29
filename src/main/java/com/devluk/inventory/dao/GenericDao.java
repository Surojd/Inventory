package com.devluk.inventory.dao;

import com.devluk.inventory.entity.Purchase;
import com.devluk.inventory.entity.Sales;
import com.devluk.inventory.entity.Stock;
import com.devluk.inventory.entity.Users;
import com.devluk.inventory.entity.Usertype;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GenericDao {

    @Autowired
    SessionFactory sessionFactory;

    public List getAllData(Class c) {
        return sessionFactory.openSession().createCriteria(c).list();
    }

    public List getAllData(String c, Map<String, String[]> l) throws Exception {
        return getAllData(ClassOf(c), l);
    }

    public List getAllData(Class c, Map<String, String[]> l) throws Exception {
        Criteria data = sessionFactory.openSession().createCriteria(c);
        String[] get = l.get("filter");
        if (get != null) {
            for (String get1 : get) {
                String[] split = get1.split(",");
                switch (split[1]) {
                    case "eq":
                        data.add(Expression.sql(split[0] + "='" + split[2] + "'"));
//                        data.add(Restrictions.eq(split[0], numOrString(split[2])));
                        break;
                    case "bt":
                        data.add(Expression.sql(split[0] + " between '" + split[2] + "' and '" + split[3] + "'"));
//                        data.add(Restrictions.between(split[0], numOrString(split[2]), numOrString(split[3])));
                        break;
                    case "gt":
                        data.add(Expression.sql(split[0] + " > '" + split[2] + "'"));
//                        data.add(Restrictions.gt(split[0], numOrString(split[2])));
                        break;
                    case "lt":
                        data.add(Expression.sql(split[0] + " < '" + split[2] + "'"));
//                        data.add(Restrictions.gt(split[0], numOrString(split[2])));
                        break;
                    case "ge":
                        data.add(Expression.sql(split[0] + " >= '" + split[2] + "'"));
//                        data.add(Restrictions.gt(split[0], numOrString(split[2])));
                        break;
                    case "le":
                        data.add(Expression.sql(split[0] + " <= '" + split[2] + "'"));
//                        data.add(Restrictions.gt(split[0], numOrString(split[2])));
                        break;
                    case "cs":
                        data.add(Restrictions.like(split[0], split[2], MatchMode.ANYWHERE));
                        break;
                    case "sw":
                        data.add(Restrictions.like(split[0], split[2], MatchMode.START));
                        break;
                    case "ew":
                        data.add(Restrictions.like(split[0], split[2], MatchMode.END));
                        break;
                }
            }
        }

//        data.add()
        return data.list();
    }

    public List getAllData(String table) throws Exception {
        return getAllData(ClassOf(table));
    }

    public <T> T getByID(Class c, int id) {
        Session session = sessionFactory.openSession();
        return (T) session.get(c, id);
    }

    public <T> T getByID(String c, int id) throws Exception {
        return (T) getByID(ClassOf(c), id);
    }

    public Class ClassOf(String c) throws Exception {
        if (c.equalsIgnoreCase("Users")) {
            return Users.class;
        } else if (c.equalsIgnoreCase("Stock")) {
            return Stock.class;
        } else if (c.equalsIgnoreCase("Purchase")) {
            return Purchase.class;
        } else if (c.equalsIgnoreCase("Sales")) {
            return Sales.class;
        } else if (c.equalsIgnoreCase("UserType")) {
            return Usertype.class;
        }
        throw new Exception("abc");
    }

    @Transactional
    public <T> int saveOrUpdate(T t) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(t);
        return 1;
    }

    private Object numOrString(String chars) {
        boolean num = true;
        for (char ch : chars.toCharArray()) {
            if (Character.isAlphabetic(ch)) {
                num = false;
                break;
            }
        }
        Object obj = null;
        if (num) {
            try {
                obj = Integer.parseInt(chars);
            } catch (Exception e) {
                try {
                    obj = Double.parseDouble(chars);
                } catch (Exception ex) {
                }
            }
        } else {
            obj = chars;
        }
        return obj;
    }
}
