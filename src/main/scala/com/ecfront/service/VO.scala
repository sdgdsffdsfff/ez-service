package com.ecfront.service

import scala.beans.BeanProperty

abstract class IdVO {
  @BeanProperty var id: String = _
}

abstract class SecureVO extends IdVO {
  @BeanProperty var create_user: String = _
  @BeanProperty var create_time: Long = _
  @BeanProperty var update_user: String = _
  @BeanProperty var update_time: Long = _
}





