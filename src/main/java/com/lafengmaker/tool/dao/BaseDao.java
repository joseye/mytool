package com.lafengmaker.tool.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
public interface BaseDao {
   /**
    * Retrieve a single row mapped from parameter.
    * 
    * @param <T>
    * @param <T> the returned object type
    * @param parameter A parameter object to pass to the statement.
    * @return Mapped object
    */
   <T> T getOne(Object parameter);
   /**
    * Retrieve a single row mapped from the statement key and parameter.
    * 
    * @param <T> the returned object type
    * @param statement Unique identifier matching the statement to use.
    * @param parameter A parameter object to pass to the statement.
    * @return Mapped object
    */
   Object getOne(String statement, Object parameter);
   /**
    * Retrieve a list of mapped objects from parameter.
    * 
    * @param <E> the returned list element type
    * @param parameter A parameter object to pass to the statement.
    * @return List of mapped object
    */
   <E> List<E> getList(Object parameter);
   /**
    * Retrieve a list of mapped objects from the statement key and parameter.
    * 
    * @param <E>
    * @param <E> the returned list element type
    * @param statement Unique identifier matching the statement to use.
    * @param parameter A parameter object to pass to the statement.
    * @return List of mapped object
    */
   <E> List<E> getList(String statement, Object parameter);
   /**
    * get a count of mapped objects from parameter.
    * 
    * @param parameter A parameter object to pass to the statement.
    * @return count of mapped object
    */
   int getCount(Object parameter);
   /**
    * Execute an insert statement with the given parameter object. Any generated autoincrement values or selectKey
    * entries will modify the given parameter object properties. Only the number of rows affected will be returned.
    * 
    * @param entity A parameter object to pass to the statement.
    * @return int The number of rows affected by the insert.
    */
   int insert(Object entity);
   /**
    * Execute an insert statement with the given parameter object. Any generated autoincrement values or selectKey
    * entries will modify the given parameter object properties. Only the number of rows affected will be returned.
    * 
    * @param statement Unique identifier matching the statement to execute.
    * @param entity A parameter object to pass to the statement.
    * @return int The number of rows affected by the insert.
    */
   int insert(String statement, Object entity);
   /**
    * Execute an update statement. The number of rows affected will be returned.
    * 
    * @param parameter A parameter object to pass to the statement.
    * @return int The number of rows affected by the update.
    */
   int update(Object entity);
   /**
    * Execute an update statement. The number of rows affected will be returned.
    * 
    * @param statement Unique identifier matching the statement to execute.
    * @param entity A parameter object to pass to the statement.
    * @return int The number of rows affected by the update.
    */
   int update(String statement, Object entity);
   /**
    * Execute a delete statement. The number of rows affected will be returned.
    * 
    * @param entity A parameter object to pass to the statement.
    * @return int The number of rows affected by the delete.
    */
   int delete(String statement );
   /**
    * Execute a delete statement. The number of rows affected will be returned.
    * 
    * @param statement Unique identifier matching the statement to execute.
    * @param entity A parameter object to pass to the statement.
    * @return int The number of rows affected by the delete.
    */
   int delete(String statement, Object entity);
   <E> void insertBatch(List<E> entities);
   void setSqLSession(SqlSession session);
}