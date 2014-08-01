package com.lafengmaker.tool.dao.impl;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lafengmaker.tool.dao.BaseDao;
@Component()
@Scope(value="prototype")
public  class BaseDaoImpl implements BaseDao{
	   @Autowired
	   private SqlSession sqlSession;
	   /**
	    * get namespace for mybatis map
	    * 
	    * @return String the namespace of map.
	    */
	  public  String getNamespace(){
		  return "JdbcBean";
	  }
	   /**
	    * {@inheritDoc}
	    * 
	    * @param <T>
	    */
	   @SuppressWarnings("unchecked")
	   public <T> T getOne(Object parameter) {
	       return (T) getOne("getOne", parameter);
	   }
	   /**
	    * {@inheritDoc}
	    * 
	    * @param <T>
	    */
	   public Object getOne(String statement, Object parameter) {
	       return sqlSession.selectOne(getNamespace() + "." + statement, parameter);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public <E> List<E> getList(Object parameter) {
	       return getList("getList", parameter);
	   }
	   /**
	    * {@inheritDoc}
	    * 
	    * @param <E>
	    */
	   public <E> List<E> getList(String statement, Object parameter) {
	       return sqlSession.selectList(getNamespace() + "." + statement, parameter);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int getCount(Object parameter) {
	       return (Integer) getOne("getCount", parameter);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int insert(Object entity) {
	       return insert("insert", entity);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int insert(String statement, Object entity) {
	       return sqlSession.insert(getNamespace() + "." + statement, entity);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int update(Object entity) {
	       return update("update", entity);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int update(String statement, Object entity) {
	       return sqlSession.update(getNamespace() + "." + statement, entity);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int delete(Object entity) {
	       return delete("delete", entity);
	   }
	   /**
	    * {@inheritDoc}
	    */
	   public int delete(String statement, Object entity) {
	       return sqlSession.delete(getNamespace() + "." + statement, entity);
	   }
	   public <E> void insertBatch(List<E> entities) {
	       insertBatch("insert", entities);
	   }
	   public <E> void insertBatch(String statement, List<E> entities) {
	       for (Object object : entities) {
	           	insert(statement, object);
	       }
	   }
	public void setSqLSession(SqlSession session) {
		this.sqlSession=session;
		
	}
	   
}