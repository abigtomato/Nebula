package org.abigtomato.nebula.spark.scala.project.conf

import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.apache.commons.configuration2.{FileBasedConfiguration, PropertiesConfiguration}

object ConfigurationManager {

  // 创建用于初始化配置生成器实例的参数对象
  private val params = new Parameters()

  // FileBasedConfigurationBuilder：产生一个传入的类的实例对象
  // FileBasedConfiguration：融合FileBased和Configuration的接口
  // PropertiesConfiguration：从一个或多个文件读取配置的标准配置加载器
  // configure()：通过params实例初始化配置生成器
  // 向FileBasedConfigurationBuilder()中传入一个标准配置加载器，生成一个加载器类的实例对象，然后通过params参数对其初始化
  private val builder = new FileBasedConfigurationBuilder[FileBasedConfiguration](classOf[PropertiesConfiguration])
    .configure(params.properties().setFileName("commerce.properties"))

  // 通过getConfiguration获取配置对象
  val config: FileBasedConfiguration = builder.getConfiguration
}
