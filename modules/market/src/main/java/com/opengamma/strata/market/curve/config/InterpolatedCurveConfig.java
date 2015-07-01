/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.curve.config;

import static com.opengamma.strata.collect.Guavate.toImmutableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.ImmutableDefaults;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.interpolator.CurveExtrapolator;
import com.opengamma.strata.basics.interpolator.CurveInterpolator;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.market.curve.CurveMetadata;
import com.opengamma.strata.market.curve.CurveName;
import com.opengamma.strata.market.curve.CurveParameterMetadata;
import com.opengamma.strata.market.curve.DefaultCurveMetadata;
import com.opengamma.strata.market.value.ValueType;

/**
 * Configuration specifying how to calibrate a curve.
 * <p>
 * This class contains a list of {@link CurveNode} instances specifying the instruments which make up the curve.
 */
@BeanDefinition
public final class InterpolatedCurveConfig
    implements CurveConfig, ImmutableBean {

  /**
   * The curve name.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final CurveName name;
  /**
   * The x-value type, providing meaning to the x-values of the curve.
   * <p>
   * This type provides meaning to the x-values. For example, the x-value might
   * represent a year fraction, as represented using {@link ValueType#YEAR_FRACTION}.
   * <p>
   * If using the builder, this defaults to {@link ValueType#UNKNOWN}.
   */
  @PropertyDefinition(validate = "notNull")
  private final ValueType xValueType;
  /**
   * The y-value type, providing meaning to the y-values of the curve.
   * <p>
   * This type provides meaning to the y-values. For example, the y-value might
   * represent a year fraction, as represented using {@link ValueType#ZERO_RATE}.
   * <p>
   * If using the builder, this defaults to {@link ValueType#UNKNOWN}.
   */
  @PropertyDefinition(validate = "notNull")
  private final ValueType yValueType;
  /**
   * The day count, optional.
   * <p>
   * If the x-value of the curve represents time as a year fraction, the day count
   * can be specified to define how the year fraction is calculated.
   */
  @PropertyDefinition(get = "optional")
  private final DayCount dayCount;
  /**
   * The nodes in the curve.
   * <p>
   * The nodes are used to find the par rates and calibrate the curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableList<CurveNode> nodes;
  /**
   * The interpolator used to find points on the curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurveInterpolator interpolator;
  /**
   * The extrapolator used to find points to the left of the leftmost point on the curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurveExtrapolator leftExtrapolator;
  /**
   * The extrapolator used to find points to the right of the rightmost point on the curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurveExtrapolator rightExtrapolator;

  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.xValueType = ValueType.UNKNOWN;
    builder.yValueType = ValueType.UNKNOWN;
  }

  // Hand-written constructor allows wildcard parameter without a wildcard in the field type
  @ImmutableConstructor
  private InterpolatedCurveConfig(
      CurveName name,
      ValueType xValueType,
      ValueType yValueType,
      DayCount dayCount,
      List<? extends CurveNode> nodes,
      CurveInterpolator interpolator,
      CurveExtrapolator leftExtrapolator,
      CurveExtrapolator rightExtrapolator) {

    this.name = ArgChecker.notNull(name, "name");
    this.xValueType = xValueType;
    this.yValueType = yValueType;
    this.dayCount = dayCount;
    this.nodes = ImmutableList.copyOf(nodes);
    this.interpolator = ArgChecker.notNull(interpolator, "interpolator");
    this.leftExtrapolator = ArgChecker.notNull(leftExtrapolator, "leftExtrapolator");
    this.rightExtrapolator = ArgChecker.notNull(rightExtrapolator, "rightExtrapolator");
  }

  //-------------------------------------------------------------------------
  @Override
  public CurveMetadata metadata(LocalDate valuationDate) {
    List<CurveParameterMetadata> nodeMetadata = nodes.stream()
        .map(node -> node.metadata(valuationDate))
        .collect(toImmutableList());
    return DefaultCurveMetadata.builder()
        .curveName(name)
        .xValueType(xValueType)
        .yValueType(yValueType)
        .dayCount(dayCount)
        .parameterMetadata(nodeMetadata)
        .build();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code InterpolatedCurveConfig}.
   * @return the meta-bean, not null
   */
  public static InterpolatedCurveConfig.Meta meta() {
    return InterpolatedCurveConfig.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(InterpolatedCurveConfig.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static InterpolatedCurveConfig.Builder builder() {
    return new InterpolatedCurveConfig.Builder();
  }

  @Override
  public InterpolatedCurveConfig.Meta metaBean() {
    return InterpolatedCurveConfig.Meta.INSTANCE;
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
  @Override
  public CurveName getName() {
    return name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the x-value type, providing meaning to the x-values of the curve.
   * <p>
   * This type provides meaning to the x-values. For example, the x-value might
   * represent a year fraction, as represented using {@link ValueType#YEAR_FRACTION}.
   * <p>
   * If using the builder, this defaults to {@link ValueType#UNKNOWN}.
   * @return the value of the property, not null
   */
  public ValueType getXValueType() {
    return xValueType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the y-value type, providing meaning to the y-values of the curve.
   * <p>
   * This type provides meaning to the y-values. For example, the y-value might
   * represent a year fraction, as represented using {@link ValueType#ZERO_RATE}.
   * <p>
   * If using the builder, this defaults to {@link ValueType#UNKNOWN}.
   * @return the value of the property, not null
   */
  public ValueType getYValueType() {
    return yValueType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count, optional.
   * <p>
   * If the x-value of the curve represents time as a year fraction, the day count
   * can be specified to define how the year fraction is calculated.
   * @return the optional value of the property, not null
   */
  public Optional<DayCount> getDayCount() {
    return Optional.ofNullable(dayCount);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the nodes in the curve.
   * <p>
   * The nodes are used to find the par rates and calibrate the curve.
   * @return the value of the property, not null
   */
  public ImmutableList<CurveNode> getNodes() {
    return nodes;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the interpolator used to find points on the curve.
   * @return the value of the property, not null
   */
  public CurveInterpolator getInterpolator() {
    return interpolator;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the extrapolator used to find points to the left of the leftmost point on the curve.
   * @return the value of the property, not null
   */
  public CurveExtrapolator getLeftExtrapolator() {
    return leftExtrapolator;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the extrapolator used to find points to the right of the rightmost point on the curve.
   * @return the value of the property, not null
   */
  public CurveExtrapolator getRightExtrapolator() {
    return rightExtrapolator;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      InterpolatedCurveConfig other = (InterpolatedCurveConfig) obj;
      return JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getXValueType(), other.getXValueType()) &&
          JodaBeanUtils.equal(getYValueType(), other.getYValueType()) &&
          JodaBeanUtils.equal(dayCount, other.dayCount) &&
          JodaBeanUtils.equal(getNodes(), other.getNodes()) &&
          JodaBeanUtils.equal(getInterpolator(), other.getInterpolator()) &&
          JodaBeanUtils.equal(getLeftExtrapolator(), other.getLeftExtrapolator()) &&
          JodaBeanUtils.equal(getRightExtrapolator(), other.getRightExtrapolator());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getXValueType());
    hash = hash * 31 + JodaBeanUtils.hashCode(getYValueType());
    hash = hash * 31 + JodaBeanUtils.hashCode(dayCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(getNodes());
    hash = hash * 31 + JodaBeanUtils.hashCode(getInterpolator());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLeftExtrapolator());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRightExtrapolator());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(288);
    buf.append("InterpolatedCurveConfig{");
    buf.append("name").append('=').append(getName()).append(',').append(' ');
    buf.append("xValueType").append('=').append(getXValueType()).append(',').append(' ');
    buf.append("yValueType").append('=').append(getYValueType()).append(',').append(' ');
    buf.append("dayCount").append('=').append(dayCount).append(',').append(' ');
    buf.append("nodes").append('=').append(getNodes()).append(',').append(' ');
    buf.append("interpolator").append('=').append(getInterpolator()).append(',').append(' ');
    buf.append("leftExtrapolator").append('=').append(getLeftExtrapolator()).append(',').append(' ');
    buf.append("rightExtrapolator").append('=').append(JodaBeanUtils.toString(getRightExtrapolator()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code InterpolatedCurveConfig}.
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
        this, "name", InterpolatedCurveConfig.class, CurveName.class);
    /**
     * The meta-property for the {@code xValueType} property.
     */
    private final MetaProperty<ValueType> xValueType = DirectMetaProperty.ofImmutable(
        this, "xValueType", InterpolatedCurveConfig.class, ValueType.class);
    /**
     * The meta-property for the {@code yValueType} property.
     */
    private final MetaProperty<ValueType> yValueType = DirectMetaProperty.ofImmutable(
        this, "yValueType", InterpolatedCurveConfig.class, ValueType.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> dayCount = DirectMetaProperty.ofImmutable(
        this, "dayCount", InterpolatedCurveConfig.class, DayCount.class);
    /**
     * The meta-property for the {@code nodes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableList<CurveNode>> nodes = DirectMetaProperty.ofImmutable(
        this, "nodes", InterpolatedCurveConfig.class, (Class) ImmutableList.class);
    /**
     * The meta-property for the {@code interpolator} property.
     */
    private final MetaProperty<CurveInterpolator> interpolator = DirectMetaProperty.ofImmutable(
        this, "interpolator", InterpolatedCurveConfig.class, CurveInterpolator.class);
    /**
     * The meta-property for the {@code leftExtrapolator} property.
     */
    private final MetaProperty<CurveExtrapolator> leftExtrapolator = DirectMetaProperty.ofImmutable(
        this, "leftExtrapolator", InterpolatedCurveConfig.class, CurveExtrapolator.class);
    /**
     * The meta-property for the {@code rightExtrapolator} property.
     */
    private final MetaProperty<CurveExtrapolator> rightExtrapolator = DirectMetaProperty.ofImmutable(
        this, "rightExtrapolator", InterpolatedCurveConfig.class, CurveExtrapolator.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name",
        "xValueType",
        "yValueType",
        "dayCount",
        "nodes",
        "interpolator",
        "leftExtrapolator",
        "rightExtrapolator");

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
        case -868509005:  // xValueType
          return xValueType;
        case -1065022510:  // yValueType
          return yValueType;
        case 1905311443:  // dayCount
          return dayCount;
        case 104993457:  // nodes
          return nodes;
        case 2096253127:  // interpolator
          return interpolator;
        case -1992066886:  // leftExtrapolator
          return leftExtrapolator;
        case 1202004815:  // rightExtrapolator
          return rightExtrapolator;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public InterpolatedCurveConfig.Builder builder() {
      return new InterpolatedCurveConfig.Builder();
    }

    @Override
    public Class<? extends InterpolatedCurveConfig> beanType() {
      return InterpolatedCurveConfig.class;
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
     * The meta-property for the {@code xValueType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ValueType> xValueType() {
      return xValueType;
    }

    /**
     * The meta-property for the {@code yValueType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ValueType> yValueType() {
      return yValueType;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DayCount> dayCount() {
      return dayCount;
    }

    /**
     * The meta-property for the {@code nodes} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableList<CurveNode>> nodes() {
      return nodes;
    }

    /**
     * The meta-property for the {@code interpolator} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurveInterpolator> interpolator() {
      return interpolator;
    }

    /**
     * The meta-property for the {@code leftExtrapolator} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurveExtrapolator> leftExtrapolator() {
      return leftExtrapolator;
    }

    /**
     * The meta-property for the {@code rightExtrapolator} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurveExtrapolator> rightExtrapolator() {
      return rightExtrapolator;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((InterpolatedCurveConfig) bean).getName();
        case -868509005:  // xValueType
          return ((InterpolatedCurveConfig) bean).getXValueType();
        case -1065022510:  // yValueType
          return ((InterpolatedCurveConfig) bean).getYValueType();
        case 1905311443:  // dayCount
          return ((InterpolatedCurveConfig) bean).dayCount;
        case 104993457:  // nodes
          return ((InterpolatedCurveConfig) bean).getNodes();
        case 2096253127:  // interpolator
          return ((InterpolatedCurveConfig) bean).getInterpolator();
        case -1992066886:  // leftExtrapolator
          return ((InterpolatedCurveConfig) bean).getLeftExtrapolator();
        case 1202004815:  // rightExtrapolator
          return ((InterpolatedCurveConfig) bean).getRightExtrapolator();
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
   * The bean-builder for {@code InterpolatedCurveConfig}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<InterpolatedCurveConfig> {

    private CurveName name;
    private ValueType xValueType;
    private ValueType yValueType;
    private DayCount dayCount;
    private List<CurveNode> nodes = ImmutableList.of();
    private CurveInterpolator interpolator;
    private CurveExtrapolator leftExtrapolator;
    private CurveExtrapolator rightExtrapolator;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(InterpolatedCurveConfig beanToCopy) {
      this.name = beanToCopy.getName();
      this.xValueType = beanToCopy.getXValueType();
      this.yValueType = beanToCopy.getYValueType();
      this.dayCount = beanToCopy.dayCount;
      this.nodes = beanToCopy.getNodes();
      this.interpolator = beanToCopy.getInterpolator();
      this.leftExtrapolator = beanToCopy.getLeftExtrapolator();
      this.rightExtrapolator = beanToCopy.getRightExtrapolator();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case -868509005:  // xValueType
          return xValueType;
        case -1065022510:  // yValueType
          return yValueType;
        case 1905311443:  // dayCount
          return dayCount;
        case 104993457:  // nodes
          return nodes;
        case 2096253127:  // interpolator
          return interpolator;
        case -1992066886:  // leftExtrapolator
          return leftExtrapolator;
        case 1202004815:  // rightExtrapolator
          return rightExtrapolator;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this.name = (CurveName) newValue;
          break;
        case -868509005:  // xValueType
          this.xValueType = (ValueType) newValue;
          break;
        case -1065022510:  // yValueType
          this.yValueType = (ValueType) newValue;
          break;
        case 1905311443:  // dayCount
          this.dayCount = (DayCount) newValue;
          break;
        case 104993457:  // nodes
          this.nodes = (List<CurveNode>) newValue;
          break;
        case 2096253127:  // interpolator
          this.interpolator = (CurveInterpolator) newValue;
          break;
        case -1992066886:  // leftExtrapolator
          this.leftExtrapolator = (CurveExtrapolator) newValue;
          break;
        case 1202004815:  // rightExtrapolator
          this.rightExtrapolator = (CurveExtrapolator) newValue;
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
    public InterpolatedCurveConfig build() {
      return new InterpolatedCurveConfig(
          name,
          xValueType,
          yValueType,
          dayCount,
          nodes,
          interpolator,
          leftExtrapolator,
          rightExtrapolator);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code name} property in the builder.
     * @param name  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder name(CurveName name) {
      JodaBeanUtils.notNull(name, "name");
      this.name = name;
      return this;
    }

    /**
     * Sets the {@code xValueType} property in the builder.
     * @param xValueType  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder xValueType(ValueType xValueType) {
      JodaBeanUtils.notNull(xValueType, "xValueType");
      this.xValueType = xValueType;
      return this;
    }

    /**
     * Sets the {@code yValueType} property in the builder.
     * @param yValueType  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder yValueType(ValueType yValueType) {
      JodaBeanUtils.notNull(yValueType, "yValueType");
      this.yValueType = yValueType;
      return this;
    }

    /**
     * Sets the {@code dayCount} property in the builder.
     * @param dayCount  the new value
     * @return this, for chaining, not null
     */
    public Builder dayCount(DayCount dayCount) {
      this.dayCount = dayCount;
      return this;
    }

    /**
     * Sets the {@code nodes} property in the builder.
     * @param nodes  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder nodes(List<CurveNode> nodes) {
      JodaBeanUtils.notNull(nodes, "nodes");
      this.nodes = nodes;
      return this;
    }

    /**
     * Sets the {@code nodes} property in the builder
     * from an array of objects.
     * @param nodes  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder nodes(CurveNode... nodes) {
      return nodes(ImmutableList.copyOf(nodes));
    }

    /**
     * Sets the {@code interpolator} property in the builder.
     * @param interpolator  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder interpolator(CurveInterpolator interpolator) {
      JodaBeanUtils.notNull(interpolator, "interpolator");
      this.interpolator = interpolator;
      return this;
    }

    /**
     * Sets the {@code leftExtrapolator} property in the builder.
     * @param leftExtrapolator  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder leftExtrapolator(CurveExtrapolator leftExtrapolator) {
      JodaBeanUtils.notNull(leftExtrapolator, "leftExtrapolator");
      this.leftExtrapolator = leftExtrapolator;
      return this;
    }

    /**
     * Sets the {@code rightExtrapolator} property in the builder.
     * @param rightExtrapolator  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder rightExtrapolator(CurveExtrapolator rightExtrapolator) {
      JodaBeanUtils.notNull(rightExtrapolator, "rightExtrapolator");
      this.rightExtrapolator = rightExtrapolator;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(288);
      buf.append("InterpolatedCurveConfig.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(name)).append(',').append(' ');
      buf.append("xValueType").append('=').append(JodaBeanUtils.toString(xValueType)).append(',').append(' ');
      buf.append("yValueType").append('=').append(JodaBeanUtils.toString(yValueType)).append(',').append(' ');
      buf.append("dayCount").append('=').append(JodaBeanUtils.toString(dayCount)).append(',').append(' ');
      buf.append("nodes").append('=').append(JodaBeanUtils.toString(nodes)).append(',').append(' ');
      buf.append("interpolator").append('=').append(JodaBeanUtils.toString(interpolator)).append(',').append(' ');
      buf.append("leftExtrapolator").append('=').append(JodaBeanUtils.toString(leftExtrapolator)).append(',').append(' ');
      buf.append("rightExtrapolator").append('=').append(JodaBeanUtils.toString(rightExtrapolator));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
