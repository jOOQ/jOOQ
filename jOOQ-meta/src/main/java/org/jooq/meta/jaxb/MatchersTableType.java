
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Declarative naming strategy configuration for table names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersTableType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersTableType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32009L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule tableClass;
    protected MatcherRule tableIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableImplements;
    protected MatcherRule pathClass;
    protected MatcherRule pathExtends;
    protected MatcherRule pathImplements;
    protected MatcherRule recordClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordImplements;
    protected MatcherRule interfaceClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String interfaceImplements;
    protected MatcherRule daoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String daoExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String daoImplements;
    protected MatcherRule pojoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoImplements;

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} object.
     * 
     */
    public MatcherRule getTableClass() {
        return tableClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} object.
     * 
     */
    public void setTableClass(MatcherRule value) {
        this.tableClass = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} identifier.
     * 
     */
    public MatcherRule getTableIdentifier() {
        return tableIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} identifier.
     * 
     */
    public void setTableIdentifier(MatcherRule value) {
        this.tableIdentifier = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Table} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.TableImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getTableExtends() {
        return tableExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Table} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.TableImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setTableExtends(String value) {
        this.tableExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} should implement.
     * 
     */
    public String getTableImplements() {
        return tableImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} should implement.
     * 
     */
    public void setTableImplements(String value) {
        this.tableImplements = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} and {@link org.jooq.Path} object.
     * 
     */
    public MatcherRule getPathClass() {
        return pathClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} and {@link org.jooq.Path} object.
     * 
     */
    public void setPathClass(MatcherRule value) {
        this.pathClass = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should extend.
     * 
     */
    public MatcherRule getPathExtends() {
        return pathExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should extend.
     * 
     */
    public void setPathExtends(MatcherRule value) {
        this.pathExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should implement.
     * 
     */
    public MatcherRule getPathImplements() {
        return pathImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should implement.
     * 
     */
    public void setPathImplements(MatcherRule value) {
        this.pathImplements = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.TableRecord} object.
     * 
     */
    public MatcherRule getRecordClass() {
        return recordClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.TableRecord} object.
     * 
     */
    public void setRecordClass(MatcherRule value) {
        this.recordClass = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.TableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.TableRecord} does, so to minimise
     * unexpected behaviour, custom table record super classes should extend {@link org.jooq.impl.TableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getRecordExtends() {
        return recordExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.TableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.TableRecord} does, so to minimise
     * unexpected behaviour, custom table record super classes should extend {@link org.jooq.impl.TableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setRecordExtends(String value) {
        this.recordExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.TableRecord} should implement.
     * 
     */
    public String getRecordImplements() {
        return recordImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.TableRecord} should implement.
     * 
     */
    public void setRecordImplements(String value) {
        this.recordImplements = value;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.TableRecord} and/or the POJO.
     * 
     */
    public MatcherRule getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.TableRecord} and/or the POJO.
     * 
     */
    public void setInterfaceClass(MatcherRule value) {
        this.interfaceClass = value;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.TableRecord} and/or POJO) should implement.
     * 
     */
    public String getInterfaceImplements() {
        return interfaceImplements;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.TableRecord} and/or POJO) should implement.
     * 
     */
    public void setInterfaceImplements(String value) {
        this.interfaceImplements = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.DAO} object.
     * 
     */
    public MatcherRule getDaoClass() {
        return daoClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.DAO} object.
     * 
     */
    public void setDaoClass(MatcherRule value) {
        this.daoClass = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.DAO} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.DAO} does, so to minimise
     * unexpected behaviour, custom DAO super classes should extend {@link org.jooq.impl.DAOImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getDaoExtends() {
        return daoExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.DAO} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.DAO} does, so to minimise
     * unexpected behaviour, custom DAO super classes should extend {@link org.jooq.impl.DAOImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setDaoExtends(String value) {
        this.daoExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.DAO} should implement.
     * 
     */
    public String getDaoImplements() {
        return daoImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.DAO} should implement.
     * 
     */
    public void setDaoImplements(String value) {
        this.daoImplements = value;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatcherRule getPojoClass() {
        return pojoClass;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public void setPojoClass(MatcherRule value) {
        this.pojoClass = value;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public String getPojoExtends() {
        return pojoExtends;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public void setPojoExtends(String value) {
        this.pojoExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public String getPojoImplements() {
        return pojoImplements;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public void setPojoImplements(String value) {
        this.pojoImplements = value;
    }

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public MatchersTableType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} object.
     * 
     */
    public MatchersTableType withTableClass(MatcherRule value) {
        setTableClass(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} identifier.
     * 
     */
    public MatchersTableType withTableIdentifier(MatcherRule value) {
        setTableIdentifier(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Table} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.TableImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersTableType withTableExtends(String value) {
        setTableExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} should implement.
     * 
     */
    public MatchersTableType withTableImplements(String value) {
        setTableImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Table} and {@link org.jooq.Path} object.
     * 
     */
    public MatchersTableType withPathClass(MatcherRule value) {
        setPathClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should extend.
     * 
     */
    public MatchersTableType withPathExtends(MatcherRule value) {
        setPathExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Table} and {@link org.jooq.Path} object should implement.
     * 
     */
    public MatchersTableType withPathImplements(MatcherRule value) {
        setPathImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.TableRecord} object.
     * 
     */
    public MatchersTableType withRecordClass(MatcherRule value) {
        setRecordClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.TableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.TableRecord} does, so to minimise
     * unexpected behaviour, custom table record super classes should extend {@link org.jooq.impl.TableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersTableType withRecordExtends(String value) {
        setRecordExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.TableRecord} should implement.
     * 
     */
    public MatchersTableType withRecordImplements(String value) {
        setRecordImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.TableRecord} and/or the POJO.
     * 
     */
    public MatchersTableType withInterfaceClass(MatcherRule value) {
        setInterfaceClass(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.TableRecord} and/or POJO) should implement.
     * 
     */
    public MatchersTableType withInterfaceImplements(String value) {
        setInterfaceImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.DAO} object.
     * 
     */
    public MatchersTableType withDaoClass(MatcherRule value) {
        setDaoClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.DAO} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.DAO} does, so to minimise
     * unexpected behaviour, custom DAO super classes should extend {@link org.jooq.impl.DAOImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersTableType withDaoExtends(String value) {
        setDaoExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.DAO} should implement.
     * 
     */
    public MatchersTableType withDaoImplements(String value) {
        setDaoImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatchersTableType withPojoClass(MatcherRule value) {
        setPojoClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public MatchersTableType withPojoExtends(String value) {
        setPojoExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public MatchersTableType withPojoImplements(String value) {
        setPojoImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("tableClass", tableClass);
        builder.append("tableIdentifier", tableIdentifier);
        builder.append("tableExtends", tableExtends);
        builder.append("tableImplements", tableImplements);
        builder.append("pathClass", pathClass);
        builder.append("pathExtends", pathExtends);
        builder.append("pathImplements", pathImplements);
        builder.append("recordClass", recordClass);
        builder.append("recordExtends", recordExtends);
        builder.append("recordImplements", recordImplements);
        builder.append("interfaceClass", interfaceClass);
        builder.append("interfaceImplements", interfaceImplements);
        builder.append("daoClass", daoClass);
        builder.append("daoExtends", daoExtends);
        builder.append("daoImplements", daoImplements);
        builder.append("pojoClass", pojoClass);
        builder.append("pojoExtends", pojoExtends);
        builder.append("pojoImplements", pojoImplements);
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
        MatchersTableType other = ((MatchersTableType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (tableClass == null) {
            if (other.tableClass!= null) {
                return false;
            }
        } else {
            if (!tableClass.equals(other.tableClass)) {
                return false;
            }
        }
        if (tableIdentifier == null) {
            if (other.tableIdentifier!= null) {
                return false;
            }
        } else {
            if (!tableIdentifier.equals(other.tableIdentifier)) {
                return false;
            }
        }
        if (tableExtends == null) {
            if (other.tableExtends!= null) {
                return false;
            }
        } else {
            if (!tableExtends.equals(other.tableExtends)) {
                return false;
            }
        }
        if (tableImplements == null) {
            if (other.tableImplements!= null) {
                return false;
            }
        } else {
            if (!tableImplements.equals(other.tableImplements)) {
                return false;
            }
        }
        if (pathClass == null) {
            if (other.pathClass!= null) {
                return false;
            }
        } else {
            if (!pathClass.equals(other.pathClass)) {
                return false;
            }
        }
        if (pathExtends == null) {
            if (other.pathExtends!= null) {
                return false;
            }
        } else {
            if (!pathExtends.equals(other.pathExtends)) {
                return false;
            }
        }
        if (pathImplements == null) {
            if (other.pathImplements!= null) {
                return false;
            }
        } else {
            if (!pathImplements.equals(other.pathImplements)) {
                return false;
            }
        }
        if (recordClass == null) {
            if (other.recordClass!= null) {
                return false;
            }
        } else {
            if (!recordClass.equals(other.recordClass)) {
                return false;
            }
        }
        if (recordExtends == null) {
            if (other.recordExtends!= null) {
                return false;
            }
        } else {
            if (!recordExtends.equals(other.recordExtends)) {
                return false;
            }
        }
        if (recordImplements == null) {
            if (other.recordImplements!= null) {
                return false;
            }
        } else {
            if (!recordImplements.equals(other.recordImplements)) {
                return false;
            }
        }
        if (interfaceClass == null) {
            if (other.interfaceClass!= null) {
                return false;
            }
        } else {
            if (!interfaceClass.equals(other.interfaceClass)) {
                return false;
            }
        }
        if (interfaceImplements == null) {
            if (other.interfaceImplements!= null) {
                return false;
            }
        } else {
            if (!interfaceImplements.equals(other.interfaceImplements)) {
                return false;
            }
        }
        if (daoClass == null) {
            if (other.daoClass!= null) {
                return false;
            }
        } else {
            if (!daoClass.equals(other.daoClass)) {
                return false;
            }
        }
        if (daoExtends == null) {
            if (other.daoExtends!= null) {
                return false;
            }
        } else {
            if (!daoExtends.equals(other.daoExtends)) {
                return false;
            }
        }
        if (daoImplements == null) {
            if (other.daoImplements!= null) {
                return false;
            }
        } else {
            if (!daoImplements.equals(other.daoImplements)) {
                return false;
            }
        }
        if (pojoClass == null) {
            if (other.pojoClass!= null) {
                return false;
            }
        } else {
            if (!pojoClass.equals(other.pojoClass)) {
                return false;
            }
        }
        if (pojoExtends == null) {
            if (other.pojoExtends!= null) {
                return false;
            }
        } else {
            if (!pojoExtends.equals(other.pojoExtends)) {
                return false;
            }
        }
        if (pojoImplements == null) {
            if (other.pojoImplements!= null) {
                return false;
            }
        } else {
            if (!pojoImplements.equals(other.pojoImplements)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((tableClass == null)? 0 :tableClass.hashCode()));
        result = ((prime*result)+((tableIdentifier == null)? 0 :tableIdentifier.hashCode()));
        result = ((prime*result)+((tableExtends == null)? 0 :tableExtends.hashCode()));
        result = ((prime*result)+((tableImplements == null)? 0 :tableImplements.hashCode()));
        result = ((prime*result)+((pathClass == null)? 0 :pathClass.hashCode()));
        result = ((prime*result)+((pathExtends == null)? 0 :pathExtends.hashCode()));
        result = ((prime*result)+((pathImplements == null)? 0 :pathImplements.hashCode()));
        result = ((prime*result)+((recordClass == null)? 0 :recordClass.hashCode()));
        result = ((prime*result)+((recordExtends == null)? 0 :recordExtends.hashCode()));
        result = ((prime*result)+((recordImplements == null)? 0 :recordImplements.hashCode()));
        result = ((prime*result)+((interfaceClass == null)? 0 :interfaceClass.hashCode()));
        result = ((prime*result)+((interfaceImplements == null)? 0 :interfaceImplements.hashCode()));
        result = ((prime*result)+((daoClass == null)? 0 :daoClass.hashCode()));
        result = ((prime*result)+((daoExtends == null)? 0 :daoExtends.hashCode()));
        result = ((prime*result)+((daoImplements == null)? 0 :daoImplements.hashCode()));
        result = ((prime*result)+((pojoClass == null)? 0 :pojoClass.hashCode()));
        result = ((prime*result)+((pojoExtends == null)? 0 :pojoExtends.hashCode()));
        result = ((prime*result)+((pojoImplements == null)? 0 :pojoImplements.hashCode()));
        return result;
    }

}
