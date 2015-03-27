package com.ecfront.service

import com.ecfront.storage.Entity

import scala.beans.BeanProperty

@Entity(idField = "id")
abstract class IdModel {
  @BeanProperty var id: String = _
}

object IdModel {
  val ID_FLAG = "id"
}

abstract class SecureModel extends IdModel {
  @BeanProperty var create_user: String = _
  @BeanProperty var create_time: Long = _
  @BeanProperty var update_user: String = _
  @BeanProperty var update_time: Long = _
}

object SecureModel {
  val SYSTEM_USER_FLAG = "system"
}
