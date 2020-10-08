/*
 * Copyright 2020 Yu Liebing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package com.github.tm.stindex.persist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.github.tm.stindex.SPIServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistableFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(PersistableFactory.class);

  private final Map<Class<Persistable>, Short> classRegistry;

  private final Map<Short, Supplier<Persistable>> constructorRegistry;
  private static PersistableFactory singletonInstance = null;

  public static synchronized PersistableFactory getInstance() {
    if (singletonInstance == null) {
      final PersistableFactory internalFactory = new PersistableFactory();
      final Iterator<PersistableRegistrySpi> persistableRegistries =
          new SPIServiceRegistry(PersistableFactory.class).load(PersistableRegistrySpi.class);
      while (persistableRegistries.hasNext()) {
        final PersistableRegistrySpi persistableRegistry = persistableRegistries.next();
        if (persistableRegistry != null) {
          internalFactory.addRegistry(persistableRegistry);
        }
      }
      singletonInstance = internalFactory;
    }
    return singletonInstance;
  }

  private PersistableFactory() {
    classRegistry = new HashMap<>();
    constructorRegistry = new HashMap<>();
  }

  protected void addRegistry(final PersistableRegistrySpi registry) {
    final PersistableRegistrySpi.PersistableIdAndConstructor[] persistables = registry.getSupportedPersistables();
    for (final PersistableRegistrySpi.PersistableIdAndConstructor p : persistables) {
      addPersistableType(p.getPersistableId(), p.getPersistableConstructor());
    }
  }

  protected void addPersistableType(
      final short persistableId,
      final Supplier<Persistable> constructor) {
    final Class persistableClass = constructor.get().getClass();
    if (classRegistry.containsKey(persistableClass)) {
      LOGGER.error(
          "'"
              + persistableClass.getCanonicalName()
              + "' already registered with id '"
              + classRegistry.get(persistableClass)
              + "'.  Cannot register '"
              + persistableClass
              + "' with id '"
              + persistableId
              + "'");
      return;
    }
    if (constructorRegistry.containsKey(persistableId)) {
      String currentClass = "unknown";

      for (final Entry<Class<Persistable>, Short> e : classRegistry.entrySet()) {
        if (persistableId == e.getValue().shortValue()) {
          currentClass = e.getKey().getCanonicalName();
          break;
        }
      }
      LOGGER.error(
          "'"
              + persistableId
              + "' already registered for class '"
              + (currentClass)
              + "'.  Cannot register '"
              + persistableClass
              + "' with id '"
              + persistableId
              + "'");
      return;
    }
    classRegistry.put(persistableClass, persistableId);
    constructorRegistry.put(persistableId, constructor);
  }

  public Persistable newInstance(final short id) {
    final Supplier<Persistable> constructor = constructorRegistry.get(id);
    if (constructor != null) {
      return constructor.get();
    }
    return null;
  }

  public Map<Class<Persistable>, Short> getClassIdMapping() {
    return classRegistry;
  }
}
