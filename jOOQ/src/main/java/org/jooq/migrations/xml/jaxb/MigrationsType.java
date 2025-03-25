
package org.jooq.migrations.xml.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for MigrationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MigrationsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="commits" type="{http://www.jooq.org/xsd/jooq-migrations-3.20.0.xsd}CommitsType" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MigrationsType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MigrationsType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32000L;
    @XmlElementWrapper(name = "commits")
    @XmlElement(name = "commit")
    protected List<CommitType> commits;

    public List<CommitType> getCommits() {
        if (commits == null) {
            commits = new ArrayList<CommitType>();
        }
        return commits;
    }

    public void setCommits(List<CommitType> commits) {
        this.commits = commits;
    }

    public MigrationsType withCommits(CommitType... values) {
        if (values!= null) {
            for (CommitType value: values) {
                getCommits().add(value);
            }
        }
        return this;
    }

    public MigrationsType withCommits(Collection<CommitType> values) {
        if (values!= null) {
            getCommits().addAll(values);
        }
        return this;
    }

    public MigrationsType withCommits(List<CommitType> commits) {
        setCommits(commits);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("commits", "commit", commits);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        MigrationsType other = ((MigrationsType) that);
        if ((commits == null)||commits.isEmpty()) {
            if ((other.commits!= null)&&(!other.commits.isEmpty())) {
                return false;
            }
        } else {
            if (!commits.equals(other.commits)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+(((commits == null)||commits.isEmpty())? 0 :commits.hashCode()));
        return result;
    }

}
