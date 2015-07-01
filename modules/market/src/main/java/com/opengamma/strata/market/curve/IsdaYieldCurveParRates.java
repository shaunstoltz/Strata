/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.curve;

import java.io.Serializable;
import java.time.Period;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableValidator;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.finance.credit.type.IsdaYieldCurveConvention;

/**
 * The par rates used when calibrating an ISDA yield curve.
 */
@BeanDefinition(builderScope = "private")
public final class IsdaYieldCurveParRates
    implements ImmutableBean, Serializable {

  /**
   * The curve name.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurveName name;
  /**
   * The tenor at each curve node.
   */
  @PropertyDefinition(validate = "notNull")
  private final Period[] yieldCurvePoints;
  /**
   * The instrument type at each curve node.
   */
  @PropertyDefinition(validate = "notNull")
  private final IsdaYieldCurveUnderlyingType[] yieldCurveInstruments;
  /**
   * The par rate at each curve node.
   */
  @PropertyDefinition(validate = "notNull")
  private final double[] parRates;
  /**
   * The underlying convention.
   */
  @PropertyDefinition(validate = "notNull")
  private final IsdaYieldCurveConvention curveConvention;

  //-------------------------------------------------------------------------
  /**
   * Creates an instance of the par rates.
   * 
   * @param name  the curve name
   * @param yieldCurvePoints  the tenor at each curve node
   * @param yieldCurveInstruments  the instrument type at each curve node
   * @param parRates  the par rate at each curve node
   * @param curveConvention  the underlying convention
   * @return the par rates
   */
  public static IsdaYieldCurveParRates of(
      CurveName name,
      Period[] yieldCurvePoints,
      IsdaYieldCurveUnderlyingType[] yieldCurveInstruments,
      double[] parRates,
      IsdaYieldCurveConvention curveConvention) {

    return new IsdaYieldCurveParRates(
        name,
        yieldCurvePoints,
        yieldCurveInstruments,
        parRates,
        curveConvention);
  }

  @ImmutableValidator
  private void validate() {
    if (yieldCurvePoints.length <= 0) {
      throw new IllegalArgumentException("Cannot have zero points");
    }
    if (yieldCurvePoints.length != yieldCurveInstruments.length || yieldCurvePoints.length != parRates.length) {
      throw new IllegalArgumentException("Points do not line up");
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Applies a parallel shift to all the nodes.
   * 
   * @param shift  the shift to apply
   * @return the bumped instance
   */
  public IsdaYieldCurveParRates parallelShiftParRatesinBps(double shift) {
    double[] shiftedRates = parRates.clone();
    for (int i = 0; i < shiftedRates.length; i++) {
      shiftedRates[i] = shiftedRates[i] + shift;
    }
    return applyShift(shiftedRates);
  }

  /**
   * Applies a bucketed shift to a single node.
   * 
   * @param index  the index of the node to shift
   * @param shift  the shift to apply
   * @return the bumped instance
   */
  public IsdaYieldCurveParRates bucketedShiftParRatesinBps(int index, double shift) {
    double[] shiftedRates = parRates.clone();
    shiftedRates[index] = shiftedRates[index] + shift;
    return applyShift(shiftedRates);
  }

  /**
   * Gets the number of nodes.
   * 
   * @return the number of points
   */
  public int getNumberOfPoints() {
    return yieldCurvePoints.length;
  }

  // applies the shift
  private IsdaYieldCurveParRates applyShift(double[] shiftedRates) {
    return IsdaYieldCurveParRates.of(
        name,
        yieldCurvePoints.clone(),
        yieldCurveInstruments.clone(),
        shiftedRates,
        curveConvention
        );
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code IsdaYieldCurveParRates}.
   * @return the meta-bean, not null
   */
  public static IsdaYieldCurveParRates.Meta meta() {
    return IsdaYieldCurveParRates.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(IsdaYieldCurveParRates.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private IsdaYieldCurveParRates(
      CurveName name,
      Period[] yieldCurvePoints,
      IsdaYieldCurveUnderlyingType[] yieldCurveInstruments,
      double[] parRates,
      IsdaYieldCurveConvention curveConvention) {
    JodaBeanUtils.notNull(name, "name");
    JodaBeanUtils.notNull(yieldCurvePoints, "yieldCurvePoints");
    JodaBeanUtils.notNull(yieldCurveInstruments, "yieldCurveInstruments");
    JodaBeanUtils.notNull(parRates, "parRates");
    JodaBeanUtils.notNull(curveConvention, "curveConvention");
    this.name = name;
    this.yieldCurvePoints = yieldCurvePoints;
    this.yieldCurveInstruments = yieldCurveInstruments;
    this.parRates = parRates.clone();
    this.curveConvention = curveConvention;
    validate();
  }

  @Override
  public IsdaYieldCurveParRates.Meta metaBean() {
    return IsdaYieldCurveParRates.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the curve name.
   * @return the value of the property, not null
   */
  public CurveName getName() {
    return name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the tenor at each curve node.
   * @return the value of the property, not null
   */
  public Period[] getYieldCurvePoints() {
    return yieldCurvePoints;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instrument type at each curve node.
   * @return the value of the property, not null
   */
  public IsdaYieldCurveUnderlyingType[] getYieldCurveInstruments() {
    return yieldCurveInstruments;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the par rate at each curve node.
   * @return the value of the property, not null
   */
  public double[] getParRates() {
    return (parRates != null ? parRates.clone() : null);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying convention.
   * @return the value of the property, not null
   */
  public IsdaYieldCurveConvention getCurveConvention() {
    return curveConvention;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      IsdaYieldCurveParRates other = (IsdaYieldCurveParRates) obj;
      return JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getYieldCurvePoints(), other.getYieldCurvePoints()) &&
          JodaBeanUtils.equal(getYieldCurveInstruments(), other.getYieldCurveInstruments()) &&
          JodaBeanUtils.equal(getParRates(), other.getParRates()) &&
          JodaBeanUtils.equal(getCurveConvention(), other.getCurveConvention());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getYieldCurvePoints());
    hash = hash * 31 + JodaBeanUtils.hashCode(getYieldCurveInstruments());
    hash = hash * 31 + JodaBeanUtils.hashCode(getParRates());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurveConvention());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("IsdaYieldCurveParRates{");
    buf.append("name").append('=').append(getName()).append(',').append(' ');
    buf.append("yieldCurvePoints").append('=').append(getYieldCurvePoints()).append(',').append(' ');
    buf.append("yieldCurveInstruments").append('=').append(getYieldCurveInstruments()).append(',').append(' ');
    buf.append("parRates").append('=').append(getParRates()).append(',').append(' ');
    buf.append("curveConvention").append('=').append(JodaBeanUtils.toString(getCurveConvention()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code IsdaYieldCurveParRates}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<CurveName> name = DirectMetaProperty.ofImmutable(
        this, "name", IsdaYieldCurveParRates.class, CurveName.class);
    /**
     * The meta-property for the {@code yieldCurvePoints} property.
     */
    private final MetaProperty<Period[]> yieldCurvePoints = DirectMetaProperty.ofImmutable(
        this, "yieldCurvePoints", IsdaYieldCurveParRates.class, Period[].class);
    /**
     * The meta-property for the {@code yieldCurveInstruments} property.
     */
    private final MetaProperty<IsdaYieldCurveUnderlyingType[]> yieldCurveInstruments = DirectMetaProperty.ofImmutable(
        this, "yieldCurveInstruments", IsdaYieldCurveParRates.class, IsdaYieldCurveUnderlyingType[].class);
    /**
     * The meta-property for the {@code parRates} property.
     */
    private final MetaProperty<double[]> parRates = DirectMetaProperty.ofImmutable(
        this, "parRates", IsdaYieldCurveParRates.class, double[].class);
    /**
     * The meta-property for the {@code curveConvention} property.
     */
    private final MetaProperty<IsdaYieldCurveConvention> curveConvention = DirectMetaProperty.ofImmutable(
        this, "curveConvention", IsdaYieldCurveParRates.class, IsdaYieldCurveConvention.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name",
        "yieldCurvePoints",
        "yieldCurveInstruments",
        "parRates",
        "curveConvention");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case 695376101:  // yieldCurvePoints
          return yieldCurvePoints;
        case -1469575510:  // yieldCurveInstruments
          return yieldCurveInstruments;
        case 1157229426:  // parRates
          return parRates;
        case 1796217280:  // curveConvention
          return curveConvention;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends IsdaYieldCurveParRates> builder() {
      return new IsdaYieldCurveParRates.Builder();
    }

    @Override
    public Class<? extends IsdaYieldCurveParRates> beanType() {
      return IsdaYieldCurveParRates.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurveName> name() {
      return name;
    }

    /**
     * The meta-property for the {@code yieldCurvePoints} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Period[]> yieldCurvePoints() {
      return yieldCurvePoints;
    }

    /**
     * The meta-property for the {@code yieldCurveInstruments} property.
     * @return the meta-property, not null
     */
    public MetaProperty<IsdaYieldCurveUnderlyingType[]> yieldCurveInstruments() {
      return yieldCurveInstruments;
    }

    /**
     * The meta-property for the {@code parRates} property.
     * @return the meta-property, not null
     */
    public MetaProperty<double[]> parRates() {
      return parRates;
    }

    /**
     * The meta-property for the {@code curveConvention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<IsdaYieldCurveConvention> curveConvention() {
      return curveConvention;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((IsdaYieldCurveParRates) bean).getName();
        case 695376101:  // yieldCurvePoints
          return ((IsdaYieldCurveParRates) bean).getYieldCurvePoints();
        case -1469575510:  // yieldCurveInstruments
          return ((IsdaYieldCurveParRates) bean).getYieldCurveInstruments();
        case 1157229426:  // parRates
          return ((IsdaYieldCurveParRates) bean).getParRates();
        case 1796217280:  // curveConvention
          return ((IsdaYieldCurveParRates) bean).getCurveConvention();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code IsdaYieldCurveParRates}.
   */
  private static final class Builder extends DirectFieldsBeanBuilder<IsdaYieldCurveParRates> {

    private CurveName name;
    private Period[] yieldCurvePoints;
    private IsdaYieldCurveUnderlyingType[] yieldCurveInstruments;
    private double[] parRates;
    private IsdaYieldCurveConvention curveConvention;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case 695376101:  // yieldCurvePoints
          return yieldCurvePoints;
        case -1469575510:  // yieldCurveInstruments
          return yieldCurveInstruments;
        case 1157229426:  // parRates
          return parRates;
        case 1796217280:  // curveConvention
          return curveConvention;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this.name = (CurveName) newValue;
          break;
        case 695376101:  // yieldCurvePoints
          this.yieldCurvePoints = (Period[]) newValue;
          break;
        case -1469575510:  // yieldCurveInstruments
          this.yieldCurveInstruments = (IsdaYieldCurveUnderlyingType[]) newValue;
          break;
        case 1157229426:  // parRates
          this.parRates = (double[]) newValue;
          break;
        case 1796217280:  // curveConvention
          this.curveConvention = (IsdaYieldCurveConvention) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public IsdaYieldCurveParRates build() {
      return new IsdaYieldCurveParRates(
          name,
          yieldCurvePoints,
          yieldCurveInstruments,
          parRates,
          curveConvention);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("IsdaYieldCurveParRates.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(name)).append(',').append(' ');
      buf.append("yieldCurvePoints").append('=').append(JodaBeanUtils.toString(yieldCurvePoints)).append(',').append(' ');
      buf.append("yieldCurveInstruments").append('=').append(JodaBeanUtils.toString(yieldCurveInstruments)).append(',').append(' ');
      buf.append("parRates").append('=').append(JodaBeanUtils.toString(parRates)).append(',').append(' ');
      buf.append("curveConvention").append('=').append(JodaBeanUtils.toString(curveConvention));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
