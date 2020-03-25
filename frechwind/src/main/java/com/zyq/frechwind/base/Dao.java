package com.zyq.frechwind.base;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Repository
public class Dao<T, PK extends Serializable> {

    @Autowired
    private SessionFactory sessionFactory;

    public Dao() {
        this.entityClass = getSuperClassGenricType(getClass());
    }

    private Logger log = LoggerFactory.getLogger(Dao.class);

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();


    /*
    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        HibernateJpaSessionFactoryBean hjfb = new HibernateJpaSessionFactoryBean();
        return hjfb;
    }
    */

    private Class<T> entityClass;

    private void validate(T entity) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        StringBuilder buf = new StringBuilder();
        for (ConstraintViolation<T> cv : constraintViolations) {
            String msg = cv.getMessage();
            buf.append(msg + ", ");
        }
        if (buf.length() > 0) {
            throw new AppException(buf.toString());
        }
    }

    private void validateObject(Object o) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);
        StringBuilder buf = new StringBuilder();
        for (ConstraintViolation cv : constraintViolations) {
            String msg = cv.getMessage();
            buf.append(msg + ", ");
        }
        if (buf.length() > 0) {
            throw new AppException(buf.toString());
        }
    }

    public T create(T entity) {
        validate(entity);
        if (entity instanceof TimeStamp) {
            Operator operator = ApplicationContext.getInstance().getOperator();
            try {
                if (operator != null) {
                    BeanUtils.setProperty(entity, "creator", operator.getOperatorId());
                    BeanUtils.setProperty(entity, "modifier", operator.getOperatorId());
                }
                BeanUtils.setProperty(entity, "createTime", new Date());
                BeanUtils.setProperty(entity, "modifyTime", new Date());
                ClientInfo clientInfo = ApplicationContext.getInstance().getClientInfo();
                if (clientInfo != null) {
                    BeanUtils.setProperty(entity, "createIp", clientInfo.getLoginIp());
                    BeanUtils.setProperty(entity, "modifyIp", clientInfo.getLoginIp());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        PK pk = (PK) sessionFactory.getCurrentSession().save(entity);
        if (YmlConfig.useRedis()) {
            JedisCache.getInstance().setObject(redisKey(pk), entity);
        }

        return get(pk);
    }

    /**
     * 更新实体
     *
     * @param entity
     */
    public T update(T entity) {
        validate(entity);
        if (entity instanceof TimeStamp) {
            try {
                Operator operator = ApplicationContext.getInstance().getOperator();
                if (operator != null) {
                    BeanUtils.setProperty(entity, "modifier", operator.getOperatorId());
                }
                BeanUtils.setProperty(entity, "modifyTime", new Date());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        sessionFactory.getCurrentSession().update(entity);
        Serializable pk = sessionFactory.getCurrentSession().getIdentifier(entity);
        entity = get(pk);
        if (YmlConfig.useRedis()) {
            JedisCache.getInstance().setObject(redisKey((PK) pk), entity);
        }
        return entity;
    }

    public T merge(T entity) {
        validate(entity);
        if (entity instanceof TimeStamp) {
            try {
                Operator operator = ApplicationContext.getInstance().getOperator();
                if (operator != null) {
                    BeanUtils.setProperty(entity, "modifier", operator.getOperatorId());
                }
                BeanUtils.setProperty(entity, "modifyTime", new Date());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        entity = (T) sessionFactory.getCurrentSession().merge(entity);
        if (YmlConfig.useRedis()) {
            String pk = (String) sessionFactory.getCurrentSession().getIdentifier(entity);
            JedisCache.getInstance().setObject(redisKey((PK) pk), entity);
        }
        return entity;
    }

    public void flush(){
        sessionFactory.getCurrentSession().flush();
    }

    /**
     * 保存的时候，检查最后修改时间
     *
     * @param entity
     * @return
     */
    public T safeMerge(T entity, PK pk) {
        validate(entity);
        TimeStamp obj = ((TimeStamp) entity);
        TimeStamp dbObject = (TimeStamp) get(pk);

        //对比微秒数，如果一致，那就证明没有被修改过。
        if (obj.getModifyTime().getTime() == dbObject.getModifyTime().getTime()) {
            return merge(entity);
        } else {
            String p = "yy/MM/dd HH:mm:ss.SSS";
            String objTimes = DateFormatUtils.format(obj.getModifyTime(), p);
            String dbTims = DateFormatUtils.format(dbObject.getModifyTime(), p);

            log.info("objTimes:" + objTimes + "; dbTims:" + dbTims);
            log.info("entityClass:" + entityClass.getClass().getName() + "; pk:" + pk);
            AppException ae = new AppException("数据已经被修改，请勿重复提交。");
            ae.printStackTrace();
            throw ae;
        }
    }

    /**
     * 用于对象类型不明时候的保存。
     *
     * @param entity
     * @return
     */
    public Object mergeObject(Object entity) {
        validateObject(entity);
        if (entity instanceof TimeStamp) {
            try {
                Operator operator = ApplicationContext.getInstance().getOperator();
                if (operator != null) {
                    BeanUtils.setProperty(entity, "modifier", operator.getOperatorId());
                }
                BeanUtils.setProperty(entity, "modifyTime", new Date());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        entity = sessionFactory.getCurrentSession().merge(entity);
        if (YmlConfig.useRedis()) {
            String pk = (String) sessionFactory.getCurrentSession().getIdentifier(entity);
            JedisCache.getInstance().setObject(entity.getClass().getSimpleName() + pk, entity);
        }
        return entity;
    }

    public void delete(PK pk) {
        T t = (T) sessionFactory.getCurrentSession().load(this.entityClass, pk);
        sessionFactory.getCurrentSession().delete(t);
        if (YmlConfig.useRedis()) {
            JedisCache.getInstance().deleteObject(redisKey(pk));
        }
    }

    public T load(PK pk) {
        return get(pk);
    }

    public T get(Serializable pk) {
        if (pk == null) {
            return null;
        }
        T obj;
        if (YmlConfig.useRedis()) {
            String redisKey = redisKey(pk);
            obj = (T) JedisCache.getInstance().getObject(redisKey);
            //如果redis里面没有，那么从数据库中加载，并且放到redis里面
            if (obj == null) {
                obj = (T) getCurrentSession().get(this.entityClass, pk);
                if (obj != null) {
                    JedisCache.getInstance().setObject(redisKey, obj);
                }
            }
        } else {
            obj = (T) getCurrentSession().get(this.entityClass, pk);
        }

        return obj;
    }

    private String redisKey(Serializable pk) {
        return entityClass.getSimpleName() + pk;
    }

    public List<T>  findBySql(String sql, Object... values) {
        SQLQuery q = createSQLQuery(sql, values);
        q.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        q.addEntity(entityClass);
        List list = q.list();
        return list;
    }

    public Pager<T> findPagerBySql(Pager pager, String sql, Object... values) {
        pager = pager == null ? new Pager<T>() : pager;
        SQLQuery q = createSQLQuery(sql, values);
        pager.setTotalCount(countSqlResult(sql, values));
        q.setFirstResult(pager.getFirst());
        q.setMaxResults(pager.getPageSize());
        q.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        q.addEntity(entityClass);
        List list = q.list();
        pager.setPageItems(list);
        pager.fillPageList();

        return pager;
    }

    public Pager findPagerBySqlReturnObjectArray(Pager pager, String sql, Object... values) {
        pager = pager == null ? new Pager() : pager;
        SQLQuery q = createSQLQuery(sql, values);
        pager.setTotalCount(countSqlResultSqlInludeGroup(sql, values));
        q.setFirstResult(pager.getFirst());
        q.setMaxResults(pager.getPageSize());
        List list = q.list();
        pager.setPageItems(list);
        pager.fillPageList();

        return pager;
    }

    public List<T> find(String hql, Object... values) {
        return createQuery(hql, values).list();
    }

    public T getUnique(String hql, Object... values) {
        try {
            Query query = createQuery(hql, values);
            return (T) query.uniqueResult();
        } catch (NonUniqueResultException e) {
            StringBuffer buf = new StringBuffer();
            for (Object o : values) {
                buf.append(o + ",");
            }
            throw new AppException("NonUniqueResultException hql:" + hql + "; values:" + buf);
        }
    }

    /**
     * 这个方法调用的时候，必须调用addEntity进行类型的转换
     *
     * @param sql
     * @param values
     * @return
     */
    public Object getUniqueBySql(String sql, Object... values) {
        try {
            SQLQuery query = createSQLQuery(sql, values);
            return query.uniqueResult();
        } catch (NonUniqueResultException e) {
            StringBuffer buf = new StringBuffer();
            for (Object o : values) {
                buf.append(o + ",");
            }
            throw new AppException("hql:" + sql + "; values:" + buf);
        }
    }

    public Object getUniqueBySql(String sql, Class clz, Object... values) {
        try {
            SQLQuery query = createSQLQuery(sql, values);
            query.addEntity(clz);
            return query.uniqueResult();
        } catch (NonUniqueResultException e) {
            StringBuffer buf = new StringBuffer();
            for (Object o : values) {
                buf.append(o + ",");
            }
            throw new AppException("hql:" + sql + "; values:" + buf);
        }
    }

    public T getUnique(Finder finder) {
        KeyValue<String, Map<String, Serializable>> kv = finder.toHql();
        String hql = kv.getKey();
        Map properties = kv.getValue();

        Query query = getCurrentSession().createQuery(hql);
        query.setProperties(properties);

        try {
            return (T) query.uniqueResult();
        } catch (NonUniqueResultException e) {
            throw new AppException(e.getMessage() + ", " + finder);
        }
    }

    public SQLQuery createSQLQuery(String queryString, Object... values) {
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    public Query createQuery(String hql, Object... values) {
        Query query = getCurrentSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    public Query createQuery(String queryString, Map valuesMap) {
        Query query = getCurrentSession().createQuery(queryString);
        query.setProperties(valuesMap);
        return query;
    }

    public Pager<T> findPager(Pager<T> pager, String hql, Map values) {
        if (pager == null) {
            pager = new Pager<T>();
        }
        values = (values == null ? new HashMap() : values);
        Query q = createQuery(hql, values);
        pager.setTotalCount(countHqlResult(hql, values));
        q.setFirstResult(pager.getFirst());
        q.setMaxResults(pager.getPageSize());
        q.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        pager.setPageItems(q.list());
        pager.fillPageList();
        return pager;
    }

    public T first(String hql, Object ... paras) {
        List<T> list = find(hql, paras);
        if(list.size() == 0){
            return null;
        }
        return list.get(0);
    }

    public Pager<T> findPager(Finder finder) {
        Pager<T> pager = finder.getPager();
        pager = pager == null ? new Pager<T>() : pager;

        KeyValue<String, Map<String, Serializable>> kv = finder.toHql();
        String hql = kv.getKey();
        Map properties = kv.getValue();

        Query query = getCurrentSession().createQuery(hql);
        query.setProperties(properties);

        pager.setTotalCount(countHqlResult(hql, properties));
        query.setFirstResult(pager.getFirst());
        query.setMaxResults(pager.getPageSize());
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        pager.setPageItems(query.list());

        pager.setParaMap(finder.getParaMap());

        finder.setPager(pager);
        pager.fillPageList();

        return pager;
    }

    public List<T> find(Finder finder) {
        KeyValue<String, Map<String, Serializable>> kv = finder.toHql();
        String hql = kv.getKey();
        Map properties = kv.getValue();

        Query query = getCurrentSession().createQuery(hql);
        query.setProperties(properties);

        List<T> list = query.list();
        if (list == null) {
            list = new ArrayList<T>();
        }
        return list;
    }

    public int countSqlResult(String sql, Object... values) {
        int orderPos = StringUtils.indexOfIgnoreCase(sql, "order by");
        if (orderPos > 0) {
            sql = StringUtils.substring(sql, 0, orderPos);
        }

        int fromPos = StringUtils.indexOfIgnoreCase(sql, "from");
        if (fromPos > 0) {
            sql = "select count(*) from " + StringUtils.substring(sql, fromPos + 5);
        }

        try {
            Number count = (Number) createSQLQuery(sql, values).uniqueResult();
            return count.intValue();
        } catch (Exception e) {
            throw new RuntimeException("sql can't be auto count, sql is:" + sql + "" + e.getMessage(), e);
        }
    }

    public int countSqlResultSqlInludeGroup(String sql, Object... values) {
        sql = "select count(*) from (" + sql + ") countSql";
        try {
            Number count = (Number) createSQLQuery(sql, values).uniqueResult();
            return count.intValue();
        } catch (Exception e) {
            throw new RuntimeException("sql can't be auto count, sql is:" + sql + "" + e.getMessage(), e);
        }
    }

    private int countHqlResult(String hql, Map<String, ?> values) {
        String fromHql = hql;
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        try {
            Query query = getCurrentSession().createQuery(countHql);
            for (String key : values.keySet()) {
                query.setParameter(key, values.get(key));
            }
            Number num = (Number) query.uniqueResult();

            return num.intValue();
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql + "" + e.getMessage(), e);
        }
    }

    public Session getCurrentSession() {
        Session session = sessionFactory.getCurrentSession();
        return session;
    }

    private static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if ((index >= params.length) || (index < 0)) {
            return Object.class;
        }
        if (!(params[index] instanceof Class<?>)) {
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getSuperClassGenricType(Class clazz) {
        return (Class<T>) getSuperClassGenricType(clazz, 0);
    }
}