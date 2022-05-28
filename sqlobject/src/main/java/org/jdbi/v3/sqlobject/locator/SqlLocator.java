/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.sqlobject.locator;

import java.lang.reflect.Method;

import org.jdbi.v3.core.config.ConfigRegistry;

/**
 * Locates SQL for jdbi SQL Object methods.
 */
@FunctionalInterface
public interface SqlLocator {
    /**
     * Locates and returns the SQL for the given SQL Object type and method.
     *
     * @param sqlObjectType the SQL object type
     * @param method the method
     * @param config the config registry
     * @return the SQL for the given method.
     * @deprecated use {@link #locate(Class, Object, Method, ConfigRegistry)}
     */
    @Deprecated
    String locate(Class<?> sqlObjectType, Method method, ConfigRegistry config);

    /**
     * Locates and returns the SQL for the given SQL Object type, target and method.
     *
     * @param sqlObjectType the SQL object type
     * @param target the target
     * @param method the method
     * @param config the config registry
     * @return the SQL for the given method.
     */
    default String locate(Class<?> sqlObjectType, Object target, Method method, ConfigRegistry config) {
        return locate(sqlObjectType, method, config);
    }
}
