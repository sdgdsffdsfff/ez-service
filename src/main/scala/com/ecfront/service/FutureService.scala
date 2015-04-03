package com.ecfront.service

import com.ecfront.common.{Resp, SReq}
import com.ecfront.storage.PageModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait FutureService[M <: AnyRef] extends BasicService[M] {

  def _getById(id: String, request: SReq): Future[Resp[M]] = Future {
    _executeGetById(id, request)
  }

  def _getByCondition(condition: String, request: SReq): Future[Resp[M]] = Future {
    _executeGetByCondition(condition, request)
  }

  def _findAll(request: SReq): Future[Resp[List[M]]] = Future {
    _executeFindAll(request)
  }

  def _findByCondition(condition: String, request: SReq): Future[Resp[List[M]]] = Future {
    _executeFindByCondition(condition, request)
  }

  def _pageAll(pageNumber: Long, pageSize: Long, request: SReq): Future[Resp[PageModel[M]]] = Future {
    _executePageAll(pageNumber, pageSize, request)
  }

  def _pageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Future[Resp[PageModel[M]]] = Future {
    _executePageByCondition(condition, pageNumber, pageSize, request)
  }

  def _save(model: M, request: SReq): Future[Resp[String]] = Future {
    _executeSave(model, request)
  }

  def _update(id: String, model: M, request: SReq): Future[Resp[String]] = Future {
    _executeUpdate(id, model, request)
  }

  def _deleteById(id: String, request: SReq): Future[Resp[String]] = Future {
    _executeDeleteById(id, request)
  }

  def _deleteByCondition(condition: String, request: SReq): Future[Resp[List[String]]] = Future {
    _executeDeleteByCondition(condition, request)
  }

  def _deleteAll(request: SReq): Future[Resp[List[String]]] = Future {
    _executeDeleteAll(request)
  }

}



