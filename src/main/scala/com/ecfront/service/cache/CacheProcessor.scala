package com.ecfront.service.cache

trait CacheProcessor[M <: AnyRef] {

  def save(id: String,model: M)

  def get(id: String): Option[M]

  def update(id: String, model: M)

  def delete(id: String)

  def deleteAll()
}
