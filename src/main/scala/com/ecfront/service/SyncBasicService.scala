package com.ecfront.service

import com.ecfront.common.{Resp, SReq}
import com.ecfront.storage.PageModel

trait SyncBasicService[M <: AnyRef] extends BasicService[M] {

  def _getById(id: String, request: SReq): Resp[M] = {
    _executeGetById(id, request)
  }

  def _getByCondition(condition: String, request: SReq): Resp[M] = {
    _executeGetByCondition(condition, request)
  }

  def _findAll(request: SReq): Resp[List[M]] = {
    _executeFindAll(request)
  }

  def _findByCondition(condition: String, request: SReq): Resp[List[M]] = {
    _executeFindByCondition(condition, request)
  }

  def _pageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    _executePageAll(pageNumber, pageSize, request)
  }

  def _pageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    _executePageByCondition(condition, pageNumber, pageSize, request)
  }

  def _save(model: M, request: SReq): Resp[String] = {
    _executeSave(model, request)
  }

  def _update(id: String, model: M, request: SReq): Resp[String] = {
    _executeUpdate(id, model, request)
  }

  def _deleteById(id: String, request: SReq): Resp[String] = {
    _executeDeleteById(id, request)
  }

  def _deleteByCondition(condition: String, request: SReq): Resp[List[String]] = {
    _executeDeleteByCondition(condition, request)
  }

  def _deleteAll(request: SReq): Resp[List[String]] = {
    _executeDeleteAll(request)
  }

}



