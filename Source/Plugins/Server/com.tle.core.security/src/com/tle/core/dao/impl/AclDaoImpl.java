/*
 * Copyright 2017 Apereo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tle.core.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tle.beans.Institution;
import com.tle.beans.security.ACLEntryMapping;
import com.tle.beans.security.AccessEntry;
import com.tle.common.security.TargetListEntry;
import com.tle.core.dao.AclDao;
import com.tle.core.dao.helpers.CollectionPartitioner;
import com.tle.core.guice.Bind;
import com.tle.core.hibernate.dao.GenericDaoImpl;
import com.tle.core.security.impl.SecureOnCallSystem;
import com.tle.core.user.CurrentInstitution;

/**
 * @author Nicholas Read
 */
@Bind(AclDao.class)
@Singleton
@SuppressWarnings("nls")
public class AclDaoImpl extends GenericDaoImpl<AccessEntry, Long> implements AclDao
{
	public AclDaoImpl()
	{
		super(AccessEntry.class);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Long save(AccessEntry entity)
	{
		entity.generateAggregateOrdering();
		return super.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void saveOrUpdate(AccessEntry entity)
	{
		entity.generateAggregateOrdering();
		super.saveOrUpdate(entity);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(final String target, final String privilege, final Institution institution)
	{
		getHibernateTemplate().execute(new HibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session)
			{
				Query query = session.createQuery("delete from AccessEntry where"
					+ " targetObject = :target and privilege = :privilege" + " and institution = :institution");
				query.setString("target", target);
				query.setString("privilege", privilege);
				query.setParameter("institution", institution);

				query.executeUpdate();

				return null;
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteAll(final String target, final boolean targetIsPartial, final List<Integer> priorities)
	{
		getHibernateTemplate().execute(new HibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session)
			{
				String op = targetIsPartial ? "like" : "=";

				String tar = target;
				if( targetIsPartial )
				{
					tar += "%";
				}

				Query query = session.createQuery("delete from AccessEntry where" + " targetObject " + op
					+ " :target and institution = :institution" + " and aclPriority in (:priorities)");
				query.setString("target", tar);
				query.setParameterList("priorities", priorities);
				query.setParameter("institution", CurrentInstitution.get());
				query.executeUpdate();
				return null;
			}
		});
	}

	@Override
	@SecureOnCallSystem
	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteAll()
	{
		getHibernateTemplate().deleteAll(listAll());
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.MANDATORY)
	public List<Object[]> getPrivileges(final Collection<String> privileges, final Collection<Long> expressions)
	{
		return getHibernateTemplate().executeFind(new TLEHibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session) throws HibernateException
			{
				Query query = session.getNamedQuery("getPrivileges");
				query.setParameter("institution", CurrentInstitution.get());
				query.setParameterList("privileges", privileges);
				query.setParameterList("expressions", expressions);
				return query.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.MANDATORY)
	public List<Object[]> getPrivilegesForTargets(final Collection<String> privileges, Collection<String> targets,
		final Collection<Long> expressions)
	{
		return getHibernateTemplate().executeFind(new CollectionPartitioner<String, Object[]>(targets)
		{
			@Override
			public List<Object[]> doQuery(Session session, Collection<String> collection)
			{
				Query query = session.getNamedQuery("getPrivilegesForTargets");
				query.setParameter("institution", CurrentInstitution.get());
				query.setParameterList("privileges", privileges);
				query.setParameterList("targets", collection);
				query.setParameterList("expressions", expressions);
				return query.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.MANDATORY)
	public List<TargetListEntry> getTargetListEntries(final String target, final Collection<Integer> priorities)
	{
		return getHibernateTemplate().executeFind(new TLEHibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session) throws HibernateException
			{
				Query query = session.getNamedQuery("getTargetListEntries");
				query.setParameter("institution", CurrentInstitution.get());
				query.setParameterList("priorities", priorities);
				query.setString("target", target);
				return query.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.MANDATORY)
	public List<ACLEntryMapping> getAllEntries(final Collection<String> privileges, Collection<String> targets)
	{
		return getHibernateTemplate().executeFind(new CollectionPartitioner<String, ACLEntryMapping>(targets)
		{
			@Override
			public List<ACLEntryMapping> doQuery(Session session, Collection<String> collection)
			{
				Query query = session.getNamedQuery("getAllEntries");
				query.setParameter("institution", CurrentInstitution.get());
				query.setParameterList("targets", collection);
				query.setParameterList("privileges", privileges);
				return query.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.MANDATORY)
	public List<AccessEntry> listAll()
	{
		return getHibernateTemplate().executeFind(new TLEHibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session) throws HibernateException
			{
				Query query = session.getNamedQuery("getAllEntriesForInstitution");
				query.setParameter("institution", CurrentInstitution.get());
				return query.list();
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void remapExpressionId(final long oldId, final long newId)
	{
		getHibernateTemplate().executeFind(new TLEHibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session) throws HibernateException
			{
				Query query = session.createQuery("UPDATE AccessEntry SET expression.id = :newId"
					+ " WHERE institution = :institution AND expression.id = :oldId");
				query.setLong("oldId", oldId);
				query.setLong("newId", newId);
				query.setParameter("institution", CurrentInstitution.get());
				query.executeUpdate();
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccessEntry> getVirtualAccessEntries(Collection<Integer> priorities)
	{
		return getHibernateTemplate().findByNamedParam(
			"FROM AccessEntry WHERE institution = :inst AND aclPriority in (:priorities) order by aclOrder desc",
			new String[]{"inst", "priorities"}, new Object[]{CurrentInstitution.get(), priorities});
	}
}