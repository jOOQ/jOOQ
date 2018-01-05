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
 *
 *
 *
 */
package org.jooq.example.jpa.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.jooq.example.jpa.converters.YearConverter;
import org.jooq.example.jpa.embeddables.Title;

/**
 * @author Lukas Eder
 */
@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer filmId;

    // Future versions of jOOQ's code generator will be able to derive nested records
    // from JPA embedded data types, see https://github.com/jOOQ/jOOQ/issues/2360
    @Column
    @Embedded
    public Title title;

    @Column
    public int length;

    // It is easy to reuse a JPA AttributeConverter by manually generating the relevant Converters in jOOQ.
    // A future version of jOOQ may auto-discover all such JPA AttributeConverters and reuse them in jOOQ as well.
    @Column(name = "RELEASE_YEAR")
    @Convert(converter = YearConverter.class)
    public Year releaseYear;

    @ManyToOne(fetch = LAZY)
    public Language language;

    @ManyToOne(fetch = LAZY)
    public Language originalLanguage;

    @ManyToMany(fetch = LAZY, cascade = CascadeType.ALL)
    public Set<Actor> actors = new HashSet<>();

    public Film() {}

    public Film(Title title, Language language, int length, Year releaseYear) {
        this.title = title;
        this.language = language;
        this.length = length;
        this.releaseYear = releaseYear;
    }
}
