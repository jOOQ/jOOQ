/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.example.guice;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * The module is used to programmatically configure all Guice annotation
 * interceptor.
 *
 * @author Lukas Eder
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(Service.class).in(Singleton.class);

        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        requestInjection(interceptor);

        bindInterceptor(annotatedWith(Transactional.class), any(), interceptor);
        bindInterceptor(any(), annotatedWith(Transactional.class), interceptor);

        install(new DataSources());
    }
}
