package org.jooq.scala.test.pojo

import java.beans.ConstructorProperties

/**
 * Case classes are in fact classes with a public constructor, which can be annotated using {@link ConstructorProperties}
 * @author Lukas Eder
 */
case class BookCaseWithConstructorProperties @ConstructorProperties(Array("id", "authorId", "title")) (
  id: Int,
  authorId: Int,
  title: String)