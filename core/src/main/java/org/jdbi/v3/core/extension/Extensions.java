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
package org.jdbi.v3.core.extension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jdbi.v3.core.config.JdbiConfig;
import org.jdbi.v3.meta.Beta;

/**
 * Configuration class for defining {@code Jdbi} extensions via {@link ExtensionFactory}
 * instances.
 */
public class Extensions implements JdbiConfig<Extensions> {
    private final List<ExtensionFactory> factories = new CopyOnWriteArrayList<>();
    private boolean allowProxy = true;

    /**
     * Create an empty {@link ExtensionFactory} configuration.
     */
    public Extensions() {}

    /**
     * Create an extension configuration by cloning another
     * @param that the configuration to clone
     */
    private Extensions(Extensions that) {
        allowProxy = that.allowProxy;
        factories.addAll(that.factories);
    }

    /**
     * Register an extension factory.
     * @param factory the factory to register
     * @return this
     */
    public Extensions register(ExtensionFactory factory) {
        factories.add(0, factory);
        return this;
    }

    /**
     * Returns true if an extension is registered for the given type.
     *
     * @param extensionType the type to query.
     * @return true if a registered extension handles the type.
     */
    public boolean hasExtensionFor(Class<?> extensionType) {
        return findFactoryFor(extensionType).isPresent();
    }

    /**
     * Create an extension instance if we have a factory that understands
     * the extension type which has access to a {@code Handle} through a {@link HandleSupplier}.
     * @param <E> the extension type to create
     * @param extensionType the extension type to create
     * @param handle the handle supplier
     * @return an attached extension instance if a factory is found
     */
    public <E> Optional<E> findFor(Class<E> extensionType, HandleSupplier handle) {
        return findFactoryFor(extensionType)
                .map(factory -> factory.attach(extensionType, handle));
    }

    private Optional<ExtensionFactory> findFactoryFor(Class<?> extensionType) {
        return factories.stream()
                .filter(factory -> factory.accepts(extensionType))
                .findFirst();
    }

    /**
     * Find the registered factory of the given type, if any
     * @param <F> the factory type to find
     * @param factoryType the factory's type to find
     * @return the found factory, if any
     */
    public <F extends ExtensionFactory> Optional<F> findFactory(Class<F> factoryType) {
        return factories.stream()
                .filter(factoryType::isInstance)
                .map(factoryType::cast)
                .findFirst();
    }

    /**
     * Allow using {@link java.lang.reflect.Proxy} to implement extensions.
     * @param allowProxy whether to allow use of Proxy types
     * @return this
     */
    @Beta
    public Extensions setAllowProxy(boolean allowProxy) {
        this.allowProxy = allowProxy;
        return this;
    }

    /**
     * Returns whether Proxy classes are allowed to be used.
     *
     * @return whether Proxy classes are allowed to be used.
     */
    @Beta
    public boolean isAllowProxy() {
        return allowProxy;
    }

    @Override
    public Extensions createCopy() {
        return new Extensions(this);
    }

    /**
     * Throw if proxy creation is disallowed.
     */
    @Beta
    public void onCreateProxy() {
        if (!isAllowProxy()) {
            throw new IllegalStateException("Creating onDemand proxy disallowed. Ensure @GenerateSqlObject annotation is being processed by `jdbi3-generator` annotation processor.");
        }
    }
}
