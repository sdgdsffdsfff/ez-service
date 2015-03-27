package com.ecfront.service

import java.lang.reflect.ParameterizedType
import java.util.UUID

import com.ecfront.common.{BeanHelper, Resp, SReq}
import com.ecfront.service.cache.{CacheProcessor, Cacheable}
import com.ecfront.storage.PageModel
import com.typesafe.scalalogging.slf4j.LazyLogging

trait BasicService[M <: AnyRef] extends LazyLogging {

  protected val _modelClazz = this.getClass.getGenericInterfaces()(0).asInstanceOf[ParameterizedType].getActualTypeArguments()(0).asInstanceOf[Class[M]]

  protected val _isIdModel = classOf[IdModel].isAssignableFrom(_modelClazz)
  protected val _cacheable = this.getClass.getGenericInterfaces.exists(_.getTypeName == classOf[Cacheable].getName)
  //TODO cache
  protected val _cacheProcessor: CacheProcessor[M] = null // if (cacheable) CacheProcessor[M](modelClazz) else null

  logger.info( """Create Service: model: %s""".format(_modelClazz.getSimpleName))

  //=========================Common=========================

  protected def _convertToView(model: M, request: SReq): M = {
    model
  }

  protected def _convertToViews(models: List[M], request: SReq): List[M] = {
    models
  }

  //=========================GetByID=========================

  protected def _preGetById(id: String, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postGetById(result: M, preResult: Any, request: SReq): Resp[M] = {
    Resp.success(result)
  }

  protected def _executeGetById(id: String, request: SReq): Resp[M] = {
    val cResult = if (_cacheable) _cacheProcessor.get(id) else null
    if (cResult == null) {
      val preResult = _preGetById(id, request)
      if (preResult) {
        val result = _doGetById(id, request)
        if (result) {
          if (result.body != null) {
            if (_cacheable) _cacheProcessor.save(id, result.body)
            _postGetById(_convertToView(result.body, request), preResult.body, request)
          } else {
            Resp.notFound("Model [%s] not exist by %s".format(_modelClazz.getSimpleName, id))
          }
        } else {
          Resp.fail(result.code, result.message)
        }
      } else {
        Resp.fail(preResult.code, preResult.message)
      }
    } else {
      Resp.success(cResult.get)
    }
  }

  protected def _doGetById(id: String, request: SReq): Resp[M]

  //=========================GetByCondition=========================

  protected def _preGetByCondition(condition: String, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postGetByCondition(result: M, preResult: Any, request: SReq): Resp[M] = {
    Resp.success(result)
  }

  protected def _executeGetByCondition(condition: String, request: SReq): Resp[M] = {
    val cResult = if (_cacheable) _cacheProcessor.get(condition.hashCode + "") else null
    if (cResult == null) {
      val preResult = _preGetByCondition(condition, request)
      if (preResult) {
        val result = _doGetByCondition(condition, request)
        if (result) {
          if (result.body != null) {
            if (_cacheable) _cacheProcessor.save(condition.hashCode + "", result.body)
            _postGetByCondition(_convertToView(result.body, request), preResult.body, request)
          } else {
            Resp.notFound("Model [%s] not exist by %s".format(_modelClazz.getSimpleName, condition))
          }
        } else {
          Resp.fail(result.code, result.message)
        }
      } else {
        Resp.fail(preResult.code, preResult.message)
      }
    } else {
      Resp.success(cResult.get)
    }
  }

  protected def _doGetByCondition(condition: String, request: SReq): Resp[M]

  //=========================FindAll=========================

  protected def _preFindAll(request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postFindAll(result: List[M], preResult: Any, request: SReq): Resp[List[M]] = {
    Resp.success(result)
  }

  protected def _executeFindAll(request: SReq): Resp[List[M]] = {
    val preResult = _preFindAll(request)
    if (preResult) {
      val result = _doFindAll(request)
      if (result) {
        _postFindAll(_convertToViews(result.body, request), preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doFindAll(request: SReq): Resp[List[M]]

  //=========================FindByCondition=========================

  protected def _preFindByCondition(condition: String, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postFindByCondition(result: List[M], preResult: Any, request: SReq): Resp[List[M]] = {
    Resp.success(result)
  }

  protected def _executeFindByCondition(condition: String, request: SReq): Resp[List[M]] = {
    val preResult = _preFindByCondition(condition, request)
    if (preResult) {
      val result = _doFindByCondition(condition, request)
      if (result) {
        _postFindByCondition(_convertToViews(result.body, request), preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doFindByCondition(condition: String, request: SReq): Resp[List[M]]

  //=========================PageAll=========================

  protected def _prePageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postPageAll(result: PageModel[M], preResult: Any, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    Resp.success(result)
  }

  protected def _executePageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    val preResult = _prePageAll(pageNumber, pageSize, request)
    if (preResult) {
      val result = _doPageAll(pageNumber, pageSize, request)
      if (result) {
        result.body.setResults(_convertToViews(result.body.results, request))
        _postPageAll(result.body, preResult.body, pageNumber, pageSize, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doPageAll(pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]]

  //=========================PageByCondition=========================

  protected def _prePageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postPageByCondition(result: PageModel[M], preResult: Any, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    Resp.success(result)
  }

  protected def _executePageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]] = {
    val preResult = _prePageByCondition(condition, pageNumber, pageSize, request)
    if (preResult) {
      val result = _doPageByCondition(condition, pageNumber, pageSize, request)
      if (result) {
        result.body.setResults(_convertToViews(result.body.results, request))
        _postPageByCondition(result.body, preResult.body, pageNumber, pageSize, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doPageByCondition(condition: String, pageNumber: Long, pageSize: Long, request: SReq): Resp[PageModel[M]]

  //=========================Save=========================

  protected def _preSave(model: M, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postSave(result: String, preResult: Any, request: SReq): Resp[String] = {
    Resp.success(result)
  }

  protected def _executeSave(model: M, request: SReq): Resp[String] = {
    model match {
      case idModel: IdModel =>
        if (idModel.id == null) {
          idModel.id = UUID.randomUUID().toString
        } else {
          if (_doGetById(idModel.id, request).body != null) {
            return Resp.badRequest("Id exist :" + idModel.id)
          }
        }
        idModel match {
          case secureModel: SecureModel =>
            secureModel.create_time = System.currentTimeMillis()
            secureModel.create_user = request.accountId
            secureModel.update_time = System.currentTimeMillis()
            secureModel.update_user = request.accountId
          case _ =>
        }
      case _ =>
    }
    val preResult = _preSave(model, request)
    if (preResult) {
      val result = _doSave(model, request)
      if (result) {
        if (_cacheable && _isIdModel) _cacheProcessor.save(model.asInstanceOf[IdModel].id, model)
        _postSave(result.body, preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doSave(model: M, request: SReq): Resp[String]

  //=========================Update=========================

  protected def _preUpdate(id: String, model: M, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postUpdate(result: String, preResult: Any, request: SReq): Resp[String] = {
    Resp.success(result)
  }

  protected def _executeUpdate(id: String, model: M, request: SReq): Resp[String] = {
    val getResult = _doGetById(id, request)
    if (getResult) {
      BeanHelper.copyProperties(getResult.body, model)
      val nModel = getResult.body
      nModel match {
        case idModel: IdModel =>
          idModel.id = id
          idModel match {
            case secureModel: SecureModel =>
              secureModel.update_time = System.currentTimeMillis()
              secureModel.update_user = request.accountId
            case _ =>
          }
        case _ =>
      }
      val preResult = _preUpdate(id, nModel, request)
      if (preResult) {
        val result = _doUpdate(id, nModel, request)
        if (result) {
          if (_cacheable) _cacheProcessor.update(id, nModel)
          _postUpdate(result.body, preResult.body, request)
        } else {
          Resp.fail(result.code, result.message)
        }
      } else {
        Resp.fail(preResult.code, preResult.message)
      }
    } else {
      Resp.fail(getResult.code, getResult.message)
    }
  }

  protected def _doUpdate(id: String, model: M, request: SReq): Resp[String]

  //=========================DeleteById=========================

  protected def _preDeleteById(id: String, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postDeleteById(result: String, preResult: Any, request: SReq): Resp[String] = {
    Resp.success(result)
  }

  protected def _executeDeleteById(id: String, request: SReq): Resp[String] = {
    if (_cacheable) _cacheProcessor.delete(id)
    val preResult = _preDeleteById(id, request)
    if (preResult) {
      val result = _doDeleteById(id, request)
      if (result) {
        _postDeleteById(result.body, preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doDeleteById(id: String, request: SReq): Resp[String]

  //=========================DeleteByCondition=========================

  protected def _preDeleteByCondition(condition: String, request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postDeleteByCondition(result: List[String], preResult: Any, request: SReq): Resp[List[String]] = {
    Resp.success(result)
  }

  protected def _executeDeleteByCondition(condition: String, request: SReq): Resp[List[String]] = {
    if (_cacheable) _cacheProcessor.delete(condition.hashCode + "")
    val preResult = _preDeleteByCondition(condition, request)
    if (preResult) {
      val result = _doDeleteByCondition(condition, request)
      if (result) {
        _postDeleteByCondition(result.body, preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doDeleteByCondition(condition: String, request: SReq): Resp[List[String]]

  //=========================DeleteAll=========================

  protected def _preDeleteAll(request: SReq): Resp[Any] = {
    Resp.success(null)
  }

  protected def _postDeleteAll(result: List[String], preResult: Any, request: SReq): Resp[List[String]] = {
    Resp.success(result)
  }

  protected def _executeDeleteAll(request: SReq): Resp[List[String]] = {
    if (_cacheable) _cacheProcessor.deleteAll()
    val preResult = _preDeleteAll(request)
    if (preResult) {
      val result = _doDeleteAll(request)
      if (result) {
        _postDeleteAll(result.body, preResult.body, request)
      } else {
        Resp.fail(result.code, result.message)
      }
    } else {
      Resp.fail(preResult.code, preResult.message)
    }
  }

  protected def _doDeleteAll(request: SReq): Resp[List[String]]

}



