package com.ecfront.service

import com.ecfront.common.SReq
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class JDBCServiceSpec extends FunSuite {

  JDBCService.init()

  test("Async测试") {

    val request = SReq("0000", "jzy")

    //-------------------save--------------------------------------------
    val model = TestModel()
    model.name = "张三"
    model.bool = true
    model.age = 14
    model.id = "id001"
    Await.result(TestJDBCService._save(model, request), Duration.Inf)
    assert(Await.result(TestJDBCService._save(model, request), Duration.Inf).message == "Id exist :id001")
    var resultSingle = Await.result(TestJDBCService._getById("id001", request), Duration.Inf).body
    assert(resultSingle.name == "张三")
    assert(resultSingle.bool)
    assert(resultSingle.create_user == "jzy")
    assert(resultSingle.update_user == "jzy")
    assert(resultSingle.create_time != 0)
    assert(resultSingle.update_time != 0)
    //-------------------update--------------------------------------------
    model.name = "haha"
    model.bool = false
    Await.result(TestJDBCService._update("id001", model, request), Duration.Inf)
    resultSingle = Await.result(TestJDBCService._getById("id001", request), Duration.Inf).body
    assert(resultSingle.name == "haha")
    assert(resultSingle.create_time != 0)
    assert(!resultSingle.bool)
    //-------------------getByCondition--------------------------------------------
    resultSingle = Await.result(TestJDBCService._getByCondition("id='%s' AND name='%s'".format("id001", "haha"), request), Duration.Inf).body
    assert(resultSingle.name == "haha")
    //-------------------findAll--------------------------------------------
    var resultList = Await.result(TestJDBCService._findAll(request), Duration.Inf).body
    assert(resultList.size == 1)
    assert(resultList.head.name == "haha")
    //-------------------pageAll--------------------------------------------
    model.id = null
    Await.result(TestJDBCService._save(model, request), Duration.Inf)
    model.id = null
    Await.result(TestJDBCService._save(model, request), Duration.Inf)
    model.id = null
    Await.result(TestJDBCService._save(model, request), Duration.Inf)
    model.id = null
    model.name = "last"
    Await.result(TestJDBCService._save(model, request), Duration.Inf)
    var resultPage = Await.result(TestJDBCService._pageAll(2, 2, request), Duration.Inf).body
    assert(resultPage.getPageNumber == 2)
    assert(resultPage.getPageSize == 2)
    assert(resultPage.getPageTotal == 3)
    assert(resultPage.getRecordTotal == 5)
    //-------------------pageByCondition--------------------------------------------
    resultPage = Await.result(TestJDBCService._pageByCondition("name = '%s' ORDER BY create_time desc".format("haha"), 1, 3, request), Duration.Inf).body
    assert(resultPage.getPageNumber == 1)
    assert(resultPage.getPageSize == 3)
    assert(resultPage.getPageTotal == 2)
    assert(resultPage.getRecordTotal == 4)
    //-------------------deleteById--------------------------------------------
    Await.result(TestJDBCService._deleteById(resultPage.results.last.id, request), Duration.Inf)
    resultList = Await.result(TestJDBCService._findByCondition("id='%s'".format(resultPage.results.head.id), request), Duration.Inf).body
    assert(resultList.size == 1)
    resultList = Await.result(TestJDBCService._findByCondition("id='%s'".format(resultPage.results.last.id), request), Duration.Inf).body
    assert(resultList.size == 0)
    //-------------------deleteAll--------------------------------------------
    Await.result(TestJDBCService._deleteAll(request), Duration.Inf)
    resultList = Await.result(TestJDBCService._findAll(request), Duration.Inf).body
    assert(resultList.size == 0)
  }

  test("Sync测试") {

    val request = SReq("0000", "jzy")

    //-------------------save--------------------------------------------
    val model = TestModel()
    model.name = "张三"
    model.bool = true
    model.age = 14
    model.id = "id001"
    TestJDBCSyncService._save(model, request)
    assert(TestJDBCSyncService._save(model, request).message == "Id exist :id001")
    var resultSingle = TestJDBCSyncService._getById("id001", request).body
    assert(resultSingle.name == "张三")
    assert(resultSingle.bool)
    assert(resultSingle.create_user == "jzy")
    assert(resultSingle.update_user == "jzy")
    assert(resultSingle.create_time != 0)
    assert(resultSingle.update_time != 0)
    //-------------------update--------------------------------------------
    model.name = "haha"
    model.bool = false
    TestJDBCSyncService._update("id001", model, request)
    resultSingle = TestJDBCSyncService._getById("id001", request).body
    assert(resultSingle.name == "haha")
    assert(resultSingle.create_time != 0)
    assert(!resultSingle.bool)
    //-------------------getByCondition--------------------------------------------
    resultSingle = TestJDBCSyncService._getByCondition("id='%s' AND name='%s'".format("id001", "haha"), request).body
    assert(resultSingle.name == "haha")
    //-------------------findAll--------------------------------------------
    var resultList = TestJDBCSyncService._findAll(request).body
    assert(resultList.size == 1)
    assert(resultList.head.name == "haha")
    //-------------------pageAll--------------------------------------------
    model.id = null
    TestJDBCSyncService._save(model, request)
    model.id = null
    TestJDBCSyncService._save(model, request)
    model.id = null
    TestJDBCSyncService._save(model, request)
    model.id = null
    model.name = "last"
    TestJDBCSyncService._save(model, request)
    var resultPage =TestJDBCSyncService._pageAll(2, 2, request).body
    assert(resultPage.getPageNumber == 2)
    assert(resultPage.getPageSize == 2)
    assert(resultPage.getPageTotal == 3)
    assert(resultPage.getRecordTotal == 5)
    //-------------------pageByCondition--------------------------------------------
    resultPage = TestJDBCSyncService._pageByCondition("name = '%s' ORDER BY create_time desc".format("haha"), 1, 3, request).body
    assert(resultPage.getPageNumber == 1)
    assert(resultPage.getPageSize == 3)
    assert(resultPage.getPageTotal == 2)
    assert(resultPage.getRecordTotal == 4)
    //-------------------deleteById--------------------------------------------
    TestJDBCSyncService._deleteById(resultPage.results.last.id, request)
    resultList = TestJDBCSyncService._findByCondition("id='%s'".format(resultPage.results.head.id), request).body
    assert(resultList.size == 1)
    resultList = TestJDBCSyncService._findByCondition("id='%s'".format(resultPage.results.last.id), request).body
    assert(resultList.size == 0)
    //-------------------deleteAll--------------------------------------------
    TestJDBCSyncService._deleteAll(request)
    TestJDBCSyncService._findAll(request)
    assert(resultList.size == 0)
  }

  test("VO测试") {

    val request = SReq("0000", "jzy")

    //-------------------save--------------------------------------------
    val vo = TestVO()
    vo.name = "张三"
    vo.bool = true
    vo.age = 14
    vo.id = "id001"
    TestJDBCVOService._save(vo, request)
    assert(TestJDBCVOService._save(vo, request).message == "Id exist :id001")
    var resultSingle = TestJDBCVOService._getById("id001", request).body
    assert(resultSingle.name == "张三")
    assert(resultSingle.bool)
    //-------------------update--------------------------------------------
    vo.name = "haha"
    vo.bool = false
    TestJDBCVOService._update("id001", vo, request)
    resultSingle = TestJDBCVOService._getById("id001", request).body
    assert(resultSingle.name == "haha")
    assert(!resultSingle.bool)
    //-------------------getByCondition--------------------------------------------
    resultSingle = TestJDBCVOService._getByCondition("id='%s' AND name='%s'".format("id001", "haha"), request).body
    assert(resultSingle.name == "haha")
    //-------------------findAll--------------------------------------------
    var resultList = TestJDBCVOService._findAll(request).body
    assert(resultList.size == 1)
    assert(resultList.head.name == "haha")
    //-------------------pageAll--------------------------------------------
    vo.id = null
    TestJDBCVOService._save(vo, request)
    vo.id = null
    TestJDBCVOService._save(vo, request)
    vo.id = null
    TestJDBCVOService._save(vo, request)
    vo.id = null
    vo.name = "last"
    TestJDBCVOService._save(vo, request)
    var resultPage =TestJDBCVOService._pageAll(2, 2, request).body
    assert(resultPage.getPageNumber == 2)
    assert(resultPage.getPageSize == 2)
    assert(resultPage.getPageTotal == 3)
    assert(resultPage.getRecordTotal == 5)
    //-------------------pageByCondition--------------------------------------------
    resultPage = TestJDBCVOService._pageByCondition("name = '%s' ORDER BY create_time desc".format("haha"), 1, 3, request).body
    assert(resultPage.getPageNumber == 1)
    assert(resultPage.getPageSize == 3)
    assert(resultPage.getPageTotal == 2)
    assert(resultPage.getRecordTotal == 4)
    //-------------------deleteById--------------------------------------------
    TestJDBCVOService._deleteById(resultPage.results.last.id, request)
    resultList = TestJDBCVOService._findByCondition("id='%s'".format(resultPage.results.head.id), request).body
    assert(resultList.size == 1)
    resultList = TestJDBCVOService._findByCondition("id='%s'".format(resultPage.results.last.id), request).body
    assert(resultList.size == 0)
    //-------------------deleteAll--------------------------------------------
    TestJDBCVOService._deleteAll(request)
    TestJDBCVOService._findAll(request)
    assert(resultList.size == 0)
  }

}

object TestJDBCService extends JDBCService[TestModel] with FutureService[TestModel]
object TestJDBCSyncService extends JDBCService[TestModel] with SyncService[TestModel]
object TestJDBCVOService extends JDBCService[TestModel] with SyncVOService[TestModel,TestVO]



