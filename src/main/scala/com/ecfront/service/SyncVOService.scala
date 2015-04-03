package com.ecfront.service

import java.lang.reflect.ParameterizedType

import com.ecfront.common.{BeanHelper, Resp, SReq}
import com.ecfront.storage.PageModel

import scala.collection.mutable.ArrayBuffer

trait SyncVOService[M <: AnyRef, V <: AnyRef] extends BasicService[M] {

  protected val _typeArgs=this.getClass.getGenericInterfaces()(1).asInstanceOf[ParameterizedType].getActualTypeArguments
  protected val _voClazz = if (_typeArgs.size == 2) _typeArgs(1).asInstanceOf[Class[V]] else null

  protected def modelToVO(model: M, vo: V): V = vo

  protected def voToModel(vo: V, model: M): M = model

  def _findAll(request: SReq): Resp[List[V]] = {
    _find(super._executeFindAll(request))
  }

  def _findByCondition(condition: String, request: SReq): Resp[List[V]] = {
    _find(super._executeFindByCondition(condition, request))
  }

  private def _find(result: Resp[List[M]]): Resp[List[V]] = {
    if (_voClazz == null) {
      result.asInstanceOf[Resp[List[V]]]
    } else {
      if (result) {
        val vos = ArrayBuffer[V]()
        result.body.foreach {
          item =>
            val vo = _voClazz.newInstance()
            BeanHelper.copyProperties(vo, item)
            vos += modelToVO(item, vo)
        }
        Resp.success(vos.toList)
      } else {
        Resp.fail(result.code, result.message)
      }
    }
  }

  def _getByCondition(condition: String, request: SReq): Resp[V] = {
    _get(super._executeGetByCondition(condition, request))
  }

  def _getById(id: String, request: SReq): Resp[V] = {
    _get(super._executeGetById(id, request))
  }

  private def _get(result: Resp[M]): Resp[V] = {
    if (_voClazz == null) {
      result.asInstanceOf[Resp[V]]
    } else {
      if (result) {
        val vo = _voClazz.newInstance()
        BeanHelper.copyProperties(vo, result.body)
        Resp.success(modelToVO(result.body, vo))
      } else {
        Resp.fail(result.code, result.message)
      }
    }
  }

  def _pageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[V]] = {
    _page(super._executePageAll(pageNumber, pageSize, request))
  }

  def _pageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[V]] = {
    _page(super._executePageByCondition(condition, pageNumber, pageSize, request))
  }

  private def _page(result: Resp[PageModel[M]]): Resp[PageModel[V]] = {
    if (_voClazz == null) {
      result.asInstanceOf[Resp[PageModel[V]]]
    } else {
      if (result) {
        val vos = ArrayBuffer[V]()
        result.body.results.foreach {
          item =>
            val vo = _voClazz.newInstance()
            BeanHelper.copyProperties(vo, item)
            vos += modelToVO(item, vo)
        }
        Resp.success(PageModel(result.body.pageNumber, result.body.pageSize, result.body.pageTotal, result.body.recordTotal, vos.toList))
      } else {
        Resp.fail(result.code, result.message)
      }
    }
  }

  def _save(vo: V, request: SReq): Resp[String] = {
    if (_voClazz == null) {
      super._executeSave(vo.asInstanceOf[M], request)
    } else {
      val model = _modelClazz.newInstance()
      BeanHelper.copyProperties(model, vo)
      super._executeSave(voToModel(vo, model), request)
    }
  }

  def _update(id: String, vo: V, request: SReq): Resp[String] = {
    if (_voClazz == null) {
      super._executeUpdate(id,vo.asInstanceOf[M], request)
    } else {
      val model = _modelClazz.newInstance()
      BeanHelper.copyProperties(model, vo)
      super._executeUpdate(id, voToModel(vo, model), request)
    }
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



