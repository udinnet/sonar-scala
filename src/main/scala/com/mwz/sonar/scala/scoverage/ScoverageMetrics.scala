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
package com.mwz.sonar.scala.scoverage

import org.sonar.api.measures.{CoreMetrics, Metric, Metrics}
import scala.collection.JavaConverters._

/** Statement coverage metric definition. */
class ScoverageMetrics extends Metrics {
  override def getMetrics: java.util.List[Metric[_ <: java.io.Serializable]] =
    List[Metric[_ <: java.io.Serializable]](
      ScoverageMetrics.totalStatements,
      ScoverageMetrics.coveredStatements,
      ScoverageMetrics.statementCoverage,
      ScoverageMetrics.branchCoverage
    ).asJava
}

object ScoverageMetrics {

  /** Builds a new [[Metric]] */
  private def buildMetric[T <: java.io.Serializable](
    metricKey: String,
    metricName: String,
    metricType: Metric.ValueType,
    metricDescription: String,
    metricDirection: java.lang.Integer,
    metricDomain: String,
    isMetricQualitative: Boolean           = false,
    metricValues: Option[(Double, Double)] = None
  ): Metric[T] = {
    val metricBuilder = new Metric.Builder(metricKey, metricName, metricType)
      .setDescription(metricDescription)
      .setDirection(metricDirection)
      .setDomain(metricDomain)
      .setQualitative(isMetricQualitative)

    metricValues match {
      case Some((worstValue, bestValue)) =>
        metricBuilder
          .setWorstValue(worstValue)
          .setBestValue(bestValue)
          .create[T]()
      case None => metricBuilder.create[T]()
    }
  }

  val totalStatements: Metric[java.lang.Integer] =
    buildMetric(
      metricKey = "total_statements",
      metricName = "Total statements",
      metricType = Metric.ValueType.INT,
      metricDescription = "Number of all statements",
      metricDirection = Metric.DIRECTION_BETTER,
      metricDomain = CoreMetrics.DOMAIN_SIZE,
    )

  val coveredStatements: Metric[java.lang.Integer] =
    buildMetric(
      metricKey = "covered_statements",
      metricName = "Covered statements",
      metricType = Metric.ValueType.INT,
      metricDescription = "Number of statements covered by tests",
      metricDirection = Metric.DIRECTION_BETTER,
      metricDomain = CoreMetrics.DOMAIN_SIZE,
    )

  val statementCoverage: Metric[java.lang.Double] =
    buildMetric(
      metricKey = "scoverage",
      metricName = "Statement coverage",
      metricType = Metric.ValueType.PERCENT,
      metricDescription = "Percentage of statements covered by tests",
      metricDirection = Metric.DIRECTION_BETTER,
      metricDomain = CoreMetrics.DOMAIN_COVERAGE,
      isMetricQualitative = true,
      metricValues = Some((0.0d, 100.0d))
    )

  val branchCoverage: Metric[java.lang.Double] =
    buildMetric(
      metricKey = "branch_scoverage",
      metricName = "Branch coverage (reported by scoverage)",
      metricType = Metric.ValueType.PERCENT,
      metricDescription = "Percentage of branches covered by tests",
      metricDirection = Metric.DIRECTION_BETTER,
      metricDomain = CoreMetrics.DOMAIN_COVERAGE,
      isMetricQualitative = true,
      metricValues = Some((0.0d, 100.0d))
    )
}
