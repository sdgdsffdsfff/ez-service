package com.ecfront.service

import com.ecfront.common.{SReq, Resp}
import com.ecfront.storage.{JDBCStorable, PageModel}
import com.typesafe.scalalogging.slf4j.LazyLogging

trait JDBCService[M <: IdModel] extends BasicService[M] with JDBCStorable[M, SReq] {

  override protected def _doFindAll(request: SReq): Resp[List[M]] = {
    _doFindByCondition("1=1", request)
  }

  override protected def _doFindByCondition(condition: String, request: SReq): Resp[List[M]] = {
    Resp.success(__findByCondition(_addDefaultSort(condition), request).orNull)
  }

  override protected def _doGetByCondition(condition: String, request: SReq): Resp[M] = {
    Resp.success(__getByCondition(condition, request).get)
  }

  override protected def _doPageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    _doPageByCondition("1=1", pageNumber, pageSize, request)
  }

  override protected def _doPageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    Resp.success(__pageByCondition(_addDefaultSort(condition), pageNumber, pageSize, request).orNull)
  }

  override protected def _doSave(model: M, request: SReq): Resp[String] = {
    Resp.success(__save(model, request).orNull)
  }

  protected def _doSaveWithoutTransaction(model: M, request: SReq): Resp[String] = {
    Resp.success(__saveWithoutTransaction(model, request).orNull)
  }

  override protected def _doGetById(id: String, request: SReq): Resp[M] = {
    Resp.success(__getById(id, request).get)
  }

  override protected def _doUpdate(id: String, model: M, request: SReq): Resp[String] = {
    Resp.success(__update(id, model, request).orNull)
  }

  protected def _doUpdateWithoutTransaction(id: String, model: M, request: SReq): Resp[String] = {
    Resp.success(__updateWithoutTransaction(id, model, request).orNull)
  }

  override protected def _doDeleteById(id: String, request: SReq): Resp[String] = {
    Resp.success(__deleteById(id, request).orNull)
  }

  protected def _doDeleteByIdWithoutTransaction(id: String, request: SReq): Resp[String] = {
    Resp.success(__deleteByIdWithoutTransaction(id, request).orNull)
  }

  override protected def _doDeleteAll(request: SReq): Resp[List[String]] = {
    Resp.success(__deleteAll(request).orNull)
  }

  protected def _doDeleteAllWithoutTransaction(request: SReq): Resp[List[String]] = {
    Resp.success(__deleteAllWithoutTransaction(request).orNull)
  }

  override protected def _doDeleteByCondition(condition: String, request: SReq): Resp[List[String]] = {
    Resp.success(__deleteByCondition(condition, request).orNull)
  }

  protected def _doDeleteByConditionWithoutTransaction(condition: String, request: SReq): Resp[List[String]] = {
    Resp.success(__deleteByConditionWithoutTransaction(condition, request).orNull)
  }

  private def _addDefaultSort(condition: String): String = {
    if (condition.toLowerCase.indexOf("order by") == -1 && classOf[SecureModel].isAssignableFrom(_modelClazz)) {
      condition + " ORDER BY update_time desc"
    } else {
      condition
    }
  }

}

object JDBCService extends LazyLogging {

  def init(): Unit = {
    var path = this.getClass.getResource("/").getPath
    if (System.getProperties.getProperty("os.name").toUpperCase.indexOf("WINDOWS") != -1) {
      path = path.substring(1)
    }
    JDBCStorable.init(path)
  }

}
