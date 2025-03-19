/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection.wrapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/**
 * 抽象了对象的属性信息
 * 定义了一系列查询对象信息的方法、更新属性的方法
 *
 * @author Clinton Begin
 */
public interface ObjectWrapper {

  Object get(PropertyTokenizer prop);

  void set(PropertyTokenizer prop, Object value);

  String findProperty(String name, boolean useCamelCaseMapping);

  String[] getGetterNames();

  String[] getSetterNames();

  Class<?> getSetterType(String name);

  Class<?> getGetterType(String name);

  default Entry<Type, Class<?>> getGenericSetterType(String name) {
    throw new UnsupportedOperationException(
        "'" + this.getClass() + "' must override the default method 'getGenericSetterType()'.");
  }

  default Entry<Type, Class<?>> getGenericGetterType(String name) {
    throw new UnsupportedOperationException(
        "'" + this.getClass() + "' must override the default method 'getGenericGetterType()'.");
  }

  boolean hasSetter(String name);

  boolean hasGetter(String name);

  MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

  boolean isCollection();

  void add(Object element);

  <E> void addAll(List<E> element);

}
