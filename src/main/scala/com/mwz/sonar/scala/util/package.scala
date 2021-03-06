/*
 * Sonar Scala Plugin
 * Copyright (C) 2018 All contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.mwz.sonar.scala

import java.util.Optional
import scala.language.implicitConversions

/**
 *  Util methods and conversions
 *
 *  @note The Scala.Option <-> Java.Optional conversions were taken from
 *  https://gist.github.com/julienroubieu/fbb7e1467ab44203a09f
 */
package object util {

  /** Conversions between Scala Option and Java 8 Optional */
  object JavaOptionals {
    implicit def toRichOption[T >: Null](opt: Option[T]): RichOption[T] = new RichOption[T](opt)
    implicit def toRichOptional[T](optional: Optional[T]): RichOptional[T] = new RichOptional[T](optional)
  }

  /** Transform this Option to an equivalent Java Optional */
  class RichOption[T >: Null](opt: Option[T]) {
    def toOptional: Optional[T] = Optional.ofNullable(opt.orNull)
  }

  /** Transform this Optional to an equivalent Scala Option */
  class RichOptional[T](opt: Optional[T]) {
    def toOption: Option[T] = if (opt.isPresent) Some(opt.get()) else None
  }

  object PathUtils {

    /** Ensures a path can safely be prepend to other path */
    def sanitizePath(path: String): String = path.lastOption match {
      case Some('/') => path
      case Some(_)   => path + '/'
      case None      => ""
    }
  }
}
