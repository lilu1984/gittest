package com.wonders.tdsc.blockwork.dao;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.exception.DataAccessException;
import com.wonders.esframework.id.bo.IncrementId;
import org.hibernate.*;

public class IncrementIdDao extends BaseHibernateDaoImpl
{

    public IncrementIdDao()
    {
    }

    protected Class getEntityClass()
    {
        return com.wonders.esframework.id.bo.IncrementId.class;
    }

    public Long getId(String idName)
    {
        return getId(idName, 1);
    }

    public Long getId(String idName, int increment)
    {
        IncrementId incrementId = null;
        long nextId = 0L;
        try
        {
            String queryHql = "from IncrementId a where a.idName = ?";
            Query query = getSession().createQuery(queryHql);
            query.setParameter(0, idName);
            query.setLockMode("a", LockMode.UPGRADE);
            query.setFlushMode(FlushMode.AUTO);
            incrementId = (IncrementId)query.uniqueResult();
            if(incrementId != null)
            {
                incrementId.setCurrentValue(incrementId.getCurrentValue() + (long)increment);
                update(incrementId);
                nextId = incrementId.getCurrentValue();
            } else
            {
                nextId = 1L;
                incrementId = new IncrementId();
                incrementId.setIdName(idName);
                incrementId.setCurrentValue(nextId);
                save(incrementId);
            }
        }
        catch(HibernateException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return new Long(nextId);
    }

	public IncrementId getIncrement(String name) {
		IncrementId incrementId = null;
		String queryHql = "from IncrementId a where a.idName = ?";
        Query query = getSession().createQuery(queryHql);
        query.setParameter(0, name);
        query.setFlushMode(FlushMode.AUTO);
        incrementId = (IncrementId)query.uniqueResult();
		return incrementId;
	}
}
