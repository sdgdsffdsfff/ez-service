package com.ecfront.service

import com.ecfront.common.{Resp, SReq}
import com.ecfront.service.cache.CacheProcessor
import com.ecfront.storage.PageModel

trait DCacheService[M <: IdModel] extends BasicService[M] {

  //TODO cache
  private val _processor: CacheProcessor[M] = null //CacheProcessor[M](modelClazz)

  override protected def _doFindAll(request: SReq): Resp[List[M]] = ???

  override protected def _doGetByCondition(condition: String, request: SReq): Resp[M] = ???

  override protected def _doPageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = ???

  override protected def _doSave(model: M, request: SReq): Resp[String] = {
    _processor.save(model.id, model)
    Resp.success(model.id)
  }

  override protected def _doFindByCondition(condition: String, request: SReq): Resp[List[M]] = ???

  override protected def _doPageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = ???

  override protected def _doGetById(id: String, request: SReq): Resp[M] = {
    val res = _processor.get(id)
    if (res != null) {
      Resp.success(res.get)
    } else {
      Resp.notFound("")
    }
  }

  override protected def _doUpdate(id: String, model: M, request: SReq): Resp[String] = {
    _processor.update(id, model)
    Resp.success(id)
  }

  override protected def _doDeleteById(id: String, request: SReq): Resp[String] = {
    _processor.delete(id)
    Resp.success(id)
  }

  override protected def _doDeleteAll(request: SReq): Resp[List[String]] = {
    _processor.deleteAll()
    Resp.success(null)
  }

  override protected def _doDeleteByCondition(condition: String, request: SReq): Resp[List[String]] = ???

}

