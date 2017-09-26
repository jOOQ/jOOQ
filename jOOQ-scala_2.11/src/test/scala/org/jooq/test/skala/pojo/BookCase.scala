package org.jooq.test.skala.pojo

import java.beans.ConstructorProperties

/**
 * Case classes are in fact classes with a public constructor
 * @author Lukas Eder
 */
case class BookCase(
  id: Int,
  authorId: Int,
  title: String)